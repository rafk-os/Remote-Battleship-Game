# Battleships Game Protocol (BGP)

## Introduction

The protocol is based on tcp. BGP was developed to support professional real-time battleships game.

## Documentation

Protocol works similar to SMTP/ESMTP protocol - defines the commands to be used on the client and server side. Protocol is adapted to work in Client-Server architectire. \
After connecting to the server, server will send information with name and version protocol. Example: 

``` HELLO You have been connected to a server using BGP version 1.0 ```

# Planning phase

In this phase all commands scope to prepare game.

## List of avaiable commands and server informations


### LIST

The purpose of this command is to send a request to the server for information on free game rooms. If the server finds an empty space, it will send it in single lines with a dot (```.```) at the end. If there are no available rooms server will send ``` NO_FREE_ROOM ``` information.

Request:

``` LIST ``` 

Example response (success):

```
Avaiable rooms:
1
3
8
.
```
Example response (fail):

```MP_FREE_ROOM```

### SIT <room_number>

When client want to join the room he has to send command ```SIT <room_number>``` where room_number is a number that determinetes room identifier. Server in response might send 4 possible messages: \
``` BAD_SYNTAX ``` - client used bad syntax \
``` BAD_ROOM ``` - client wanted to join to the room that does not exist \
``` NO_AVAIABLE_SLOTS ``` - client wanted to join to the room that does not have free slots \
``` JOINTED_SUCCESSFUL ``` - client have join to the room \

Example request:

```SIT 6```

Example response (success):

``` JOINED_SUCCESSFUL ```

Example response (fail):

``` BAD_ROOM ```

If client seccessfuly joined to the room server ``` JOINTED_SUCCESSFUL ``` message will send information about lobby status. There are two possible messages: ``` WAITING_FOR_SECOND_PLAYER ``` and ``` GAME_IS_STARTING ```. In case the client sees the first message, he will still see the second one after second player will join the room. \

Example:

```
WAITING_FOR_SECOND_PLAYER
GAME_IS_STARTING
```
