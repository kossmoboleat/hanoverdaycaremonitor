# hanover daycare monitor

The daycare monitor checks the available daycares from the city's website and sends out an email every time a new daycare with available places appears.

There are many daycares offerend in Hanover and the city's website lists them all. Unfortunately there aren't enough available places at these daycares to go around. Parents have to manually check the available places and hope to catch the right moment when new places are available.

To simplify these regular checks and save some time in my already busy life as a new parent, I built this tool. First I had built the check using a script that uses the browser without user interaction and clicks on links and forms like a normal user would. The technology behind this is called selenium. This worked nicely but I found a more direct solution that can be accessed with a single API call.

This project is based on that second solution. I added the map overview because the location of a daycare is usually one of the highest priorities.

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run

The first check is performed at the next full hour. You can change that behaviour in checker_scheduler.clj

## Technology

The frontend is programmed in vanilla Javascript that transforms the server's JSON response in markers on the map. For the map I used the very user-friendly Leaflet library.

Currently I've configured it to use the map tiles from the company Mapbox, but you should either use your API access token for Mapbox or use a different provider such as openstreetmap, which already serves as the data basis for Mapbox's tiles.

The backend is programmed in Clojure with the Luminus framework. When updating Luminus one probably has to regenerate the whole project based on their new template to get all the changes. The version used to generate this project is "2.9.11.16".

## License

MIT license
