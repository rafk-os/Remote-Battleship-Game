import socket
import ssl
from _thread import *
from threading import Lock
import re
from Rooms import NoFreeRoom
from Rooms import RoomFull
from Rooms import RoomNotFound
from Rooms import PlayerNotFound
from Player import Player
import asyncore

#Konwersje z tablic na to co przychodzi od klienta
conversions={
    'A':0,
    'B':1,
    'C':2,
    'D':3,
    'E':4,
    'F':5,
    'G':6,
    'H':7,
    'I':8,
    'J':9
}

#Funkcja która analizuje mape od klienta i wyciąga koordynaty statków

def GetPlayerShips(WholeMap):
    VisitedNodes=[]
    GameShips=[]
    for y in range(10):
        for x in range(10):
            if WholeMap[y][x] =='x' and (y,x) not in VisitedNodes:
                k=y
                m=x
                newShipCell=True
                left=right=up=down=False
                VisitedNodes.append((y,x))
                NewShip=[(x,y)]  
                while WholeMap[k][m] == 'x' and newShipCell:
                    newShipCell = False 
                    if k+1 <=9 and (k+1,m) not in VisitedNodes:
                        VisitedNodes.append((k+1,m))
                        if WholeMap[k+1][m] == 'x':
                            newShipCell=True
                            down=True
                            NewShip.append((m,k+1))
                    if k-1 >=0 and (k-1,m) not in VisitedNodes:
                        VisitedNodes.append((k-1,m))
                        if WholeMap[k-1][m] == 'x':
                            newShipCell=True
                            up=True
                            NewShip.append((m,k-1))
                    if m+1 <=9 and (k,m+1) not in VisitedNodes:
                        VisitedNodes.append((k,m+1))
                        if WholeMap[k][m+1] == 'x':
                            newShipCell=True
                            right=True
                            NewShip.append((m+1,k))
                    if  m-1 >=0 and (k,m-1) not in VisitedNodes:
                        VisitedNodes.append((k,m-1))
                        if WholeMap[k][m-1] == 'x':
                            newShipCell=True
                            left=True
                            NewShip.append((m-1,k)) 
                    if up:
                        up=False
                        k=k-1
                    elif down:
                        down=False
                        k=k+1
                    elif right:
                        right=False
                        m=m+1
                    elif left:
                        left=False
                        m=m-1


                GameShips.append(NewShip)
    
    return GameShips



                
                


class Server(asyncore.dispatcher):
    def __init__(self, host, port, lobby):
        print ("[Initialization] Server is starting....")
        asyncore.dispatcher.__init__(self)
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        addr = (host, port)
        self.bind(addr)
        self.listen(1)
        #self.setblocking(0)
        #self.settimeout(5)
        self.port = port
        self.lobby = lobby
        self.host = host
        id=0
        print ("[Initialization] Server has started....")

    def handle_accept(self):
        conn, addr = self.accept()
        secureClientSocket = ssl.wrap_socket(conn,server_side=True,ssl_version=ssl.PROTOCOL_TLS,certfile="BattleshipServer/rootCA.crt",keyfile="BattleshipServer/rootCA.key")
        id+=1
        initializeTunnel(secureClientSocket,addr,self.lobby,id)

    def start(self):
        asyncore.loop()

# tworzenie nowego wątku oraz wywołanie funkcji obsługującej klienta 
def initializeTunnel(connection,addr,lobby,id):
    print("[RESPONSE] Player " + str(id) + " established a connection with  server.")
    connection.send("HELLO You have been connected to a server using BGP version 1.0\r\n".encode("utf-8"))
    tunnel = BGP(connection,addr[0],addr[1], lobby, id)
    connection.close()     
class BGP(asyncore.dispatcher_with_send):
    def __init__(self,conn, host, port, lobby,id):
        self.conn=conn
        self.host=host
        self.port=port
        self.lobby = lobby
        self.id=id


    def handle_read(self):
        player= Player(str(self.host),str(self.port),self.id)
        data = self.recv(1024).decode("utf-8")
        #OBSŁUGA LIST
        if data == "LIST\r\n":
            try:
                list = self.lobby.list()
                self.send("Available rooms:\r\n".encode("utf-8"))
                for i in list:
                    self.send((str(i) + "\r\n").encode("utf-8"))
                self.send(".\r\n".encode("utf-8"))
                print("[RESPONSE] Player " + str(player.id) + " recived requested rooms list.")
            except NoFreeRoom:
                print ("[ERROR] Player " + str(player.id) +" cannot see list  because there is no free room.")
                self.send("NO_FREE_ROOM\r\n".encode("utf-8"))

        #OBSŁUGA SIT razem z odebraniem map graczy

        elif re.search("SIT Room[0-9]\r\n",data):
            twoTime=False
            data = data.split(" ")
            room_id = data[1]
            room_id=room_id.replace("\r","")
            room_id=room_id.replace("\n","")
            try:
                roomData = self.lobby.join(player,room_id)
                self.send("JOINING_SUCCESSFUL\r\n".encode("utf-8"))
                print("[RESPONSE] Player " + str(player.id) + " has joined room " + str(roomData))
                playerCount=self.lobby.checkPlayerCount(roomData)

                #jeżeli gracz jest pierwszy w pokoju
                if playerCount == 1:
                    serverResponse="WAITING_FOR_SECOND_PLAYER\r\n"
                    secondServerResponse="GAME_IS_STARTING\r\n"
                    endingResponse="YOUR_TURN\r\n"
                    twoTime=True
                    player.hisTurn=True
                    self.send((serverResponse).encode("utf-8"))
                    while True:
                        if self.lobby.checkPlayerCount(roomData) == 2:
                            break
                  #jeżeli gracz jest drugi w pokoju
                else:
                    serverResponse="GAME_IS_STARTING\r\n"
                    player.hisTurn=False
                    endingResponse="YOUR_OPPONENT_TURN\r\n"

                #pobieranie mapy
                if not player.hisTurn:
                    self.send((serverResponse).encode("utf-8"))
                if twoTime:
                    self.send((secondServerResponse).encode("utf-8"))
                print("[RESPONSE] Player " + str(player.id) + " has started game in room: " + str(roomData))
                player.setStatus()
                WholeMap=[]
                for i in range(10):
                     mapLine = self.recv(1024).decode("utf-8")
                     mapLine=mapLine.replace("\r","")
                     mapLine=mapLine.replace("\n","")
                     WholeMap.append(mapLine)


                player.GameShips=GetPlayerShips(WholeMap)
                self.send("MAP_SEND_SUCCESSFULLY\r\n".encode("utf-8"))
                self.send(endingResponse.encode("utf-8"))
                print("[RESPONSE] Player " + str(player.id) + " has second turn in room: " + str(roomData))
                if not player.hisTurn:
                    self.lobby.send=True



            except RoomFull:
                print ("[ERROR] Player " + str(player.id) +" cannot join room: " + room_id + " because room is full.")
                self.send("NO_AVAIABLE_SLOTS\r\n".encode("utf-8"))
            except RoomNotFound:
                print ("[ERROR] Player " + str(player.id) +" cannot join room: " + room_id + " because room doesnt exist.")
                self.send("WRONG_ROOM\r\n".encode("utf-8"))

        #obsługa quit

        elif data == "QUIT\r\n":
            print("[RESPONSE] Player " + str(player.id) + " has closed  connection to the server." )
            try:
               player_id= self.lobby.leave(player.id)
               print("[RESPONSE] Player " + str(player_id) + " has been  deleted from server database." )
               self.close()

            except PlayerNotFound:
                print ("[ERROR] Cannot delete "+ str(player.id) + " : Player not found.")

        else:
            print("[RESPONSE] Player " + str(player.id) + " has used wrong command." )
            self.send("BAD_SYNTAX\r\n".encode("utf-8"))


        #obsługa wszystkiego co się dzieje podczas gry

        while  player.isInGame:
            #obsługa gracza strzelającego
            if player.hisTurn:
                print("[RESPONSE] Player " + str(player.id) + " is in his turn." )
                data = self.recv(1024).decode("utf-8")
                if re.search("SHOT [A-J][0-9]\r\n",data):
                    data=data.replace("\r","")
                    data=data.replace("\n","")
                    data = data.split(" ")
                    #pobranie koordynatów strzału oraz pobranie mapy statków przeciwnika i sprawdzenie czy strzał był celny
                    trueCord=data[1]
                    cord=(conversions[trueCord[-2]],int(trueCord[-1]))

                    opponent=self.lobby.find(player.id)
                    OponnentShips= opponent.GameShips

                    foundShip=False
                    sunkShip=False
                    victory=False
                    for shipList in OponnentShips:
                        if foundShip:
                            break
                        for shipCell in shipList:
                            if cord==shipCell:
                                foundShip=True
                                if len(shipList) == 1:
                                    sunkShip=True
                                    if len(OponnentShips)==1:
                                        victory=True
                                    OponnentShips.remove(shipList)
                                if not sunkShip:
                                    shipList.remove(cord)
                                break
                    #wysłanie wiadomości do klienta oraz oznaczenie wiadomości dla oponenta odnośnie wykonanego strzału
                    if foundShip and not sunkShip:
                        self.send("HIT\r\n".encode("utf-8"))
                        print("[RESPONSE] Player " + str(player.id) + " hit enemy ship.")
                        opponent.message="HIT "+trueCord+"\r\n"
                    elif foundShip and sunkShip and not victory:
                        self.send("HIT_AND_SINK\r\n".encode("utf-8"))
                        print("[RESPONSE] Player " + str(player.id) + " sunk enemy ship.")
                        opponent.message="HIT_AND_SINK "+trueCord+"\r\n"
                    elif foundShip and sunkShip and victory:
                        self.send("VICTORY\r\n".encode("utf-8"))
                        print("[RESPONSE] Player " + str(player.id) + " won the game.")
                        opponent.message="VICTORY "+trueCord+"\r\n"
                        player.isInGame=False
                        break
                    elif not foundShip:
                        self.send("MISS\r\n".encode("utf-8"))
                        print("[RESPONSE] Player " + str(player.id) + " missed shot.")
                        opponent.message="MISS "+trueCord+"\r\n"
                    player.hisTurn=False
                else:
                    print("[RESPONSE] Player " + str(player.id) + " has used wrong command." )
                    self.send("BAD_SYNTAX\r\n".encode("utf-8"))
            else:
                #obsługa gracza czekającego na ture
                print("[RESPONSE] Player " + str(player.id) + " is waiting." )
                while True:
                    if player.message != 'none':
                        if re.search("VICTORY [A-J][0-9]\r\n",player.message):
                            self.send(player.message.encode("utf-8"))
                            print("[RESPONSE] Player " + str(player.id) + " has lost ." )
                            player.isInGame=False
                            break

                        else:
                            self.send(player.message.encode("utf-8"))
                            print("[RESPONSE] Player " + str(player.id) + " has recieved map update ." )
                            self.send("YOUR_TURN\r\n".encode("utf-8"))
                            player.hisTurn=True
                            player.message='none'
                            break


