PlayWithKaazing
===========

Just a collection of little samples/tests/demo's I've created with Kaazing.  

### WebTerminal

A two hour attempt to get a virtual terminal connection in the browser.  It runs Java ProcessBuilder commands from the browser via WebSocket and Jquery-Terminal.  There is a little custom parsing to handle "cd".  Kaazing is used as a WebSocket to Tcp proxy.  The ProcessBuilder exec loop is a tcp ServerSocket. 

It quick and responsive and works for simple things, but after doing a quick I've come to the opinion that a full featured implementation would require running ssh over websocket.  Things like "vi" don't work with just running single commands. I haven't yet found anything like this but someone somewhere probably has an awesome one.  If you know of any projects doing this please submit a pull request to this ReadMe and I'll add a link to it.

##### To build

from WebTerminal directory run
mvn clean install  (I used Java 7 and mvn 3.0.5)

##### To Test

from WebTerminal directory run
java -jar target/webterminal-1.0.0.0-SNAPSHOT.jar

Open the browser to the link displayed on console


