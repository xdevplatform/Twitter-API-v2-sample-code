const fetch = require('node-fetch');
const OAuth = require('oauth-1.0a');
const crypto = require('crypto');

// user credentials
const consumerKey = process.env.CONSUMER_KEY;
const consumerSecret = process.env.CONSUMER_SECRET;
const accessToken = process.env.ACCESS_TOKEN;
const accessTokenSecret = process.env.ACCESS_TOKEN_SECRET;

// Generates auth header for user-context requests
function getUserContextAuth(method, url) {
  const oauth = new OAuth({
    consumer: {
      key: consumerKey,
      secret: consumerSecret,
    },
    signature_method: 'HMAC-SHA1',
    hash_function(base_string, key) {
      return crypto.createHmac('sha1', key).update(base_string).digest('base64');
    },
  });

  return oauth.toHeader(
    oauth.authorize(
      {
        url: url.toString(),
        method: method,
      },
      {
        key: accessToken,
        secret: accessTokenSecret,
      },
    ),
  ).Authorization;
}

// function for sending request to the endpoint
async function follow(sourceUserID, targetUserID) {
  const endpointURL = `https://api.twitter.com/2/users/${sourceUserID}/following`;
  const jsonBodyParams = {
    target_user_id: targetUserID
  }
  const body = JSON.stringify(jsonBodyParams);
  const headers = {
    Authorization: getUserContextAuth('post', endpointURL),
    'Content-Type': 'application/json'
  }

  return fetch(endpointURL, {
    method: 'post',
    headers,
    body
  }).then(res => res.json());
}

// make the request
follow('your-user-id', 'user-id-to-follow').then(res => console.log(res));