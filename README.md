# spring-lotto
## Status
Build: [![CircleCI](https://circleci.com/gh/szefoski/spring-lotto.svg?style=svg)](https://circleci.com/gh/szefoski/spring-lotto)

(Build and automatic releases upload to GitHub by https://circleci.com/gh/szefoski/spring-lotto)

## Summary
This application generates Lotto numbers (from 1 to 49) and do some other silly stuff.

## Usage
* http://localhost:8080/generate-lotto?id=6
* http://localhost:8080/generate-lotto?id=12311
* http://localhost:8080/generate/{number}
* http://localhost:8080/welcome/{name}
* curl --header "Content-Type: application/json" --request POST --data '[13,8,19,3,45,33]' http://localhost:8080/check-wins/
