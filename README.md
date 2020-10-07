## LangStats - a Kotlin-based app that uses the Github Jobs API to show backend programming language trends by city
### Challenges I ran into
I wanted to use the IntelliJ build system, but after a fruitless struggle to config I had to resort to the tried and true Gradle build system.
### Areas of the code you are most proud of
None of the areas of the code invoke pride more than the others.
### Areas of the code you are least proud of
If I had more time, I'd refactor the API hitting functionality to be more easily stubbed for testing.
### Tradeoffs you made and why
I have all of the functionality in a monolithic class bc I was just trying to get it to work up to spec quickly.
### Next areas of focus to move this towards production
Separate the functionality, e.g. retrieving, crunching & rendering the data, into different classes.