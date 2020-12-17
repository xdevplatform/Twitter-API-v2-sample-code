const needle = require('needle');

const userId = 2244994945;
const url = `https://api.twitter.com/2/users/${userId}/mentions`;
const bearerToken = process.env.BEARER_TOKEN;

const getUserMentions = async () => {
    let userMentions = [];
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
    console.log("Retrieving mentions...");
    while (hasNextPage) {
        let resp = await getPage(params, options, nextToken);
        if (resp && resp.meta && resp.meta.result_count && resp.meta.result_count > 0) {
            if (resp.data) {
                userMentions.push.apply(userMentions, resp.data);
            }
            if (resp.meta.next_token) {
                nextToken = resp.meta.next_token;
            }
        } else {
            hasNextPage = false;
        }
    }

    console.log(userMentions);
    console.log(`Got ${userMentions.length} mentions for ${username}!`);

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

getUserMentions();