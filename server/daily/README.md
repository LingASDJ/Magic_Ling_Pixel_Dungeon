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
- 每天每个 `playerId` 只能提交一次；重玩不能提交
- 排行榜只按 `total_score` 降序
- 排行榜按 **UTC 日期**每日刷新（UTC 0:00 / 北京 08:00）；`/leaderboard` 只查询指定日期，数据库保留历史行但不会混入当天榜

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

返回：
- `code: 0` 提交成功，`data.rank` / `data.totalPlayers`
- `code: 1001` 今日已提交过分数 / 重玩不计分
- `code: 1002` 种子/难度/挑战/日期不匹配
- `code: -1` 参数或服务器错误

### GET `/leaderboard?date=yyyy-MM-dd&page=1&pageSize=20`

> 每条 entry 还包含完整 `gameData`（每日快照 JSON），供游戏内“日志排行榜”风格的详情窗口还原
> 英雄属性、天赋、物品和快捷栏。分页条数较大时响应会明显变大。

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

## 数据库

`daily_scores` 表保留原有字段，并新增：

- `player_id`：玩家持久 UUID（旧数据为 NULL）
- `daily_replay`：是否重玩（服务端拒绝重玩提交）

唯一索引 `(date, player_id)` 保证每天每人只有一条成绩。
