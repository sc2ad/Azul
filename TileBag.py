from Tile import Tile
import random
class TileBag:
    def __init__(self):
        self.bag = []
        self.discard = []
        for i in range(20):
            for j in range(5):
                self.bag.append(Tile(j))
    def take(self, count):
        """Returns a random list of the remaining tiles.
        If there are not enough, it shuffles the discard back in
        """
        out = []
        for i in range(count):
            if len(self.bag) == 0:
                self.addTiles(self.discard)
                self.discard = []
            index = random.randint(0, len(self.bag)-1)
            out.append(self.bag[index])
            del(self.bag[index])
        return out
    def addTiles(self, tiles):
        for t in tiles:
            self.bag.append(t)
    def addToDiscard(self, tiles):
        for t in tiles:
            self.discard.append(t)