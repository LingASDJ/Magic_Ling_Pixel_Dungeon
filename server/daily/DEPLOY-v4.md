# 每日挑战服务端 v4 更新（2026-11-14）

本次更新在 v3（周榜 + 重玩榜）基础上，把**榜单分数改为服务端权威计算**，
并修复历史上因旧客户端 0.5 种子惩罚而显示一半的分数。

> v4 包含 v3 的全部改动。如果服务器还停留在 v2（`/weekly`、`/replay` 返回 404），
> 直接按本文升级即可，无需先单独部署 v3。

## 问题

旧客户端在每日挑战里把「自定义种子 0.5 倍惩罚」也算了进去，提交的
`gameData.total_score` 只有真实分数的一半。v3 的迁移只修「存储值恰好是预期值一半」
的行；如果存储值因四舍五入/不同版本略有偏差，或者服务器根本没升级到 v3，
榜单会一直显示错误分数。例如 2026-11-12 的 `knox-小刻`：

- 榜单存储/显示：`29,017,816`
- 快照分项真实分数：`58,035,640`
- 旧迁移：未修正（线上仍是 29,017,816）

## v4 改动

| 文件 | 说明 |
| --- | --- |
| `utils/bundle.js` | `expectedDailyScore()` 按客户端 int/float 运算顺序精确重算；`reconcileDailyScore()` 在分项完整时**始终返回服务端计算值** |
| `db.js` | 启动迁移改为 `daily_score_repair_v2`，同时扫描 `daily_scores` 与 `replay_scores`；即使已跑过 v1 也会再执行一次 |
| `routes/daily.js` | 新提交/重玩提交都使用服务端重算后的分数（v3 已有） |
| `test-score.js` | 新增回归测试：用 2026-11-12 的真实快照验证 `29,017,816 -> 58,035,640`，以及启动迁移 |
| `README.md` | 分数修正说明更新 |

客户端侧同时修复：`Dungeon.takeDailySnapshot()` 在打包快照前显式调用
`Rankings.calculateScore(true)`，保证每日快照永远不包含种子惩罚；以后新客户端提交的
`total_score` 本身就是正确值。

## 部署步骤

1. 备份服务器上的旧文件（路径换成实际 daily 目录）：
   ```powershell
   $d = "C:\path\to\daily"
   Copy-Item "$d\db.js" "$d\db.js.bak-20261114"
   Copy-Item "$d\routes\daily.js" "$d\routes\daily.js.bak-20261114"
   Copy-Item "$d\utils\bundle.js" "$d\utils\bundle.js.bak-20261114"
   Copy-Item "$d\utils\seed.js" "$d\utils\seed.js.bak-20261114"
   Copy-Item "$d\daily.db" "$d\daily.db.bak-20261114"
   ```
2. 把本包中的 `db.js`、`routes\daily.js`、`utils\bundle.js`、`utils\seed.js`、
   `README.md`、`DEPLOY-v4.md`、`test-score.js` 覆盖/复制到对应目录。
3. 用原先启动每日服务的方式重启 Node（计划任务 / pm2 / 手动窗口）。
   **不要重启整台服务器，不要动 Apache/MySQL 等其他程序。**
4. 启动日志应出现：
   ```
   [DB] 旧分数修复(v2): daily=N replay=M，前几行: id:旧分->新分...
   [DB] 数据库初始化完成
   ```
   `N/M` 是被修正的行数；标记写入 `meta` 表，之后重启不会重复扫描。

## 验证

```powershell
# 2026-11-12 日榜：knox-小刻 应显示 58035640
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/leaderboard?date=2026-11-12&page=1&pageSize=5"

# v3 接口（v4 包含）
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/weekly"
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/replay?date=2026-11-12&page=1&pageSize=5"

# 本地回归测试（在服务器 daily 目录）
node test-score.js
node test-replay.js
node test-weekly.js
```

也可以先不部署，在本地复制一份 `daily.db` 用
`DAILY_DB=... node -e "require('./db').initDatabase()"` 验证迁移结果。

## 回滚

把备份的 `db.js`、`routes\daily.js`、`utils\bundle.js`、`utils\seed.js` 覆盖回去，
再用 `daily.db.bak-20261114` 覆盖数据库（会丢失升级后新提交的成绩，一般不需要），
重启 Node。

## 说明

- 重玩榜每名玩家每天只保留最高分；旧服务器没有 `/replay` 时，历史重玩成绩没有入库，
  无法补录，只能重新提交一次重玩成绩。
- 2026-11-12 的 29,017,816 与本地历史里的 57,010,440 不是同一份快照：
  该行快照的分项精确计算结果为 58,035,640；57,010,440 对应另一局（通常是重玩），
  属于重玩榜而不是每日仅一次的主榜。
- 周榜读取修复后的 `daily_scores`，因此部署后周榜分数也会同步修正。
