# Twitter API v2 sample code [![v2](https://img.shields.io/endpoint?url=https%3A%2F%2Ftwbadges.glitch.me%2Fbadges%2Fv2)](https://developer.twitter.com/en/docs/twitter-api)

Sample code for early access of the new v2 endpoints (Python, Java, Ruby, and Node.js). Each available endpoint has a folder where you can find sample code.

## Getting Started

* Website: https://developer.twitter.com/en/docs/twitter-api/early-access
* Getting started: https://developer.twitter.com/en/docs/twitter-api/getting-started

## Using the code samples

In order to run the code samples in this repository you will need to create environment variables. You can find your credentials which now includes your bearer token in the App inside of your Project in the [dashboard of the developer portal](https://developer.twitter.com/en/portal/projects-and-apps).

For OAuth 1.0a samples, you will need to export your consumer key and secret in your terminal. Be sure to replace `<your_consumer_key>` and `<your_consumer_secret>` with your own credentials without the `< >`.

```bash
export 'CONSUMER_KEY'='<your_consumer_key>'
export 'CONSUMER_SECRET'='<your_consumer_secret>'
```

For samples which use bearer token authentication, you will need to export your bearer token. Be sure to replace  `<your_bearer_token>` with your own bearer token without the `< >`.

```bash
export 'BEARER_TOKEN'='<your_bearer_token>'
```

## Python environment set up
You will need to have Python 3 installed to run this code. All Python samples except `Tweet-Lookup/get_tweets_with_user_context.py ` and `User-Lookup/get_users_with_user_context.py` use `requests==2.24.0` which uses `requests-oauthlib==1.3.0`.

You can pip install these packages as follows:

```bash
pip install requests
pip install requests-oauthlib
```

## Ruby environment set up
You will need to have Ruby (recommended: >= 2.0.0) installed in order to run the code. The Ruby examples use `typheous` as the HTTP client, which needs to be gem installed. For the Tweet and User lookup with user context requests, you'll need to install the `oauth` gem (see below).

```bash
gem install typheous
gem install oauth
```

## JavaScript (Node.js) environment set up
You will need to have Node.js installed to run this code. All Node.js examples use `needle` as the HTTP client, which needs to be npm installed. For the Tweet and User lookup with user context requests, you'll need to install the `got` and `oauth-1.0a` packages.

```bash
npm install needle
npm install got
npm install oauth-1.0a
```

## Java environment set up
If you use Homebrew, you can install a Java runtime using:

```bash
brew cask install java
```

## Prerequisites

* Twitter Developer account: if you don’t have one already, [you can apply](https://developer.twitter.com/en/apply-for-access) for one.
* A Project and an App created [in the dashboard](https://developer.twitter.com/en/portal/dashboard).

## Support

* For general questions related to the API and features, please use the v2 section of our [community forums](https://twittercommunity.com/c/twitter-api/twitter-api-v2/65). 

* If there's an bug or issue with the sample code itself, please create a [new issue](https://github.com/twitterdev/Twitter-API-v2-sample-code/issues) here on GitHub.

* If you have questions about the future, check out these resources:
    * [Guide to the Future of the Twitter API](https://developer.twitter.com/en/products/twitter-api/early-access/guide)
    * [Twitter API Roadmap](https://t.co/roadmap)
    * [Twitter Developer Feedback](https://twitterdevfeedback.uservoice.com/forums/930250-twitter-api), where you can post and vote on ideas and feature requests

## Contributing

We welcome pull requests that add meaningful additions to these code samples, particularly for languages that are not yet represented here.

We feel that a welcoming community is important and we ask that you follow Twitter's
[Open Source Code of Conduct](https://github.com/twitter/code-of-conduct/blob/master/code-of-conduct.md)
in all interactions with the community.

## License

Copyright 2020 Twitter, Inc.

Licensed under the Apache License, Version 2.0: https://www.apache.org/licenses/LICENSE-2.0
