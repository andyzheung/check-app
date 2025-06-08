class FlappyBird {
    constructor() {
        this.canvas = document.getElementById('gameCanvas');
        this.ctx = this.canvas.getContext('2d');
        this.width = this.canvas.width;
        this.height = this.canvas.height;
        
        // 游戏状态
        this.gameStarted = false;
        this.gameOver = false;
        this.score = 0;
        
        // 小鸟属性
        this.bird = {
            x: 50,
            y: this.height / 2,
            radius: 15,
            velocity: 0,
            gravity: 0.5,
            jump: -8
        };
        
        // 管道属性
        this.pipes = [];
        this.pipeWidth = 50;
        this.pipeGap = 150;
        this.pipeSpacing = 200;
        this.lastPipeTime = 0;
        
        // 绑定事件处理器
        this.bindEvents();
        
        // 初始化游戏
        this.reset();
        this.draw();
    }
    
    bindEvents() {
        // 空格键和点击事件处理
        document.addEventListener('keydown', (e) => {
            if (e.code === 'Space') {
                this.handleInput();
            }
        });
        
        this.canvas.addEventListener('click', () => {
            this.handleInput();
        });
        
        document.getElementById('startBtn').addEventListener('click', () => {
            if (this.gameOver) {
                this.reset();
            }
            this.gameStarted = true;
            document.getElementById('startBtn').style.display = 'none';
        });
    }
    
    handleInput() {
        if (!this.gameStarted) {
            this.gameStarted = true;
            document.getElementById('startBtn').style.display = 'none';
        }
        if (!this.gameOver) {
            this.bird.velocity = this.bird.jump;
        }
    }
    
    reset() {
        this.gameOver = false;
        this.score = 0;
        this.pipes = [];
        this.bird.y = this.height / 2;
        this.bird.velocity = 0;
        document.getElementById('score').textContent = this.score;
        document.getElementById('startBtn').style.display = 'block';
        document.getElementById('startBtn').textContent = '开始游戏';
    }
    
    update() {
        if (!this.gameStarted || this.gameOver) return;
        
        // 更新小鸟位置
        this.bird.velocity += this.bird.gravity;
        this.bird.y += this.bird.velocity;
        
        // 生成新管道
        if (Date.now() - this.lastPipeTime > 1500) {
            const minHeight = 50;
            const maxHeight = this.height - this.pipeGap - minHeight;
            const height = Math.random() * (maxHeight - minHeight) + minHeight;
            
            this.pipes.push({
                x: this.width,
                height: height,
                passed: false
            });
            
            this.lastPipeTime = Date.now();
        }
        
        // 更新管道位置
        this.pipes.forEach(pipe => {
            pipe.x -= 2;
            
            // 检查得分
            if (!pipe.passed && pipe.x + this.pipeWidth < this.bird.x) {
                pipe.passed = true;
                this.score++;
                document.getElementById('score').textContent = this.score;
            }
            
            // 碰撞检测
            if (this.checkCollision(pipe)) {
                this.gameOver = true;
                document.getElementById('startBtn').style.display = 'block';
                document.getElementById('startBtn').textContent = '重新开始';
            }
        });
        
        // 移除超出屏幕的管道
        this.pipes = this.pipes.filter(pipe => pipe.x + this.pipeWidth > 0);
        
        // 检查是否撞到地面或天花板
        if (this.bird.y + this.bird.radius > this.height || this.bird.y - this.bird.radius < 0) {
            this.gameOver = true;
            document.getElementById('startBtn').style.display = 'block';
            document.getElementById('startBtn').textContent = '重新开始';
        }
    }
    
    checkCollision(pipe) {
        const birdRight = this.bird.x + this.bird.radius;
        const birdLeft = this.bird.x - this.bird.radius;
        const birdTop = this.bird.y - this.bird.radius;
        const birdBottom = this.bird.y + this.bird.radius;
        
        return (
            birdRight > pipe.x &&
            birdLeft < pipe.x + this.pipeWidth &&
            (birdTop < pipe.height || birdBottom > pipe.height + this.pipeGap)
        );
    }
    
    draw() {
        // 清空画布
        this.ctx.clearRect(0, 0, this.width, this.height);
        
        // 绘制背景
        this.ctx.fillStyle = '#4FC3F7';
        this.ctx.fillRect(0, 0, this.width, this.height);
        
        // 绘制小鸟
        this.ctx.beginPath();
        this.ctx.arc(this.bird.x, this.bird.y, this.bird.radius, 0, Math.PI * 2);
        this.ctx.fillStyle = '#FFEB3B';
        this.ctx.fill();
        this.ctx.closePath();
        
        // 绘制管道
        this.pipes.forEach(pipe => {
            // 上管道
            this.ctx.fillStyle = '#4CAF50';
            this.ctx.fillRect(pipe.x, 0, this.pipeWidth, pipe.height);
            
            // 下管道
            this.ctx.fillRect(
                pipe.x,
                pipe.height + this.pipeGap,
                this.pipeWidth,
                this.height - (pipe.height + this.pipeGap)
            );
        });
        
        // 游戏循环
        this.update();
        requestAnimationFrame(() => this.draw());
    }
}

// 初始化游戏
window.onload = () => {
    new FlappyBird();
}; 