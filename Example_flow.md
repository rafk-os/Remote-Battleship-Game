## Lines marked as:
- COMMAND means message that's send to the server
- RECEIVED means message that's received from the server
- Others means that they were displayed by client (not included in protocol response)

```
Received: hello hello
COMMAND: LIST
Received: Avaiable rooms:
Received: 1
Received: 1
Received: 1
Received: .
Avaiable rooms: | 1 | 1 | 1 | Insert room name: 1
COMMAND: SIT 1
Received: s
COMMAND: LIST
Received: 1
Received: 1
Received: 1
Received: .
Avaiable rooms: | 1 | 1 | Insert room name: 1
COMMAND: SIT 1
Received: JOINING_SUCCESSFUL
Received: GAME_IS_STARTING
COMMAND: ooooooooox
COMMAND: ooooooooox
COMMAND: ooxooooooo
COMMAND: ooxoxooxox
COMMAND: ooxoxooxox
COMMAND: ooxoxooxoo
COMMAND: ooxoxoooox
COMMAND: ooooooooox
COMMAND: ooooooooox
COMMAND: oooooooooo
Received: MAP_SEND_SUCCESSFULLY
Received: YOUR_TURN
COMMAND: SHOT C2
Received: HIT
Received: MISS C2
Received: YOUR_TURN
COMMAND: SHOT D2
Received: HIT_AND_SINK
.
.
.
COMMAND: SHOT A1
Received: VICTORY
VICTORY! QUITING IN 10 SEC.
COMMAND: QUIT
```
