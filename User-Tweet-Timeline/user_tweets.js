const needle = require('needle');

const userId = 2244994945;
const url = `https://api.twitter.com/2/users/${userId}/tweets`;
const bearerToken = process.env.BEARER_TOKEN;

const getUserTweets = async () => {
    let userTweets = [];
    let params = {
        "max_results": 100,
        "tweet.fields": "created_at"
    }

    const options = {
        headers: {
            "authorization": `Bearer ${bearerToken}`
        }
    }

    let hasNextPage = true;
    let nextToken = null;
    console.log("Retrieving Tweets...");
    while (hasNextPage) {
        let resp = await getPage(params, options, nextToken);
        if (resp && resp.meta && resp.meta.result_count && resp.meta.result_count > 0) {
            if (resp.data) {
                userTweets.push.apply(userTweets, resp.data);
            }
            if (resp.meta.next_token) {
                nextToken = resp.meta.next_token;
            }
        } else {
            hasNextPage = false;
        }
    }

    console.log(userTweets);
    console.log(`Got ${userTweets.length} Tweets from ${username}!`);

}

const getPage = async (params, options, nextToken) => {
    if (nextToken) {
        params.next_token = nextToken;
    }

    try {
        const resp = await needle('get', url, params, options);

        if (resp.statusCode != 200) {
            console.log(`${resp.statusCode} ${resp.statusMessage}:\n${resp.body}`);
            return;
        }
        return resp.body;
    } catch (err) {
        throw new Error(`Request failed: ${err}`);
    }
}

getUserTweets();