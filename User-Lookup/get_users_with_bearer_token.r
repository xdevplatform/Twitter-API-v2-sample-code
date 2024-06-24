require(httr)

bearer_token <- ""

headers <- c(`Authorization` = sprintf('Bearer %s', bearer_token))

params <- list(`user.fields` = 'description')

handle <- 'TwitterDev'

url_handle <-
  sprintf('https://api.twitter.com/2/users/by?usernames=%s', handle)

response <-
  httr::GET(url = url_handle,
            httr::add_headers(.headers = headers),
            query = params)

obj <- httr::content(res, as = "text")
print(obj)
