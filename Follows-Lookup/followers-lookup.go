package Follows_Lookup

// Fetch the followers of a user account, by ID. Using Bearer Token
// https://developer.twitter.com/en/docs/twitter-api/users/follows/quick-start
import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"time"
)

type UserFollower struct {
	Data     []Data   `json:"data"`
	Meta     Meta     `json:"meta"`
	Includes Includes `json:"includes"`
}

type Data struct {
	ID            int64  `json:"id"`
	Username      string `json:"username"`
	Name          string `json:"name"`
	PinnedTweetID int64  `json:"pinned_tweet_id"`
	CreatedAt     string `json:"created_at"`
}
type Includes struct {
	Tweets []Tweets `json:"tweets"`
}
type Tweets struct {
	CreatedAt time.Time `json:"created_at"`
	ID        int64     `json:"id"`
	Text      string    `json:"text"`
}

type Meta struct {
	ResultCount string `json:"result_count"`
	NextToken   string `json:"next_token"`
}

func UserFollowers() {
	if os.Getenv("BEARER_TOKEN") == "" {
		log.Fatalln("set a bearer token")
		return
	}

	// Sets the bearer token from your environment variables
	// To set environment variables on macOS or Linux, run the export command below from the terminal:
	// export BEARER_TOKEN='YOUR-TOKEN'
	bearerToken := os.Getenv("BEARER_TOKEN")
	const userID = 2244994945
	url := fmt.Sprintf("https://api.twitter.com/2/users/%d/followers", userID)

	client := &http.Client{}

	request, err := http.NewRequest("GET", url, nil)
	if err != nil {
		log.Println(err)
		return
	}

	// Add Bearer Token to HTTP Header
	request.Header.Set("Authorization", "Bearer "+bearerToken)

	// Add parameters
	q := request.URL.Query()
	q.Add("user.fields", "created_at")
	q.Add("tweet.fields", "created_at")
	q.Add("expansions", "pinned_tweet_id")
	q.Add("max_results", "5")
	request.URL.RawQuery = q.Encode()

	// Send Request
	response, err := client.Do(request)

	if err != nil {
		log.Println(err)
	}

	defer response.Body.Close()

	var userObject UserFollower
	err = json.NewDecoder(response.Body).Decode(&userObject)
	if err != nil {
		log.Println("error to decode json", err)
	}

	fmt.Println(userObject)
}
