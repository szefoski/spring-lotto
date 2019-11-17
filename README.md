# spring-lotto
## Status
Last build: [![CircleCI](https://circleci.com/gh/szefoski/spring-lotto.svg?style=svg)](https://circleci.com/gh/szefoski/spring-lotto)

(Build and automatic releases upload to GitHub by https://circleci.com/gh/szefoski/spring-lotto)

## Summary
This application generates Lotto numbers (from 1 to 49) and do some other silly stuff.

## Usage
#### Generate Lotto numbers where, {number} is a integer parameter how many will be generated
* http://localhost:8080/generate-lotto?number={number}
* http://localhost:8080/generate/{number}

#### Some silly generator, {name} is a String, put your name
* http://localhost:8080/welcome/{name}

#### It download Duzy Lotek archived games and provides in JSON format
* http://localhost:8080/games-archive/

#### Checks if given numbers won in the past and return all matches
```bash
curl --header "Content-Type: application/json" --request POST --data '[2,3,4,11,17,18]' http://localhost:8080/check-wins/
```