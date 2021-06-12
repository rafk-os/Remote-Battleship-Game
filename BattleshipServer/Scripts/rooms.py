class Room:
    def __init__(self,identifier):
        self.capacity = 2
        self.players = []
        self.identifier = identifier

    
    def isRoom_full(self):
        if len(self.players) == self.capacity:
            return True
        else:
            return False
    #sprawdza czy dany gracz jest w tym pokoju
    def isThere(self,player_id):
        for i in self.players:
            if player_id==i.id:
                return True
        return False
    #zwraca drugiego gracza z pokoju
    def getTheOtherPlayer(self,player_id):
        for i in self.players:
            if player_id != i.id:
                return i


    def join(self,player):
        if not self.isRoom_full():
            self.players.append(player)
    
   
    def deletePlayer(self,player_id):
        for i in self.players:
            if player_id == i.id:
                self.players.remove(i)




class Lobby:

    def __init__(self, capacity=2):

        self.rooms = {'Room1' : Room(1), 'Room3' :Room(3),'Room5' : Room(5)}
        self.ALLplayers = []
        self.room_capacity = capacity
    def join(self, player, room_id):

        self.ALLplayers.append(player)

        if room_id in self.rooms.keys():
            if not self.rooms[room_id].isRoom_full():
                self.rooms[room_id].players.append(player)
                return room_id
            else:
                raise RoomFull()
        else:
            raise RoomNotFound()
    
    def checkPlayerCount(self,room_id):
        if self.rooms[room_id].isRoom_full():
            return 2
        else:
            return 1


    def list(self):
        response=[]
        if len(self.rooms) != 0:
            for roomId in self.rooms:
                if not self.rooms[roomId].isRoom_full():
                    response.append(roomId)
            return response
        else:
            raise NoFreeRoom()
    
    def leave(self,player_identifier):
        for i in self.rooms.values():
            if i.isThere(player_identifier):
                i.deletePlayer(player_identifier)
                for pl in self.ALLplayers:
                    if pl.id==player_identifier:
                         self.ALLplayers.remove(pl)         
                return player_identifier
        raise PlayerNotFound()
#szuka i zwraca gracza o podanym id
    def find(self,player_identifier):
        for i in self.rooms.items():
            if i[1].isThere(player_identifier):
                return i[1].getTheOtherPlayer(player_identifier)




    
      
        


class RoomFull(Exception):
    pass


class RoomNotFound(Exception):
    pass

class NoFreeRoom(Exception):
    pass

class PlayerNotFound(Exception):
    pass



