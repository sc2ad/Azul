class Center():
    def __init__(self):
        self.startingPlayer = 0
        self.startingTaken = False
        self.tiles = []
    def take(self, tileValue, player):
        """Takes a tile from the Center, with a given player.
        Will handle the starting player, but NOT the score
        """
        assert self.valueIn(tileValue), "Tile value not in the center!"
        out = []
        temp = []
        for i in range(len(self.tiles)):
            t = self.tiles[i]
            if t.value == tileValue:
                out.append(t)
            else:
                temp.append(t)
        self.tiles = temp
        # self.startingPlayer = player
        if not self.startingTaken:
            self.startingPlayer = player
            self.startingTaken = True
        return out
    def addTile(self, tile):
        self.tiles.append(tile)
    def valueIn(self, tileValue):
        """Returns true if the value is in the Center
        """
        for t in self.tiles:
            if t.value == tileValue:
                return True
        return False
    def getTileset(self):
        """Returns a set of unique tile values
        """
        a = []
        for t in self.tiles:
            # if t.name not in a:
            a.append(t.name)
        return a
    