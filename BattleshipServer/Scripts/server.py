from rooms import Lobby
from Protocol import Server


if __name__ == "__main__":
    GameLobby = Lobby()
    GameServer = Server("",2900,GameLobby)
    GameServer.start()