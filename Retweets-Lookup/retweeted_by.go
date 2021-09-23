package Retweets_Lookup

// Search foor retweeted by Tweet ID. Using Bearer Token
// https://developer.twitter.com/en/docs/twitter-api/tweets/retweets/introduction

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"time"
)

type RetweetedBy struct {
	Data     []Data   `json:"data"`
	Includes Includes `json:"includes"`
	Meta     Meta     `json:"meta"`
}
type Data struct {
	ID            string    `json:"id"`
	Username      string    `json:"username"`
	PinnedTweetID string    `json:"pinned_tweet_id,omitempty"`
	CreatedAt     time.Time `json:"created_at"`
}
type Includes struct {
	Tweets []Tweets `json:"tweets"`
}

type Tweets struct {
	ID        string    `json:"id"`
	Text      string    `json:"text"`
	CreatedAt time.Time `json:"created_at"`
}

type Meta struct {
	ResultCount int `json:"result_count"`
}

func GetRetweetedBy() {
	if os.Getenv("Bearer") == "" {
		log.Fatalln("set a bearer token")
	}

	const tweetID = 1354143047324299264
	url := fmt.Sprintf("https://api.twitter.com/2/tweets/%d/retweeted_by", tweetID)

	// Sets the bearer token from your environment variables
	// To set environment variables on macOS or Linux, run the export command below from the terminal:
	// export BEARER_TOKEN='YOUR-TOKEN'
	bearerToken := os.Getenv("BEARER_TOKEN")

	client := &http.Client{}
	request, errCreateReq := http.NewRequest("GET", url, nil)
	if errCreateReq != nil {
		log.Println(errCreateReq)
		return
	}

	// Sets the bearer token from your environment variables
	// To set environment variables on macOS or Linux, run the export command below from the terminal:
	// export BEARER_TOKEN='YOUR-TOKEN'
	request.Header.Set("Authorization", "Bearer "+bearerToken)

	// Add Parameters
	q := request.URL.Query()
	q.Add("user.fields", "created_at")
	q.Add("expansions", "pinned_tweet_id")
	q.Add("tweet.fields", "created_at")
	request.URL.RawQuery = q.Encode()

	// Send Request
	response, requestErr := client.Do(request)
	if requestErr != nil {
		log.Println(requestErr)
		return
	}

	var retweetedBy RetweetedBy
	jsonDecodeErr := json.NewDecoder(response.Body).Decode(&retweetedBy)
	if jsonDecodeErr != nil {
		log.Println(jsonDecodeErr)
		return
	}
	fmt.Println(retweetedBy)
}
