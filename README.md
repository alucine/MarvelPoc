# MarvelPoc
Android Tech Test- Wallapop

Here at Wallapop, we love arguing who is the best comic super-hero. Too bad we don’t have a quick way to compare super-heroes in our  .

That’s why we would like you to:

-  Create a master-detail Android application that lists your favorite character’s comics:
- The master scene will display all the comics for the character, where each cell will display the comic’s title and thumbnail image.
- The detail screen will display a random image for the selected comic, along with the title and the description of the comic (or any other relevant information you want).
-  Use the Marvel Public API http://developer.marvel.com/docs in order to fetch your character’s comics.
-  Store your project in your public Github account.

Take special care when developing your app to these key points:

-  Project architecture
-  Design Patterns
-  Best practices
Bonus Points:

-  MultiThreading
-  Unit Tests
-  Instrumentational tests
-  Animations
-  Scrolling performance

Considerations:

-  You are given complete freedom over what UX to implement, what pretty colors to put in the navigationBar... the design details are up to you.
-  You can use 3rd party libraries

Good luck and have fun! The Wallapop Android Team.

API info

Authorization: all requests must pass 3 parameters using URL encoding

-  “apikey": the public key
-  “ts": a timestamp (or other long string which can change on a request-by-request basis)
-  “hash": a md5 digest of the ts parameter, your private key and your public key (e.g.

md5(ts+privateKey+publicKey))

Keys: You can use our keys:

-  public-key: “6a7ed890b4b941a925202a5630d5b162”
-  private-key: ”0f1d0fdf46a0bf32f962b0b9997233c0395cdf8e”

More info here: http://developer.marvel.com/documentation/authorization
CharacterID: You’ll need a character’s ID in order to get it’s comics. You can use Capitan

America’s ID (1009220) since he’s the coolest super-hero ever # .

The base path for all requests is http://gateway.marvel.com, here’s the documentation for this particular request: http://developer.marvel.com/docs#!/public/ getComicsCharacterCollection_get_2
