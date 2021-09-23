package Tweet_Lookup

// Get Tweets by User ID. Using Bearer Token
// https://developer.twitter.com/en/docs/twitter-api/tweets/lookup/introduction

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
)

type UserTweets struct {
	Data     []Data   `json:"data"`
	Includes Includes `json:"includes"`
}
type Data struct {
	Lang     string `json:"lang"`
	AuthorID int64  `json:"author_id"`
	ID       int64  `json:"id"`
	Text     string `json:"text"`
}
type Users struct {
	ID       int64  `json:"id"`
	Name     string `json:"name"`
	Username string `json:"username"`
}
type Includes struct {
	Users []Users `json:"users"`
}

func TweetLookup() {
	if os.Getenv("Bearer") == "" {
		log.Fatalln("set a bereaer token")
		return
	}

	// Sets the bearer token from your environment variables
	// To set environment variables on macOS or Linux, run the export command below from the terminal:
	// export BEARER_TOKEN='YOUR-TOKEN'
	bearerToken := os.Getenv("BEARER_TOKEN")
	const url = "https://api.twitter.com/2/tweets"

	client := &http.Client{}
	request, createReqErr := http.NewRequest("GET", url, nil)
	if createReqErr != nil {
		log.Println(createReqErr)
		return
	}

	// Add Parameters
	q := request.URL.Query()
	q.Add("ids", "1228393702244134912,1227640996038684673,1199786642791452673")
	q.Add("tweet.fields", "lang,author_id")
	q.Add("expansions", "author_id")

	request.URL.RawQuery = q.Encode()

	// Add Bearer Token to HTTP Header
	request.Header.Set("Authorization", "Bearer "+bearerToken)

	// Send Request
	response, responseErr := client.Do(request)
	if responseErr != nil {
		log.Println(responseErr)
	}

	defer response.Body.Close()

	var userTweets UserTweets
	json.NewDecoder(response.Body).Decode(&userTweets)

	fmt.Println(userTweets)
}
