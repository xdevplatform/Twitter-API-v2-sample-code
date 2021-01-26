library(httr)

# Replace the bearer token below with
bearer_token = ""

headers = c(
  `Authorization` = sprintf('Bearer %s', bearer_token)
)

params = list(
  `query` = 'from:twitterdev lang:en',
  `max_results` = '10',
  `tweet.fields` = 'created_at,lang,context_annotations'
)

response <- httr::GET(url = 'https://api.twitter.com/2/tweets/search/all', httr::add_headers(.headers=headers), query = params)

fas_body <-
  content(
    response,
    as = 'parsed',
    type = 'application/json',
    simplifyDataFrame = TRUE
  )

View(fas_body$data)