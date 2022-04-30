package controller

import (
	"fmt"
	"r4g_server/database"
	"r4g_server/model"
	"reflect"
	"strconv"
	"strings"

	"github.com/gin-gonic/gin"
	"gopkg.in/mgo.v2/bson"
	"net/http"
)

func Login(c *gin.Context) {
	entered_username := c.Query("username")
	entered_password := c.Query("password")
	var user model.UserInfo
	err := database.Session.DB("test").C("user").Find(bson.M{"username": entered_username}).One(&user)
	if err != nil {
		c.JSON(http.StatusOK, gin.H{"msg": "Username not exist!"})
	}
	if user.Username == entered_username && user.Password == entered_password {
		c.JSON(http.StatusOK, gin.H{"msg": "Login successfully!"})
	} else {
		c.JSON(http.StatusOK, gin.H{"msg": "Password incorrect!"})
	}
}

func Signup(c *gin.Context) {
	username := c.Query("username")
	password := c.Query("password")
	email := c.Query("email")
	var user model.UserInfo
	err := database.Session.DB("test").C("user").Find(bson.M{"username": username}).One(&user)
	if err != nil {
		fmt.Println("Not found")
		//panic(err)
	}
	if reflect.DeepEqual(user, model.UserInfo{}) {
		user = model.UserInfo{username, password, email}
		err = database.Session.DB("test").C("user").Insert(user)
		if err != nil {
			panic(err)
		} else {
			c.JSON(http.StatusOK, gin.H{"msg": "Signup successfully!", "data": user})
		}
	} else {
		c.JSON(http.StatusOK, gin.H{"msg": "Username already existed!"})
	}
}

func GetEstimatedScore(c *gin.Context) {
	user_id := c.Query("user_id")
	var score model.EstimatedScore
	err := database.Session.DB("test").C("estimated_score").Find(bson.M{"id": user_id}).One(&score)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, gin.H{"data": score})
	}
}

func GetAllQuantTest(c *gin.Context) {
	var tests []model.QuantTest
	err := database.Session.DB("test").C("quant_test").Find(bson.M{}).All(&tests)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, gin.H{"data": tests})
	}
}

func GetQuantTest(c *gin.Context) {
	test_id := c.Query("id")
	var test model.QuantTest
	err := database.Session.DB("test").C("quant_test").Find(bson.M{"id": test_id}).All(&test)
	if err != nil {
		c.AbortWithStatus(http.StatusNotFound)
	} else {
		c.JSON(http.StatusOK, gin.H{"data": test})
	}
}

func StaredOrNot(c *gin.Context) {
	user_id := c.Query("user_id")
	question_type := c.Query("question_type")
	question_id := c.Query("question_id")
	ok := database.RB.StarOrNot(user_id, question_type + ":" + question_id)
	c.JSON(http.StatusOK, gin.H{"data": ok})
}

func StarQuestion(c *gin.Context) {
	user_id := c.Query("user_id")
	question_type := c.Query("question_type")
	question_id := c.Query("question_id")
	err := database.RB.Star(user_id, question_type + ":" + question_id)
	if err != nil {
		panic(err)
	} else {
		c.JSON(http.StatusOK, gin.H{"data": user_id + ":" + question_type + ":" + question_id})
	}
}

func UnstarQuestion(c *gin.Context) {
	user_id := c.Query("user_id")
	question_type := c.Query("question_type")
	question_id := c.Query("question_id")
	err := database.RB.UnStar(user_id, question_type + ":" + question_id)
	if err != nil {
		panic(err)
	} else {
		c.JSON(http.StatusOK, gin.H{"data": user_id + ":" + question_type + ":" + question_id})
	}
}

func MyStar(c *gin.Context) {
	user_id := c.Query("user_id")
	my_star := database.RB.MyStar(user_id)
	c.JSON(http.StatusOK, gin.H{"data": my_star})
}

func SubmitTest(c *gin.Context) {
	user_id := c.Query("user_id")
	id := c.Query("id")
	selected := c.Query("selected")
	submitted := []string{}
	for _, v := range strings.Split(selected[1 : len(selected) - 1], ",") {
		submitted = append(submitted, v)
	}

	var test model.QuantTest
	err := database.Session.DB("test").C("quant_test").Find(bson.M{"id": id}).One(&test)
	if err != nil {
		panic(err)
	}
	score := 130
	//feedback := []bool{}
	//answer := []string{}
	for i := 0 ; i < 5 ; i++ {
		if strings.TrimSpace(test.Questions[i].Answer) == strings.TrimSpace(submitted[i]) {
			score += 8
		}
		//feedback = append(feedback, strings.TrimSpace(test.Questions[i].Answer) == strings.TrimSpace(submitted[i]))
		//answer = append(answer, strings.TrimSpace(test.Questions[i].Answer))
	}

	err = database.Session.DB("test").C("estimated_score").Update(bson.M{"id": user_id}, bson.M{"id": user_id, "quantscore": strconv.Itoa(score), "verbalscore": "170"})
	if err != nil {
		panic(err)
	}
	c.JSON(http.StatusOK, gin.H{})
}