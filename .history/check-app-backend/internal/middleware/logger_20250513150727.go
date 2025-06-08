package middleware

import (
	"github.com/gin-gonic/gin"

// Logger 返回一个日志中间件
func Logger() gin.HandlerFunc {
	return gin.Logger()
}
