# Battleships Game Protocol (BGP)

## Introduction

The protocol is based on tcp. BGP was developed to support professional real-time battleships game.

## Documentation

Protocol works similar to SMTP/ESMTP protocol - defines the commands to be used on the client and server side. Protocol is adapted to work in Client-Server architectire. \
After connecting to the server, server will send information with name and version protocol. Example: 

``` HELLO You have been connected to a server using BGP version 1.0 ```

## List of avaiable commands and server informations


### LIST

The purpose of this command is to send a request to the server for information on free game rooms. if there are no available rooms server will send ``` NO_FREE_ROOM ``` \

Example request:

``` LIST ``` \

Example response:

```
Avaiable rooms:



