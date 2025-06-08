# 像素鸟游戏

一个使用 HTML5 Canvas 和 JavaScript 实现的简单像素鸟游戏。

## 如何运行

1. 直接在浏览器中打开 `index.html` 文件
2. 或者使用本地服务器运行（推荐）：
   - 使用 Python: `python -m http.server 8000`
   - 使用 Node.js: `npx http-server`
   然后访问 `http://localhost:8000`

## 游戏玩法

- 点击"开始游戏"按钮开始
- 使用空格键或鼠标点击来控制小鸟跳跃
- 避开绿色管道
- 每通过一个管道获得1分
- 撞到管道或超出边界游戏结束

## 游戏特性

- 流畅的游戏动画
- 实时分数显示
- 碰撞检测
- 随机生成的管道
- 响应式控制

## 技术栈

- HTML5 Canvas
- 原生 JavaScript
- CSS3 