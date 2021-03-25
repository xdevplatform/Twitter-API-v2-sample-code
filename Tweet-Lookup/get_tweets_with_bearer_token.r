require(httr)

bearer_token <- ""

headers <- c(`Authorization` = sprintf('Bearer %s', bearer_token))

params <- list(`tweet.fields` = 'created_at')

ids <- '1293593516040269825'

url_ids <-
  sprintf('https://api.twitter.com/2/tweets?ids=%s', ids)

response <-
  httr::GET(url = url_ids,
            httr::add_headers(.headers = headers),
            query = params)


obj <- httr::content(response, as = "text")
print(obj)
