package middleware

import (
	"github.com/gin-gonic/gin"
)

// Recovery 返回一个恢复中间件
func Recovery() gin.HandlerFunc {
	return gin.Recovery()
}
