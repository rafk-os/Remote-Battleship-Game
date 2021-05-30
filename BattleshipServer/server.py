import socket
from threading import Thread, Lock
import re
from Rooms import Lobby
from Rooms import NoFreeRoom

class Server:
    def __init__(self, host, port, lobby):
        self.port = port
        self.lobby = lobby
        self.host = host

    def start(self):
        lock = Lock()
        server = Tcp(self.host, self.port, self.lobby, lock)
        server.start()
        server.join()


class Tcp(Thread):
    def __init__(self, host, port, lobby, lock):
        Thread.__init__(self)
        self.host = host
        self.port = int(port)
        self.lobby = lobby
        self.lock = lock

    def run(self):
        self.sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        self.sock.bind((self.host, self.port))
        self.sock.setblocking(0)
        self.sock.settimeout(5)
        self.sock.listen(1)

        while True:
            try:
                conn, addr = self.sock.accept()
                conn.send("HELLO You have been connected to a server using BGP version 1.0".encode("utf-8"))
            except socket.timeout:
                continue
        
                
            data = conn.recv(1024).decode("utf-8")

            if data == "LIST":
                try:
                    list = lobby.list()
                    conn.send("Available rooms:".encode("utf-8"))
                    for i in list:
                        conn.send((str(i)+" | ").encode("utf-8"))
                    conn.send(".".encode("utf-8"))
                except NoFreeRoom:
                    conn.send("NO_FREE_ROOM".encode("utf-8"))


            if re.search("^SIT [a-zA-Z0-9]*[_]?[a-zA-Z0-9]*$",data):
                data = data.decode("utf-8")
                data = data.split(" ")
                room_id = data[1]
                
                lobby.join(addr,room_id)

            if re.search("QUIT",data):
                #lobby.leave(addr)
                lobby.leave(addr,self.rooms[addr])
    


if __name__ == "__main__":
    lobby = Lobby(2)
    server = Server("127.0.0.1",2900,lobby)
    server.start()