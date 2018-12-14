import pygame
from Tile import Tile

class Center():
    width = 200
    tileBuffer = 25
    def __init__(self):
        self.startingPlayer = 0
        self.startingTaken = False
        self.tiles = []
        self.loc = [0,0]
        self.tileWidth = (self.width - self.tileBuffer) / (self.tileBuffer)
        self.rect = None
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
        self.updateTileLocations()
        return out
    def addTile(self, tile):
        self.tiles.append(tile)
        self.updateTileLocations()
    def updateTileLocations(self):
        for i in range(len(self.tiles)):
            x = ((i % self.tileWidth) + 1) * self.tileBuffer + self.loc[0]
            y = (i // self.tileWidth + 1) * self.tileBuffer + self.loc[1]
            self.tiles[i].loc = [x,y]
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
    def draw(self, screen):
        try:
            self.rect = pygame.Rect(self.loc[0], self.loc[1], self.width, (len(self.tiles) // self.tileWidth + 2) * self.tileBuffer)
            pygame.draw.rect(screen, (255,255,255), self.rect, 3)
            for t in self.tiles:
                t.draw(screen)
        except Exception:
            # Pygame either doesn't exist or isn't set up properly.
            print("Pygame not loaded, ignoring!")
    