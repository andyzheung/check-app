package middleware

import (
// Logger 返回一个日志中间件
func Logger(
func Logger(
func Logger(

	"github.com/gin-gonic/gin"

func Logger() gin.HandlerFunc {
	return gin.Logger()
}
