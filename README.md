## Overview

This is the official errata for the book [*Mastering OAuth 2.0*](https://www.packtpub.com/application-development/mastering-oauth-2).

[<img src="https://www.packtpub.com/sites/default/files/5407OS_3653_Mastering%20Oauth%202.0.jpg" width="200" />](https://www.packtpub.com/application-development/mastering-oauth-2)


**Available at:**
* [Amazon.com](http://www.amazon.com/gp/product/B013T7MQNE/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=B013T7MQNE&linkCode=as2&tag=charleon-20&linkId=CBTUPI5NOKI7Y6VD)
* [PacktPub.com](https://www.packtpub.com/application-development/mastering-oauth-2)

## Errata

The following is the list of errors and corrections for the book as identified by the readers:

| Location  | Description | Error and Correction | Reported By |
| ------------- | ------------- | ------------- | ------------- |
| <ul><li>Chapter 5, Get an Access Token with the Client-Side Flow &rarr; A closer look at the implicit grant flow &rarr; Authorization request &rarr; In our application</li><li>Located on page 59 in print and PDF versions, and page 164 in ePub version</li></ul> | The URL-encoded redirect URI specified is missing a forward slash  | Redirect URI value is `http%3A%2F%wmiig.com%2Fcallback.html` but should be `http%3A%2F%2F%wmiig.com%2Fcallback.html` | John Ray Thomas |
| <ul><li>Chapter 11, Tooling and Troubleshooting &rarr; Troubleshooting &rarr; The API call flow &rarr; The authorization request header field</li><li>Located on page 410 in ePub version.  This error is not in the print or PDF versions.</li></ul>  | The `curl` command is missing a space between the second quotation mark and the URL  | The `curl` command is `curl -H "Authorization: Bearer CAAEvZCNK2AWsBZDZD"https://graph.facebook.com/v2.5/me?fields=name` but should be `curl -H "Authorization: Bearer CAAEvZCNK2AWsBZDZD" https://graph.facebook.com/v2.5/me?fields=name` | John Ray Thomas |
