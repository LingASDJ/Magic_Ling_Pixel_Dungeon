# 每日挑战服务端 v6 更新（2026-11-22）

v6 = **作者线上 v3（藏品倍率分数修正）** + **v5 周榜性能优化**。

> 注意：`daily-v5-patch.zip` 基于旧的 v2 分数公式，**不能直接部署**；
> 请使用本 v6 包。v5 的 `utils/bundle.js` 会把作者已修好的浮点倍率改回截断版本。

## 一、相对作者线上版本的改动

| 文件 | 改动 |
| --- | --- |
| `db.js` | 保留作者的 `repairLegacyDailyScores()` / `repairPropMultiplierScores()`（v3）；叠加 v5 性能改动：周榜聚合只读窄字段、周榜摘要缓存、列表 entry 带 `id`、`summary` 参数、`getLeaderboardEntry()`、`saveToDisk()` 去掉整库 Buffer 复制。 |
| `routes/daily.js` | 新增 `GET /entry?id=<rowId>&source=daily\|replay`；列表接口支持 `summary=1`；GET 榜单增加 `Cache-Control`。 |
| `utils/bundle.js` | 作者 v3：藏品倍率按浮点参与计分（`Math.round(score * props)`），与游戏内结算明细一致。**不要用 v5 包里的旧版本覆盖。** |
| `utils/seed.js` | 与作者线上相同。 |
| `README.md` | 合并 v3 分数说明 + v5 性能/API 说明。 |
| `test-weekly.js` / `test-replay.js` | 在作者测试基础上增加 summary、`/entry`、缓存断言。 |
| `test-score.js` | 作者 v3 版本（浮点倍率 + v3 迁移回归）。 |

### 兼容性

- 旧客户端不带 `summary`：行为不变，列表仍返回完整 `gameData`；
  服务端只是不再为聚合扫描整周快照，分页后补取本页快照。
- 新客户端带 `summary=1`：列表只返回摘要，详情点开时请求 `/entry`。
- 旧服务端：新客户端也能工作；`summary=1` 被忽略，列表仍带 `gameData`，详情直接打开。
- 排序、金银铜、`runs`、分页 rank、v3 分数公式均保持不变。

## 二、部署步骤

1. 备份服务器上的旧文件：
   ```powershell
   $d = "C:\path\to\daily"
   Copy-Item "$d\db.js" "$d\db.js.bak-20261122-v6"
   Copy-Item "$d\routes\daily.js" "$d\routes\daily.js.bak-20261122-v6"
   Copy-Item "$d\utils\bundle.js" "$d\utils\bundle.js.bak-20261122-v6"
   Copy-Item "$d\utils\seed.js" "$d\utils\seed.js.bak-20261122-v6"
   Copy-Item "$d\README.md" "$d\README.md.bak-20261122-v6"
   Copy-Item "$d\daily.db" "$d\daily.db.bak-20261122-v6"
   ```
2. 用本包覆盖：
   - `db.js`
   - `routes\daily.js`
   - `utils\bundle.js`（作者 v3；不要用 v5 的旧版）
   - `utils\seed.js`
   - `README.md`
   - `DEPLOY-v6.md`
   - `test-weekly.js` / `test-replay.js` / `test-score.js`（仅测试用）
3. 用原先启动每日服务的方式重启 Node（计划任务 / pm2 / 手动窗口）。
   **不要重启整台服务器，不要动 Apache/MySQL 等其他程序。**

启动日志：

- 线上已有 `daily_score_repair_v3` 标记时，不会重复修复；
- 如果是从更旧版本升级、没有 v3 标记，会看到一次
  `[DB] 藏品倍率分数修复(v3): daily=N replay=M ...`，这是预期行为。

## 三、验证

```powershell
# 1) 旧行为：不带 summary，仍应包含 gameData
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/weekly?page=1&pageSize=20"

# 2) 新行为：summary=1，gameData 为空，entry 带 id
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/weekly?page=1&pageSize=20&summary=1"

# 3) 详情：用上一步的 id 取完整快照
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/entry?id=123&source=daily"

# 4) 日榜/重玩榜 summary
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/leaderboard?date=2026-11-22&page=1&pageSize=20&summary=1"
curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/replay?date=2026-11-22&page=1&pageSize=20&summary=1"

# 5) 本地回归（在服务器 daily 目录）
node test-score.js
node test-weekly.js
node test-replay.js
node test-seed.js
```

验证要点：

- `test-score.js` 应输出 `test-score: OK`（v3 浮点倍率 + v3 迁移回归）；
- summary 响应的 `gameData` 应为空字符串，entry 带 `id`；
- 不带 `summary` 的旧格式仍返回完整 `gameData`；
- 周榜翻页只请求当前页。

## 四、回滚

把备份的 `db.js`、`routes\daily.js`、`utils\bundle.js`、`utils\seed.js` 覆盖回去，
重启 Node。本次没有数据库结构变更；如果 v6 启动时执行了 v3 修复，回滚旧代码也不会
把分数改回去（v3 只是把截断倍率改成浮点倍率，属于修正），需要时可从
`daily.db.bak-20261122-v6` 恢复数据库。

## 五、已知未做

- 仍使用 `sql.js`：每次提交仍会 `db.export()` 整个数据库并同步写盘。
  按作者当前 5MB 库测量，服务端耗时在毫秒级，不是本次卡顿主因；如果以后库明显变大，
  再考虑 Node v26 内置 `node:sqlite` + WAL。
- `game_data` 仍与排行榜字段在同一张表；后续可拆分/压缩/设置保留期。
- 本次不包含客户端 APK；客户端源码已同步，需另行重打。
