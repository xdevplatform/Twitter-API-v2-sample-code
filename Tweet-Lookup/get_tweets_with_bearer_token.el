;;; get_tweets_with_bearer_token.el --- Description -*- lexical-binding: t; -*-
;;
;; Copyright (C) 2022 gicrisf
;;
;; Author: gicrisf <giovanni.crisalfi@protonmail.com>
;; Maintainer: gicrisf <giovanni.crisalfi@protonmail.com>
;; Created: march 17, 2022
;; Modified: march 17, 2022
;; Version: 1.0.0
;; Keywords: data
;; Homepage: https://github.com/twitterdev/Twitter-API-v2-sample-code
;; Package-Requires: ((emacs "24.3"))
;;
;; This file is not part of GNU Emacs.
;;
;;; Commentary:
;;
;;  Description
;;  Sample code for the Twitter API v2 endpoints
;;
;;; Code:

(require 'request)
;; https://github.com/tkf/emacs-request

;; Retrieve your Environment Variable with the bearer token
;; Alternately, you could just put the token here as the other vars
;; (setq bearer-token ...)
(defvar bearer-token (getenv "BEARER_TOKEN"))

;; Tweet fields are adjustable.
;; Options include:
;; attachments, author_id, context_annotations,
;; conversation_id, created_at, entities, geo, id,
;; in_reply_to_user_id, lang, non_public_metrics, organic_metrics,
;; possibly_sensitive, promoted_metrics, public_metrics, referenced_tweets,
;; source, text, and withheld
(setq tweet_fields "tweet.fields=lang,author_id")

;; You can adjust ids to include a single Tweet.
;; Or you can add to up to 100 comma-separated IDs
(setq ids "ids=1278747501642657792,1255542774432063488")

;; Let's build up the URL
(setq url (concat "https://api.twitter.com/2/tweets?" ids "&" tweet_fields))

;; Headers for the request
(setq hdrs '())
;; We need just this field for authorization
(setq auth_header (concat "Bearer " bearer_token))
(add-to-list 'hdrs (cons "Authorization" (concat "Bearer " bearer_token)))

;; Core function with the actual GET request
(request url
  :type "GET"
  :headers hdrs
  :parser 'json-read
  :error
  (cl-function (lambda (&rest args &key error-thrown &allow-other-keys)
                 (message "Got error: %S" error-thrown)))
  :success (cl-function
            (lambda (&key data &allow-other-keys)
              (message "Got: %s" data))))

(provide 'get_tweets_with_bearer_token)
;;; get_tweets_with_bearer_token.el ends here
