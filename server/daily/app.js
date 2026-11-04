const express = require('express');
const cors = require('cors');
const path = require('path');
const { initDatabase } = require('./db');
const dailyRouter = require('./routes/daily');

const app = express();
// Matches the port used by the game client's DailyServiceImpl.
const PORT = process.env.PORT || 28773;

// 中间件
app.use(cors());
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true }));

// 静态文件
app.use(express.static(path.join(__dirname, 'public')));

// API 路由
app.use('/MLPD/api/v1/daily', dailyRouter);

// 根路径重定向到排行榜页面
app.get('/', (req, res) => {
  res.redirect('/index.html');
});

// 等待数据库初始化完成后再启动服务
async function startServer() {
  try {
    await initDatabase();

    app.listen(PORT, () => {
      console.log(`========================================`);
      console.log(`  MLPD Daily Challenge Server`);
      console.log(`========================================`);
      console.log(`  服务已启动: http://localhost:${PORT}`);
      console.log(`  API 路径:   /MLPD/api/v1/daily`);
      console.log(`========================================`);
    });
  } catch (e) {
    console.error('启动失败:', e);
    process.exit(1);
  }
}

startServer();

module.exports = app;
