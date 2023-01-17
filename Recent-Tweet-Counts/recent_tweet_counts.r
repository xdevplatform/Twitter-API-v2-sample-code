library(httr)

# Replace the bearer token below with
bearer_token = ""

headers = c(
  `Authorization` = sprintf('Bearer %s', bearer_token)
)

params = list(
  `query` = 'from:twitterdev',
  `granularity` = 'day'
)

response <- httr::GET(url = 'https://api.twitter.com/2/tweets/counts/recent', httr::add_headers(.headers=headers), query = params)

body <-
  content(
    response,
    as = 'parsed',
    type = 'application/json',
    simplifyDataFrame = TRUE
  )

View(body$data)