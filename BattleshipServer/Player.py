
#klasa zawierające informacje o graczu
class Player:
    def __init__(self,adr,port,id):
        self.id=id
        self.address=adr
        self.port=port
        self.isInGame=False
        self.GameShips=[]
        self.hisTurn=False
        self.message= 'none'
    def setStatus(self):
        self.isInGame=True


    
   