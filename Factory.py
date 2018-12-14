from Tile import Tile
from Center import Center
import pygame

returningSets = False

class Factory:
    def __init__(self, tiles):
        self.tiles = tiles
        self.loc = [0,0] # Center
        self.radius = 50
    def addTiles(self, tiles):
        # assert len(tiles) == 4, "Can only add 4 tiles to a factory!"
        self.tiles = tiles
        for i in range(len(self.tiles)):
            t = self.tiles[i]
            x = self.loc[0] - self.radius/3 + 2 * self.radius/3 * (i % 2)
            y = self.loc[1] - self.radius/3 + 2 * self.radius/3 * (i // 2)
            t.loc = [x, y]
    def take(self, tileValue, center):
        """Returns a list of Tiles of the given value
        And places the rest of the tiles into the Center
        """
        assert self.valueIn(tileValue), "The TileValue must be in the factory"
        assert type(center) == Center, "The second argument must be a Center object"
        out = []
        for t in self.tiles:
            if t.value == tileValue:
                out.append(t)
            else:
                center.addTile(t)
        self.tiles = []
        return out
    def valueIn(self, tileValue):
        """Returns true if the value is in the factory
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
        if returningSets:
            return set(a)
        return a
    def draw(self, screen):
        try:
            pygame.draw.circle(screen, (255,255,255), self.loc, self.radius, 3)
            for t in self.tiles:
                t.draw(screen)
        except Exception:
            # Pygame either doesn't exist or isn't set up properly.
            print("Pygame not loaded, ignoring!")
