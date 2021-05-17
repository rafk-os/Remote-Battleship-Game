# Battleships Game Protocol (BGP)

## Introduction

The protocol is based on tcp. BGP was developed to support professional real-time battleships game.

## Documentation

Protocol works similar to SMTP/ESMTP protocol - defines the commands to be used on the client and server side. Protocol is adapted to work in Client-Server architectire.

## List of commands and informations


### Hello message

After connecting to the server, server will send information with name and version protocol. Example: \

``` HELLO You have been connected to a server using BGP version 1.0 ``` 

