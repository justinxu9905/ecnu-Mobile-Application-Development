package model

type UserInfo struct {
	Username string	`json:"username"`
	Password string	`json:"password"`
	Email string	`json:"email"`
}

type EstimatedScore struct {
	Id string 		`json:"id"`
	QuantScore string 	`json:"quantscore"`
	VerbalScore string 	`json:"verbalscore"`
}

type QuantTest struct {
	Id string			`json:"id"`
	Name string		`json:"name"`
	Questions []QuantQuestion		`json:"questions"`
}

type QuantQuestion struct {
	Id string 			`json:"id"`
	Question string 	`json:"question"`
	Options []string 	`json:"options"`
	Answer string		`json:"answer"`
}