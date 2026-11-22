# 每日挑战服务端 v5 更新（2026-11-22）——已作废，请使用 v6

> **不要部署本包**：v5 的 `utils/bundle.js` 是旧版整数藏品倍率，会覆盖作者线上的
> v3 浮点倍率修正。请使用 `DEPLOY-v6.md` / `daily-v6-patch.zip`（v6 = v3 + v5 性能）。

本次更新针对“数据库变大后周榜加载越来越慢”的问题，重点是**不再让周榜列表
携带完整 `gameData`**、**周榜聚合结果缓存**、**客户端按页请求/详情懒加载**。

> v5 包含 v4 的全部改动（服务端权威分数、周榜、重玩榜）。
> 如果服务器还是 v2 或更早，直接按本文升级即可。

## 一、服务端改动

| 文件 | 说明 |
| --- | --- |
| `db.js` | 周榜聚合不再 `SELECT game_data`；只读窄字段，分页后按本页 row id 取快照。周榜摘要按 `weekStart` 缓存（最多 12 周；过去周永久缓存，当前周 30 秒 TTL）。列表 entry 新增 `id`。新增 `getLeaderboardEntry()`。`/leaderboard`、`/weekly`、`/replay` 支持 `summary` 参数。 |
| `routes/daily.js` | 新增 `GET /entry?id=<rowId>&source=daily\|replay`；列表接口透传 `summary=1`；GET 榜单增加 `Cache-Control`。 |
| `public/app.js` | 网页排行榜改带 `summary=1`（只显示摘要，不再下载快照）。 |
| `utils/bundle.js`、`utils/seed.js` | 与 v4 相同，随包附带，方便从更旧版本一次升级。 |
| `README.md` | API/性能说明更新。 |
| `test-weekly.js` / `test-replay.js` | 新增 summary、`/entry`、缓存失效等回归断言。 |

### 兼容性

- **旧客户端**（不带 `summary`）：行为不变，列表仍返回完整 `gameData`；
  服务端只是不再为聚合扫描整周快照，分页后补取本页快照。
- **新客户端**（带 `summary=1`）：列表只返回摘要，`gameData` 为空；
  点开详情时请求 `/entry`。
- **旧服务端**：新客户端也能工作。`summary=1` 会被旧服务端忽略，列表仍带
  `gameData`，详情直接打开，不会发 `/entry`。
- 榜单排序、金银铜、`runs`、分页 rank 的定义完全不变。

## 二、部署步骤

1. 备份服务器上的旧文件：
   ```powershell
   $d = "C:\path\to\daily"
   Copy-Item "$d\db.js" "$d\db.js.bak-20261122"
   Copy-Item "$d\routes\daily.js" "$d\routes\daily.js.bak-20261122"
   Copy-Item "$d\daily.db" "$d\daily.db.bak-20261122"
   ```
2. 用本包中的文件覆盖：
   - `db.js`
   - `routes\daily.js`
   - `utils\bundle.js`、`utils\seed.js`（与 v4 相同；服务器很旧时一并覆盖）
   - `public\app.js`（如果服务器还提供网页排行榜）
   - `README.md`
   - `DEPLOY-v5.md`
   - `test-weekly.js` / `test-replay.js`（仅测试用）
3. 用原先启动每日服务的方式重启 Node（计划任务 / pm2 / 手动窗口）。
   **不要重启整台服务器，不要动 Apache/MySQL 等其他程序。**
4. 启动日志应正常出现 `[DB] 数据库初始化完成`；如果线上之前没有跑过
   `daily_score_repair_v2`，会先出现一次修复日志（与 v4 相同）。

## 三、验证

```powershell
# 1) 旧行为：不带 summary，仍应包含 gameData
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/weekly?page=1&pageSize=20"

# 2) 新行为：summary=1，gameData 应为空字符串，entry 带 id
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/weekly?page=1&pageSize=20&summary=1"

# 3) 详情：用上一步的 id 取完整快照
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/entry?id=123&source=daily"

# 4) 日榜/重玩榜 summary
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/leaderboard?date=2026-11-22&page=1&pageSize=20&summary=1"
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/replay?date=2026-11-22&page=1&pageSize=20&summary=1"

# 5) 本地回归（在服务器 daily 目录）
node test-weekly.js
node test-replay.js
node test-score.js
```

观察 `content-length`/`size_download`：`summary=1` 的周榜/日榜应明显小于
不带 `summary` 的响应；周榜翻页只请求当前页。

## 四、回滚

把 `db.js`、`routes\daily.js` 覆盖回 `.bak-20261122` 文件，重启 Node 即可。
本次没有数据库结构变更，回滚不需要恢复 `daily.db`（成绩数据不受影响）。

## 五、已知未做

- 仍使用 `sql.js`：每次提交仍会把整个数据库 `export()` 并同步写盘。
  数据库继续变大时，提交和并发请求仍可能被阻塞；建议后续换
  `better-sqlite3`/`node:sqlite` + WAL，见 PROGRESS.md §115。
- `game_data` 仍与排行榜字段在同一张表；后续可拆分/压缩/设置保留期。
- 本次只做服务端与客户端源码；未部署、未重打稳定版 APK。
