package main

import (
	"r4g_server/database"
	"r4g_server/router"
)

func main() {
	// init mongo DB
	database.Init()

	// setup routes
	r := router.SetupRouter()

	// running
	r.Run(":8080")
}