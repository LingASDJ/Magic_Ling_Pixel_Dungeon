# MLPD 每日挑战服务端

《魔棱地牢》每日挑战（每日狩猎）的服务端，只提供游戏内排行榜使用的 API；网页排行榜不再使用。

## 运行

```bash
cd server/daily
npm install --omit=dev     # 首次运行；仓库内也可直接使用已安装的 node_modules
PORT=28773 node app.js     # 默认端口 28773，默认 HTTPS
```

- Node.js >= 14（推荐 18+）
- **HTTPS 为默认**；客户端地址固定为 `https://47.109.62.104:28773/MLPD/api/v1/daily`
- 证书默认读取 `tls/key.pem` / `tls/cert.pem`，未提供时可用同目录自签证书（客户端已有 trust-all）
- 生成自签证书：
  ```bash
  mkdir -p tls
  openssl req -x509 -newkey rsa:2048 -nodes -keyout tls/key.pem -out tls/cert.pem \
    -days 3650 -subj "/CN=47.109.62.104" -addext "subjectAltName=IP:47.109.62.104"
  ```
- 仅本地 HTTP 调试：`DAILY_TLS=false PORT=28773 node app.js`（Android 客户端不能使用 HTTP）
- 可用 `DAILY_TLS_KEY` / `DAILY_TLS_CERT` 指定其他证书路径
- 数据库：`daily.db`（sql.js/SQLite），目录必须可写；`DAILY_DB` 环境变量可覆盖路径
- 旧数据库会自动 `ALTER TABLE` 补齐字段，不会清空已有数据

## 每日规则（服务端权威）

- 日期：UTC，`yyyy-MM-dd`
- 种子：
  - **旧日期（< `2026-11-08` UTC）**：沿用原版公式 `UTC 当天 0 点毫秒 + 5429503678976`，保证已有记录/排行榜不变；该值超出 `[0, TOTAL_SEEDS)`，客户端必须用 `String.valueOf`/安全转换兼容。
  - **新日期（>= `2026-11-08` UTC）**：`HMAC-SHA256(secret, "mlpd-daily-seed-v2:" + date)` 取前 8 字节，对 `TOTAL_SEEDS` 取模，得到 `[0, TOTAL_SEEDS)` 内的确定性、非线性种子；不再有每天固定 `+86,400,000` 的规律。
  - 返回字段新增 `seedVersion`：旧日期 `1`，新日期 `2`。客户端不需要识别，直接用 `seed` 即可。
- 难度：`NORMAL`（正式调查）/ `HARD`（直面灾厄）按日期确定性随机
- 挑战：数量 1~16 完全随机，从全部 16 个挑战位中随机抽取，不做保护
- 每天每个 `playerId` 的首次挑战只能提交一次，进入日榜
- 重玩成绩不进入日榜，写入独立的**重玩榜**；每名玩家每天只保留**最高分**的一条重玩记录
- 日榜/重玩榜都只按 `total_score` 降序
- 两个榜都按 **UTC 日期**每日刷新（UTC 0:00 / 北京 08:00）；查询只返回指定日期，数据库保留历史行但不会混入当天榜

### 种子 v2 环境变量

| 变量 | 默认 | 说明 |
|---|---|---|
| `MLPD_DAILY_SEED_V2_START` | `2026-11-08` | 从该 UTC 日期起使用 v2 种子；之前保持旧公式 |
| `MLPD_DAILY_SEED_SECRET` | 内置开发用盐 | 生产环境务必覆盖；丢失会导致同一日期的 v2 种子变化 |
| `MLPD_DAILY_ALLOW_FUTURE_DATES` | 未设置 | 仅诊断用；设为 `true` 才允许 `/today?date=` 查询未来日期 |

> `/today?date=` 默认只允许今天及以前，防止提前获取未来每日种子。作者之前用于批量查询的 30 天 bat 需要管理员临时打开 `MLPD_DAILY_ALLOW_FUTURE_DATES`，或改用服务器本地脚本；不要在生产长期开启。

## API

基础路径：`/MLPD/api/v1/daily`

### GET `/today`

返回当天配置。可选 `?date=yyyy-MM-dd` 查询指定日期（只能是今天或过去；默认拒绝未来日期，防止提前获取种子）。

```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "date": "2026-11-03",
    "seed": 7223167678976,
    "seedVersion": 1,
    "difficulty": "NORMAL",
    "challenges": 32735,
    "challengeCount": 14
  }
}
```

### POST `/submit`

```json
{
  "playerId": "持久 UUID",
  "playerName": "联机名",
  "date": "2026-11-03",
  "seed": 7223167678976,
  "difficulty": "NORMAL",
  "challenges": 32735,
  "replay": false,
  "gameData": "{游戏 Bundle.toString() JSON}"
}
```

服务端会重新解析 `gameData` 取成绩，并校验日期/种子/难度/挑战是否与当天配置一致。

- `replay: false`：首次挑战，写入日榜；同日重复提交返回 `1001`。
- `replay: true`：重玩成绩，写入重玩榜；同一玩家同一天可多次提交，
  只保留最高分那一条（返回 `data.score` 为当前最高分）。

返回：
- `code: 0` 提交成功，`data.rank` / `data.totalPlayers`（重玩另有 `data.score`）
- `code: 1001` 今日已提交过分数（仅首次挑战）
- `code: 1002` 种子/难度/挑战/日期不匹配
- `code: -1` 参数或服务器错误

### GET `/leaderboard?date=yyyy-MM-dd&page=1&pageSize=20`

> **新客户端应带 `summary=1`**：列表只返回摘要字段，`gameData` 为空；用户点开详情时再调用
> `GET /entry?id=<entry.id>&source=daily|replay` 取单条完整快照。这样周榜/日榜列表的响应
> 从“每人一份快照”降到几百字节/条。
>
> 不传 `summary` 的旧客户端保持原行为：每条 entry 仍包含完整 `gameData`（每日快照 JSON），
> 供游戏内“日志排行榜”风格的详情窗口还原英雄属性、天赋、物品和快捷栏。分页条数较大时
> 响应会明显变大。

```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "date": "2026-11-03",
    "totalPlayers": 1,
    "page": 1,
    "pageSize": 20,
    "myRank": 0,
    "myScore": 0,
    "entries": [
      {
        "rank": 1,
        "playerId": "持久 UUID",
        "playerName": "联机名",
        "heroClass": "WARRIOR",
        "heroSubClass": "NONE",
        "heroLevel": 1,
        "score": 12345,
        "won": true,
        "depth": 20,
        "challenges": 32735,
        "challengeCount": 14,
        "difficulty": 1
      }
    ]
  }
}
```

### GET `/replay?date=yyyy-MM-dd&page=1&pageSize=20`

重玩榜。每名玩家每天最多一条记录（多次重玩只保留最高分），按分数降序。
字段与 `/leaderboard` 相同，`data.entries` 可直接用于游戏内详情窗口。

```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "date": "2026-11-14",
    "totalPlayers": 2,
    "page": 1,
    "pageSize": 20,
    "entries": [
      {
        "rank": 1,
        "playerId": "持久 UUID",
        "playerName": "联机名",
        "score": 88888,
        "depth": 10,
        "gameData": "{最高分重玩快照 JSON}"
      }
    ]
  }
}
```

### GET `/weekly?weekStart=yyyy-MM-dd&page=1&pageSize=20`

周榜（UTC 周一 00:00 至周日 23:59）。默认返回本周；`weekStart` 会归一化到所在周的周一，
也可以传 `date=`（该周内任意一天）代替。服务端按玩家聚合：

- 每个玩家取本周**最高分**的一局作为 `score`/`depth`/`gameData` 等字段；
- `runs`：本周提交成绩的天数；
- `gold` / `silver` / `bronze`：本周拿到日榜第 1/2/3 名的天数；
- 排序：最高分降序 → 深度降序 → 玩家名升序（与客户端旧版逐日合并逻辑一致）。

```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "weekStart": "2026-11-09",
    "weekEnd": "2026-11-15",
    "totalPlayers": 1,
    "page": 1,
    "pageSize": 20,
    "myRank": 0,
    "myScore": 0,
    "entries": [
      {
        "rank": 1,
        "date": "2026-11-10",
        "playerId": "持久 UUID",
        "playerName": "联机名",
        "heroClass": "MAGE",
        "heroLevel": 30,
        "score": 1234567,
        "depth": 25,
        "won": true,
        "runs": 3,
        "gold": 1,
        "silver": 1,
        "bronze": 0,
        "gameData": "{最佳一局的每日快照 JSON}"
      }
    ]
  }
}
```

> 客户端 `WeeklyRankingsScene` 优先请求 `/weekly`；旧服务端没有该接口时会自动回退到
> 逐日拉取 7 份日榜并在本地合并。
>
> 新客户端只请求当前显示页（`pageSize=20`），并带 `summary=1`；服务端按页从周榜缓存
> 切片，不再把整周所有玩家和快照读一遍。

### GET `/entry?id=<rowId>&source=daily|replay`

按行 id 取单条完整记录（含 `gameData`），供新客户端的详情窗口按需加载。
`source` 默认 `daily`；重玩榜记录传 `source=replay`。列表 entry 中的 `id` 字段
就是这里的 `id`（周榜取本周最佳那一局的 id）。

```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "id": 123,
    "date": "2026-11-15",
    "playerName": "冒险者",
    "score": 123456,
    "gameData": "{完整快照 JSON}"
  }
}
```

- 记录不存在时返回 `code: -1`。
- 新服务端对 `/leaderboard`、`/weekly`、`/replay` 支持 `summary=1`；
  不带该参数的旧客户端仍返回完整 `gameData`。

## 分数修正

旧版本客户端（`Rankings.calculateScore()` 尚未排除每日挑战）在提交每日成绩时会把
`seedCustom` 的 0.5 种子惩罚也算进去，导致排行榜显示实际分数的一半。

- 新提交：`/submit` 会从 `gameData` 的统计分项（`prog_score`/`tres_score`/
  `expl_score`/`tot_boss`/`tot_quest`/`win_mult`/`chal_mult`/`bad_mult`/`good_mult`
  和 `conduct`）按客户端的 int/float 运算顺序重新计算分数。快照包含完整分项时，
  服务端计算结果就是榜单分数，客户端提交的 `total_score` 不再作为可信来源。
- 历史数据：服务启动时 `initDatabase()` 会执行一次 `daily_score_repair_v2` 迁移，
  扫描 `daily_scores` 与 `replay_scores`，把所有含完整分项的行修正为重新计算的分数；
  完成后在 `meta` 表写入标记，不会重复执行（即使之前跑过 v1 迁移也会再跑一次）。
- 藏品倍率修正：客户端 `Rankings.calculateScore()` 以前把藏品倍率
  `(int)(bad_mult + good_mult)` 截断成整数（界面显示 3.7x，实际只按 3x 结算）。
  修复后倍率按浮点参与计分，服务端 `expectedDailyScore()` 同步改为浮点倍率并四舍五入。
  已按旧公式写入的历史分数由 `daily_score_repair_v3` 迁移按快照分项再重算一次
  （浮点倍率不小于截断值，因此只会持平或上调）。
- 缺少完整分项的旧快照保持原值，避免把未知格式算成 0。

## 性能与缓存

- 周榜聚合结果按 `weekStart` 缓存在内存里（最多 12 周，只存摘要、不存 `gameData`）。
  过去周榜不可变、永久缓存；当前周缓存 30 秒，同一天的新成绩最多延迟 30 秒出现在周榜。
  对过去日期的补交会立即让对应周缓存失效。
- 周榜查询先只读排行榜窄字段，分页后再按本页的 row id 取 `game_data`；
  旧客户端仍能拿到完整详情，新客户端用 `summary=1` + `/entry` 完全跳过列表快照。
- 周榜客户端每次只请求当前页（20 条），不再一次拉完整周榜。
- 榜单 GET 响应带 `Cache-Control`：过去日榜/周榜可缓存 1 天，当天 30 秒；
  重玩榜因为可能被更高分覆盖，始终 30 秒。
- `daily_scores.game_data` 仍保存在原表；后续可考虑拆分/压缩，以控制数据库体积
  和 `sql.js` 全量落盘成本。

## 数据库

`daily_scores` 表保留原有字段，并新增：

- `player_id`：玩家持久 UUID（旧数据为 NULL）
- `daily_replay`：历史字段（新重玩成绩写入 `replay_scores`，该列保持兼容）

唯一索引 `(date, player_id)` 保证每天每人只有一条正式成绩。

`replay_scores` 表保存重玩成绩：字段与 `daily_scores` 基本一致，
唯一索引 `(date, player_id)` 保证每名玩家每天只有一条，提交更高分时原地更新。

`meta` 表记录一次性迁移标记（`daily_score_repair_v1` / `daily_score_repair_v2` / `daily_score_repair_v3`）。
