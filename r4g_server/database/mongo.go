package database

import (
	"fmt"
	"gopkg.in/mgo.v2"
)

var (
	// Session stores mongo session
	Session *mgo.Session

	// Mongo stores the mongodb connection string information
	Mongo *mgo.DialInfo
)

const uri = "localhost:27000"

func Init() {
	mongo, err := mgo.ParseURL(uri)
	s, err := mgo.Dial(uri)
	if err != nil {
		fmt.Printf("Can't connect to mongo, go error %v\n", err)
		panic(err)
	}
	s.SetSafe(&mgo.Safe{})
	fmt.Println("Connected to", uri)
	Session = s
	Mongo = mongo

	//Mongo.SetMode(mgo.Monotonic, true)

	//Mongo = session.DB("test").C("es_test")

	/*err = Mongo.DB("test").C("es_test").Insert(&model.EstimatedScore{Id:"2", QuantScore:"150", VerbalScore:"150", TotalScore:"123"})
	if err != nil {
		log.Fatal(err)
	}

	result := model.EstimatedScore{}
	err = Mongo.DB("test").C("es_test").Find(bson.M{"quantscore": "150"}).One(&result)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("TotalScore:", result.TotalScore)*/
}