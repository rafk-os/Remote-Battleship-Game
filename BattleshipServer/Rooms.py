class Room:
    def __init__(self,capacity,identifier):
        self.capacity = capacity
        self.players = []
        self.identifier = identifier


    def join(self,player):
        if not self.is_full():
            self.players.append(player)
    
    def isRoom_full(self):
        if len(self.players) == self.capacity:
            return True
        else:
            return False
    def deletePlayer(self,player):
        self.players.remove(player)




class Lobby:

    def __init__(self, capacity=2):

        self.rooms = {'Room1' : Room(capacity,1), 'Room3' :Room(capacity,3),'Room5' : Room(capacity,5)}
        self.ALLplayers = []
        self.room_capacity = capacity

    def join(self, player_identifier, room_id):

        self.ALLplayers.append(player_identifier)

        if 'Room'+str(room_id) in self.rooms.keys():
            Room_Name= 'Room'+str(room_id)
            if not self.rooms[Room_Name].isRoom_full:
                self.rooms[Room_Name].players.append(player_identifier)
                return room_id
            else:
                raise RoomFull()
        else:
            raise RoomNotFound()

    def list(self):
        response=[]
        if len(self.rooms) != 0:
            for roomId in self.rooms:
                if not self.rooms[roomId].isRoom_full:
                    response.append(roomId)
            return response
        else:
            raise NoFreeRoom()
    
    def leave(self,player_identifier,room_id):
        self.rooms['Room'+str(room_id)].deletePlayer(player_identifier)
        


class RoomFull(Exception):
    pass


class RoomNotFound(Exception):
    pass

class NoFreeRoom(Exception):
    pass



