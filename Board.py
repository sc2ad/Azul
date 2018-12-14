from Tile import Tile
import pygame

class Board:
    tileBuffer = 25
    def __init__(self):
        self.board = []
        for i in range(5):
            o = []
            for j in range(5):
                o.append(None)
            self.board.append(o)
        # self.board = [[None] * 5] * 5
        self.rect = None
        self.loc = [0,0]
    def confirmReset(self):
        for row in self.board:
            for item in row:
                assert item == None, "Board should be empty, but isn't! "+str(self.board)
    def scoreBoard(self, tilesToAdd):
        """Adds tiles from tilesToAdd to the board and scores them.
        Returns the number of points awarded
        """
        assert len(tilesToAdd) == 5, "Must add a tilesToAdd of length 5!"
        score = 0
        for row in range(len(tilesToAdd)):
            tile = tilesToAdd[row]
            if tile == None:
                continue
            index = getIndex(row, tile)
            self.addTile(tile, row)
            score += self.scoreTile(row, index)
        return score
    def scoreTile(self, row, index):
        """Scores a given row/index combination using adjacents
        """
        score = 0
        q = index
        while q >= 0:
            if self.board[row][q] == None:
                break
            score += 1
            q -= 1
        q = index + 1 # Avoids double count
        while q < 5:
            if self.board[row][q] == None:
                break
            score += 1
            q += 1
        # Horizontal checks
        r = row
        while r >= 0:
            if self.board[r][index] == None:
                break
            score += 1
            r -= 1
        r = row + 1 # Avoids double count
        while r < 5:
            if self.board[r][index] == None:
                break
            score += 1
            r += 1
        return score - 1
    def addTile(self, tile, row):
        """Adds the given tile to the board, in the given row
        """
        assert not tile in self.board[row], "The tile already is in the row!"
        self.board[row][getIndex(row, tile)] = tile
        self.updateTileLocation(tile, row)
    def updateTileLocation(self, tile, row):
        tile.loc = [(row + 1) * self.tileBuffer + self.loc[0], (getIndex(row, tile) + 1) * self.tileBuffer + self.loc[1]]
    def completedRowCount(self):
        """Returns the completed number of rows of this board
        """
        out = 0
        for row in range(len(self.board)):
            if not None in self.board[row]:
                # This row is complete!
                out += 1
        return out
    def completedColumnCount(self):
        """Returns the completed number of columns of this board
        """
        out = 0
        for col in range(len(self.board)):
            complete = True
            for j in range(len(self.board)):
                if self.board[j][col] == None:
                    complete = False
            if complete:
                out += 1
        return out
    def completedAllCellsCount(self):
        """Returns the completed number of 'all cells' of this board
        Where there are 5 of each type of tile
        """
        uniqueCount = [0] * 5
        for row in self.board:
            for tile in row:
                if tile != None:
                    uniqueCount[tile.value] += 1
        out = 0
        for c in uniqueCount:
            if c == 5:
                out += 1
        return out
    def draw(self, screen):
        # I also want to draw the outlines of where each colored tile goes
        try:
            # One extra y-axis tile buffer for intentional throwaway
            self.rect = pygame.Rect(self.loc[0], self.loc[1], 6 * self.tileBuffer, 6 * self.tileBuffer)
            pygame.draw.rect(screen, (255,255,255), self.rect, 3)
            for row in self.board:
                for tile in row:
                    if tile != None:
                        tile.draw(screen)
        except Exception:
            # Pygame either doesn't exist or isn't set up properly.
            print("Pygame not loaded, ignoring!")
def getIndex(row, tile):
    """Finds the index to place the given tile, given the row.
    """
    assert type(tile) == Tile
    return (tile.value + row) % 5
