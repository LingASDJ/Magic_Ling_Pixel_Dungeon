# 每日挑战服务端 v2 更新（2026-11-07）

本包只更新每日挑战服务端代码，不改数据库、不改 Apache、不改 HTTPS 证书。

## 改动

- `utils/seed.js`：从 `MLPD_DAILY_SEED_V2_START`（默认 `2026-11-08` UTC）起改用
  `HMAC-SHA256(secret, "mlpd-daily-seed-v2:" + date) mod TOTAL_SEEDS`；
  之前的日期仍用旧的 `UTC 0 点毫秒 + 5429503678976`，已有排行榜记录不变。
- `routes/daily.js`：`/today?date=` 默认禁止查询未来日期；新增 `seedVersion` 字段随配置返回。
- `test-seed.js`：本地种子回归测试（不依赖数据库）。
- `README.md`：规则与 API 文档更新。

## 部署步骤

1. 备份服务器上的旧文件：
   ```powershell
   $d = "C:\path\to\daily"   # 换成实际 daily 目录
   Copy-Item "$d\utils\seed.js" "$d\utils\seed.js.bak-20261107"
   Copy-Item "$d\routes\daily.js" "$d\routes\daily.js.bak-20261107"
   ```
2. 把本包里的 `utils\seed.js`、`routes\daily.js`、`README.md` 覆盖到对应目录；
   `test-seed.js`/`DEPLOY-v2.md` 可放在 daily 目录，仅用于测试/说明。
3. 设置生产环境 secret（只需设置一次，之后**不要修改**；修改会导致新日期的 v2 种子变化）：
   ```powershell
   # 生成一个随机 secret
   $secret = -join ((48..57)+(65..90)+(97..122) | Get-Random -Count 48 | ForEach-Object {[char]$_})
   Write-Output $secret
   # 写入机器级环境变量（管理员 PowerShell）
   [Environment]::SetEnvironmentVariable('MLPD_DAILY_SEED_SECRET', $secret, 'Machine')
   ```
   如果服务是用固定脚本/任务启动的，也可以在该脚本里设置
   `$env:MLPD_DAILY_SEED_SECRET = '...'`，但不要提交到公开仓库。
   - 可选：`MLPD_DAILY_SEED_V2_START=2026-11-08`（默认值，可省略）。
   - 不要设置 `MLPD_DAILY_ALLOW_FUTURE_DATES`，除非临时诊断；诊断完必须删掉并重启。
4. 用**原先启动每日服务的方式**重启 Node（计划任务 / pm2 / 手动窗口）。
   - 不要重启整台服务器，不要动 Apache/MySQL 等其他程序。
5. 验证：
   ```powershell
   # 旧日期：seedVersion 1，seed 仍是 14 位旧公式
   curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/today?date=2026-11-07"
   # 未来日期：应返回 code=1002（默认禁止提前取种子）
   curl.exe -k "https://dailymlpd.spldream.com:28773/MLPD/api/v1/daily/today?date=2026-11-09"
   # 本地单元测试
   node test-seed.js
   ```
   `2026-11-08` 当天（UTC）之后，`/today` 的正常请求会返回 `seedVersion:2`
   且 `seed < 5429503678976`，相邻日期不再相差固定的 86,400,000。
   - 若必须在 11-08 之前预检查 v2：临时设置
     `MLPD_DAILY_ALLOW_FUTURE_DATES=true` 并重启，查询 `?date=2026-11-08`，
     **检查完立即删除该变量并再次重启**。

## 回滚

把备份的 `utils/seed.js`、`routes/daily.js` 覆盖回去，重启 Node；环境变量可保留（旧代码不读取）。
