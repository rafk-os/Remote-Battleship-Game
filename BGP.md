# Battleships Game Protocol (BGP)

## Introduction

The protocol is based on tcp. BGP was developed to support professional real-time battleships game in console.

## Documentation

Protocol works similar to SMTP/ESMTP protocol - defines the commands to be used on the client and server side. Protocol is adapted to work in Client-Server architecture. \
After connecting to the server, server will send information with name and version protocol. Example: 

``` HELLO You have been connected to a server using BGP version 1.0 ```

# Planning phase

In this phase all commands are used to prepare game.

## List of avaiable commands and server informations

If client will use not recognizable by server command, the response message will be send as follows: ``` BAD_SYNTAX ``` 

### LIST

The purpose of this command is to send a request to the server for information about currently available game rooms. If the server finds an empty space, it will send it in single lines with a dot (```.```) at the end. If there are no available room, server will send ``` NO_FREE_ROOM ``` information.

Request:

``` LIST ``` 

Response example (success):

```
Avaiable rooms:
1
3
8
.
```
Response example (fail):

```NO_FREE_ROOM```

### SIT <room_number>

When client wants to join the room, he has to send command ```SIT <room_number>``` where room_number is a number that determines room id. Server in response might send 4 possible messages: \
``` BAD_SYNTAX ``` - client used bad syntax \
``` WRONG_ROOM ``` - client wanted to join to the room that does not exist \
``` NO_AVAIABLE_SLOTS ``` - client wanted to join to the room that does not have free player slot \
``` JOINING_SUCCESSFUL ``` - client have joined to the room 

Example request:

```SIT 6```

Example response (success):

``` JOINING_SUCCESSFUL ```

Example response (fail):

``` WRONG_ROOM ```

If client has successfully joined  room server and recived message: ``` JOINING_SUCCESSFUL ``` server will send information about lobby status. There are two possible messages: ``` WAITING_FOR_SECOND_PLAYER ``` and ``` GAME_IS_STARTING ```. In case if client sees the first message, he will see the second one after second player has joined the room. 

Example flow:

```
SIT 6
JOINING_SUCCESSFUL
WAITING_FOR_SECOND_PLAYER
GAME_IS_STARTING
```

# Game phase

### SETTINGS

If the player has found a free lobby and received a ``` GAME_IS_STARTING ``` response, he has to send 10x10 map that contains ships ( ```x``` symbol) and free space ( ```o``` symbol) as 10 messeges while each message contains 10 chars ( ``` x ``` and ``` o ``` ). Send map must include 1 five-field ship, 1 four-field ship, 2 three-field ships and 2 twofield ships.  After sending 10th message server will send response as follows:

``` MAP_SEND_SUCCESSFULY ``` - when client didn't made any mistake

Example client message:

```
xxxxoooooo
oooooxxoxx
oooooooooo
xoxoxxxxxo
xoxooooooo
xoxooooooo
oooooooooo
oooooooooo
oooooooooo
oooooooooo
```

Example server response:

``` MAP_SEND_SUCCESSFULLY ```

### YOUR_TURN / YOUR_OPPONENT_TURN messages

When both players did send maps server will begin the game and draw who is starting. The player who starts the game will receive a message ```YOUR_TURN``` and the second player will get a message ``` YOUR_OPPONENT_TURN```. In next rounds players who will do their moves, before ``` YOUR_TURN ``` messages they will receive informations about last result of opponent move. In case that game has been ended player will not receive ``` YOUR_TURN ``` message. Possible informations:

``` HIT XY ``` - ship was hit at XY coordinates (more in SHOT XY command documentation)

``` MISS XY ``` - opponent missed at XY

``` HIT_AND_SINK XY ``` - opponent hit and sink ship at XY

``` VICTORY XY ``` - opponent hit and sink ship at XY and won the game

Example messages:

```
HIT A5
YOUR_TURN
```

```
MISS C2
YOUR_TURN
```

```
VICTORY F6
```

### SHOT XY

After receiving ``` YOUR TURN ``` message player which wants to play has to send ``` SHOT XY ``` message where X is a letter from range A-J and means position on x axis and Y is a number from range 0-9 that means position on y axis. Pair of thoose numbers are coordinates which means position on map. After using this command player can get response messeges as follows: /

``` HIT ``` - ship was hit

``` MISS ``` - player didn't hit anything

``` HIT_AND_SINK ``` - player hit and destroyed ship

``` VICTORY ``` - player destroyed last ship and won the game


### QUIT

Player that won or lost game could write ``` QUIT ``` command to end the game. If he did game will automatically end after 10 sec countdown.

### End of game

When player will see ``` VICTORY ``` message after using command ``` SHOT ``` or ``` LOSE ``` message, instead of ``` YOUR_TURN ``` the game is finished and both players have to use command ``` QUIT ``` to leave game.
