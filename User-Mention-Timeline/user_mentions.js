// Get User mentions timeline by user ID
// https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/quick-start

const needle = require('needle');

const userId = 2244994945;
const url = `https://api.twitter.com/2/users/${userId}/mentions`;

// The code below sets the bearer token from your environment variables
// To set environment variables on macOS or Linux, run the export command below from the terminal:
// export BEARER_TOKEN='YOUR-TOKEN'
const bearerToken = process.env.BEARER_TOKEN;

// this is the ID for @TwitterDev
const getUserMentions = async () => {
    let userMentions = [];
    let params = {
        "max_results": 100,
        "tweet.fields": "created_at"
    }

    const options = {
        headers: {
            "User-Agent": "v2UserMentionssJS",
            "authorization": `Bearer ${bearerToken}`
        }
    }

    let hasNextPage = true;
    let nextToken = null;
    console.log("Retrieving mentions...");
    while (hasNextPage) {
        let resp = await getPage(params, options, nextToken);
        if (resp && resp.meta && resp.meta.result_count && resp.meta.result_count > 0) {
            if (resp.data) {
                userMentions.push.apply(userMentions, resp.data);
            }
            if (resp.meta.next_token) {
                nextToken = resp.meta.next_token;
            } else {
                hasNextPage = false;
            }
        } else {
            hasNextPage = false;
        }
    }

    console.dir(userMentions, {
        depth: null
    });

    console.log(`Got ${userMentions.length} mentions for user ID ${userId}!`);

}

const getPage = async (params, options, nextToken) => {
    if (nextToken) {
        params.pagination_token = nextToken;
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

getUserMentions();
