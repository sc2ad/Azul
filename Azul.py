from Board import *
from Factory import *
from Center import *
from Tile import *
from Placement import Placement
from TileBag import TileBag
import AI

playerPlacements = []
playerBoards = []
Bag = TileBag()
factories = []
scores = []
center = Center()

def setup(playerCount):
    global playerBoards, playerPlacements, Bag, factories, center
    assert 4 >= playerCount >= 2, "Cannot play with more than 4 players, or less than 2"
    for i in range(playerCount):
        playerBoards.append(Board())
        playerPlacements.append(Placement())
        scores.append(0)
    for i in range(playerCount + 2):
        # 2 Extra factories
        factories.append(Factory([]))
    Bag = TileBag()
    for board in playerBoards:
        board.confirmReset()
    for placement in playerPlacements:
        placement.confirmReset()
    center = Center()

def roundStart():
    for fact in factories:
        fact.addTiles(Bag.take(4)) # Takes 4 tiles from bag to put on each factory
    # for placement in playerPlacements:
    #     placement.reset()
    #     placement.confirmReset()
    center.startingTaken = False

def resetForRound():
    # Actually, nothing needs to happen here because it all happens at the beginning
    print("Round complete!")

def displayFactories():
    for i in range(len(factories)):
        fact = factories[i]
        if len(fact.tiles) != 0:
            print("Factory "+str(i)+" has: "+str(fact.tiles))

def displayCenter():
    print("The Center has:")
    print(center.tiles)

def displayBoard(player):
    print("Player %i's Board" % player)
    print(playerBoards[player])

def displayPlacement(player):
    print("Player %i's Placement Mat" % player)
    print(playerPlacements[player])

def showInstructions(player):
    print("It is now Player %i's Turn." % player)
    print("===========================")
    print("Choose to take from either the factories(f) or the center(c)")
    print("You can only choose one type of tile to take.")
    print("If you choose to take from the center, you will have a minus point for this round.")
    print("Any tiles you cannot place on your placement mat will become minus points for that round.")
    print("Play continues until a full row is complete.")
    print("Type 'f' for factories, 'c' for the center, or 'help' to see this again.")

def factoriesEmpty():
    for f in factories:
        if len(f.tiles) != 0:
            return False
    return True

def getFactoryChoice(player):
    inpu = -1
    while True:
        print("Make a factory selection. Pick a number 0 to %i" % (len(factories)-1))
        inp = input("Pick a factory: ")
        try:
            inpu = int(inp)
            if len(factories)-1 >= inpu >= 0 and len(factories[inpu].tiles) != 0:
                break
        except:
            # continue
            if inp.lower().startswith('q'):
                return []
        print("Please enter a valid factory index, or 'q' to rechoose.")
    while True:
        print("Now select a tile type from these options: ")
        print(factories[inpu].getTileset())
        inp = input("Tile type: ")
        if inp.lower().startswith('q'):
            return []
        for tile in factories[inpu].tiles:
            if inp.lower().startswith(str(tile)):
                val = ConvertNameToValue(str(tile))
                return factories[inpu].take(val, center)
        print("Please enter a valid tile type, or 'q' to rechoose.")

def getCenterChoice(player):
    while True:
        print("Select a tile type from these options: ")
        print(center.getTileset())
        inp = input("Tile type: ")
        if inp.lower().startswith('q'):
            return []
        for tile in center.tiles:
            if inp.lower().startswith(str(tile)):
                val = ConvertNameToValue(str(tile))
                return center.take(val, player)
        print("Please enter a valid tile type, or 'q' to rechoose.")

def takeTurn(player):
    displayFactories()
    displayCenter()
    displayBoard(player)
    displayPlacement(player)
    showInstructions(player)
    # Eventually show other player's boards and placements
    validInput = False
    takeFrom = []
    while not validInput:
        inp = input("Please enter an input: ")
        if inp.lower().startswith('f'):
            # Factory selection
            takeFrom = getFactoryChoice(player)
            if len(takeFrom) != 0:
                validInput = True
        elif inp.lower().startswith('c'):
            # Center selection
            if len(center.tiles) == 0:
                print("Cannot take any tiles from the center, because there are none!")
                continue
            takeFrom = getCenterChoice(player)
            if len(takeFrom) != 0:
                validInput = True
        elif inp.lower().startswith('help'):
            # Help show
            showInstructions(player)
    # Now find out where the player wants to put the tiles they took.
    print("You have "+str(len(takeFrom))+" "+str(takeFrom[0])+" tiles!")
    print("Now you must place them on your placement mat.")
    displayPlacement(player)
    while len(takeFrom) != 0:
        print("Where would you like to place your "+str(len(takeFrom))+" "+str(takeFrom[0])+" tiles?")
        inp = input("Location on placement mat: ")
        if not playerPlacements[player].possiblePlacemat(takeFrom):
            # Need to add -1 points and stuff!
            playerPlacements[player].addScoreloss(len(takeFrom))
            print("You know have " + str(playerPlacements[player].calculateScoreloss()) + " negative points this round.")
            return
        try:
            inpu = int(inp)
            if not 5 > inpu >= 0:
                # invalid input!
                print("Please enter a number from 0-4")
                continue
            try:
                takeFrom = playerPlacements[player].addTiles(takeFrom, inpu)

            except AssertionError:
                print("You cannot place tiles on a full row, or a row that already has tiles of a different type!")
                continue

        except:
            print("Please enter a number from 0-4")
        
def takeAITurn(player):
    # Also include inputs of factories and center (possibly Bag?)

    chosenTiles = AI.getChosenTiles(player, playerBoards, playerPlacements, scores, factories, center)
    while len(chosenTiles) != 0:
        if not playerPlacements[player].possiblePlacemat(chosenTiles):
            # Need to add -1 points and stuff!
            playerPlacements[player].addScoreloss(len(chosenTiles))
            return
        chosenLocation = AI.getChosenLocation(player, playerBoards, playerPlacements, scores)
        playerPlacements[player].addTiles(chosenTiles)

def playRound():
    roundStart()
    activePlayer = center.startingPlayer
    while len(center.tiles) != 0 or not factoriesEmpty():
        if activePlayer != 0:
            takeAITurn(activePlayer)
        else:
            takeTurn(activePlayer)
        activePlayer = (activePlayer + 1) % len(playerBoards)
    # The round is over now. Score everything and reset the round for the next round
    # scores[center.startingPlayer] -= 1
    playerPlacements[center.startingPlayer].addScoreloss(1)
    for i in range(len(playerPlacements)):
        scores[i] -= playerPlacements[i].calculateScoreloss()
        tiles = playerPlacements[i].moveTiles()
        playerPlacements[i].discardTiles(Bag, tiles)
        score = playerBoards[i].scoreBoard(tiles)
        scores[i] += score
    # Things are scored. Now reset for the next round.
    resetForRound()

def checkEnd():
    for b in playerBoards:
        if b.completedRowCount() > 0:
            return True
    return False

def finalScoring():
    for i in range(len(playerBoards)):
        scores[i] += playerBoards[i].completedRowCount() * 2
        scores[i] += playerBoards[i].completedColumnCount() * 7
        scores[i] += playerBoards[i].completedAllCellsCount() * 10

def playGame():
    pCount = 0
    while not 4 >= pCount >= 2:
        pstr = input("How many players: ")
        try:
            pCount = int(pstr)
        except:
            pass
    setup(pCount)
    while not checkEnd():
        playRound()
        print("The scores are now: ")
        for p in range(len(scores)):
            print("Player", p, "has score:", scores[p])
    finalScoring()
    print(scores)

if __name__ == "__main__":
    playGame()
