package router

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"r4g_server/controller"
)

func SetupRouter() *gin.Engine {
	r := gin.Default()

	r.POST("Login", controller.Login)
	r.POST("Signup", controller.Signup)
	r.GET("EstimatedScore", controller.GetEstimatedScore)
	r.GET("AllQuantTest", controller.GetAllQuantTest)
	r.GET("QuantTest", controller.GetQuantTest)
	r.POST("StaredOrNot", controller.StaredOrNot)
	r.POST("StarQuestion", controller.StarQuestion)
	r.POST("UnstarQuestion", controller.UnstarQuestion)
	r.GET("MyStar", controller.MyStar)
	r.POST("SubmitTest", controller.SubmitTest)

	r.NoRoute(func(c *gin.Context) {
		c.String(http.StatusNotFound, "Not Found")
	})

	return r
}