from Tile import Tile
from Board import Board
from TileBag import TileBag
class Placement:
    def __init__(self):
        self.placements = []
        self.reset()
        self.loss = 0
        self.lossTiles = []
    def reset(self):
        self.placements = []
        for i in range(5):
            t = []
            for j in range(i+1):
                t.append(None)
            self.placements.append(t)
    def confirmReset(self):
        for l in self.placements:
            for item in l:
                assert item == None, "Placement should be empty, but isn't!"+str(self.placements)
    def addTile(self, tile, row):
        """Adds a tile to a given row in the placement mat
        """
        assert 5 > row >= 0, "Row must be from 0 - 4"
        assert None in self.placements[row], "There must be an empty spot for the tile to be placed!"
        # If there are some other tiles in the row:
        empty = True
        for t in self.placements[row]:
            if t != None:
                empty = False
        if empty:
            self.placements[row][0] = tile
        else:
            # Find first empty instance
            assert self.placements[row][0].value == tile.value, "Tile values must match! Cannot add a different Tile to placements"
            for i in range(len(self.placements[row])):
                if self.placements[row][i] == None:
                    self.placements[row][i] = tile
                    break
    def possiblePlacemat(self, tiles):
        """Tests to make sure that tiles can be placed within this placemat.
        Returns True if ANY tiles can be placed, False otherwise.
        """
        for row in range(len(self.placements)):
            empty = True
            for tile in self.placements[row]:
                if tile != None:
                    empty = False
            if empty:
                return True
            if None in self.placements[row]:
                # A tile can still be placed
                if self.placements[row][0].value == tiles[0].value:
                    # The tile values are the same
                    return True
        return False
            
    def addTiles(self, tiles, row):
        """Adds tiles to a given row in the placement mat
        Remaining tiles are returned.
        """
        for i in range(len(tiles)):
            tile = tiles[i]
            try:
                self.addTile(tile, row)
            except AssertionError as e:
                if "empty spot" in str(e):
                    # Did I try to add when it was full?
                    return tiles[i:len(tiles)]
                else:
                    raise e
        return []
    def moveTiles(self):
        """Returns the tiles that will be moved to the board.
        If no tiles can be moved for a particular row, None is returned for that row.
        """
        out = [None] * 5
        for i in range(len(self.placements)):
            row = self.placements[i]
            if not None in row:
                # This row is complete!
                out[i] = row[len(row)-1]
                self.placements[i][len(row)-1] = None
        return out
    def discardTiles(self, tileBag, movedTiles):
        """To be called after scoring tiles, returns the other tiles to the bag
        """
        for i in range(len(self.placements)):
            assert None == self.placements[i][len(self.placements[i])-1], "The last item of each row must be a None!"
            if movedTiles[i] != None:
                tileBag.addToDiscard(self.placements[i][:len(self.placements[i])-1])
                self.placements[i][:len(self.placements[i])-1] = [None for _ in range(len(self.placements[i]))]
        [tileBag.addToDiscard(item) for item in self.lossTiles if type(item) == Tile]
        self.lossTiles = []
        # self.reset()
    def addScoreloss(self, tiles):
        # Need to discard the tiles that are extra back into the bag after calculating the score adjustment!
        # Otherwise the bag just loses tiles when players put them into the scoreloss zone
        assert type(tiles) == list, "Must input a valid list of tiles for scoreloss!"
        self.lossTiles.extend(tiles)
        self.loss = len(self.lossTiles)
        if len(self.lossTiles) > 2:
            self.loss += 1
        if len(self.lossTiles) > 4:
            self.loss += 1
    def calculateScoreloss(self):
        self.addScoreloss([]) # Confirms that the correct loss is calculated
        return self.loss
    def __str__(self):
        out = ""
        for row in self.placements:
            out += "Row: "+str(row)+"\n"
        return out