const { Client, auth } = require("twitter-api-sdk");

const readline = require("readline").createInterface({
  input: process.stdin,
  output: process.stdout,
});

//Helper function to parse callback
const getQueryStringParams = (query) => {
  return query
    ? (/^[?#]/.test(query) ? query.slice(1) : query)
        .split(/[\?\&]/)
        .reduce((params, param) => {
          let [key, value] = param.split("=");
          params[key] = value
            ? decodeURIComponent(value.replace(/\+/g, " "))
            : "";
          return params;
        }, {})
    : {};
};

//Helper terminal input function
async function input(prompt) {
  return new Promise(async (resolve, reject) => {
    readline.question(prompt, (out) => {
      readline.close();
      resolve(out);
    });
  });
}

// The code below sets the consumer key and consumer secret from your environment variables
// To set environment variables on macOS or Linux, run the export commands below from the terminal:
// export CLIENT_ID='YOUR-CLIENT-ID'
// export CLIENET_SECRET='YOUR-CLIENT-SECRET'
const CLIENT_ID = process.env.CLIENT_ID;
const CLIENT_SECRET = process.env.CLIENT_SECRET;

// Optional parameters for additional payload data
const params = {
  expansions: "author_id",
  "user.fields": ["username", "created_at"],
  "tweet.fields": ["geo", "entities", "context_annotations"],
};

(async () => {
  const authClient = new auth.OAuth2User({
    client_id: CLIENT_ID,
    client_secret: CLIENT_SECRET,
    callback: "https://www.example.com/oauth",
    scopes: ["tweet.read", "users.read"],
  });

  const client = new Client(authClient);
  const STATE = "my-state";

  //Get authorization
  const authUrl = authClient.generateAuthURL({
    state: STATE,
    code_challenge: "challenge",
  });

  console.log(`Please go here and authorize:`, authUrl);

  //Input users callback url in termnial
  const redirectCallback = await input("Paste the redirected callback here: ");

  try {
    //Parse callback
    const { state, code } = getQueryStringParams(redirectCallback);
    if (state !== STATE) {
      console.log("State isn't matching");
    }
    //Gets access token
    await authClient.requestAccessToken(code);

    //Get the user ID
    const {
      data: { id },
    } = await client.users.findMyUser();

    //Makes api call
    const getUsersTimeline = await client.tweets.usersIdTimeline(id, params);
    console.dir(getUsersTimeline, {
      depth: null,
    });
    process.exit();
  } catch (error) {
    console.log(error);
  }
})();
