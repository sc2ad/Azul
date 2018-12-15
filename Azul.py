from Board import *
from Factory import *
from Center import *
from Tile import *
from Placement import Placement
from TileBag import TileBag
import AI
import Util

import math
import sys
import time
import pygame
from pygame.locals import *

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
        except KeyboardInterrupt:
            sys.exit(0)
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
        elif inp.lower().startswith('q'):
            sys.exit(0)
    # Now find out where the player wants to put the tiles they took.
    print("You have "+str(len(takeFrom))+" "+str(takeFrom[0])+" tiles!")
    print("Now you must place them on your placement mat.")
    displayPlacement(player)
    while len(takeFrom) != 0:
        print("Where would you like to place your "+str(len(takeFrom))+" "+str(takeFrom[0])+" tiles?")
        if not playerPlacements[player].possiblePlacemat(takeFrom):
            # Need to add -1 points and stuff!
            playerPlacements[player].addScoreloss(takeFrom)
            print("You now have " + str(playerPlacements[player].calculateScoreloss()) + " negative points this round.")
            return
        inp = input("Location on placement mat: ")
        try:
            inpu = int(inp)
            if not 5 > inpu >= -1:
                # invalid input!
                print("Please enter a number from -1-4")
                continue
            try:
                if inpu == -1:
                    playerPlacements[player].addScoreloss(takeFrom)
                    print("You now have " + str(playerPlacements[player].calculateScoreloss()) + " negative points this round.")
                    return
                takeFrom = playerPlacements[player].addTiles(takeFrom, inpu)

            except AssertionError:
                print("You cannot place tiles on a full row, or a row that already has tiles of a different type!")
                continue
        except KeyboardInterrupt:
            sys.exit(0)
        except:
            if inp.lower() == "q":
                takeTurn(player)
                return
            print("Please enter a number from -1-4")
        
def takeAITurn(player):
    # Also include inputs of factories and center (possibly Bag?)
    AI.takeTurn(player, playerBoards, playerPlacements, scores, factories, center)
    chosenTiles = AI.getChosenTiles(player, playerBoards, playerPlacements, scores, factories, center)
    while len(chosenTiles) != 0:
        if not playerPlacements[player].possiblePlacemat(chosenTiles):
            # Need to add -1 points and stuff!
            playerPlacements[player].addScoreloss(chosenTiles)
            return
        chosenLocation = AI.getChosenLocation(player, chosenTiles, playerBoards, playerPlacements, scores)
        playerPlacements[player].addTiles(chosenTiles, chosenLocation)

def playRound():
    roundStart()
    activePlayer = center.startingPlayer
    while len(center.tiles) != 0 or not factoriesEmpty():
        if activePlayer != 0:
            # takeAITurn(activePlayer)
            takeTurn(activePlayer)
        else:
            takeTurn(activePlayer)
        activePlayer = (activePlayer + 1) % len(playerBoards)
    # The round is over now. Score everything and reset the round for the next round
    # scores[center.startingPlayer] -= 1
    playerPlacements[center.startingPlayer].addScoreloss([1])
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

### PYGAME STUFF
## BEGIN
clock = pygame.time.Clock()
width = 1024
height = 1024
screen = None
playingRound = False

target = None
selectedTiles = None
activePlayer = 0

def start_pygame():
    global screen
    pygame.init()

    pygame.display.set_caption("Azul")
    screen = pygame.display.set_mode([width, height])
    pCount = 0
    while not 4 >= pCount >= 2:
        pstr = input("How many players: ")
        try:
            pCount = int(pstr)
        except:
            pass
    setup(pCount)
    center.loc = [width/2 - center.width/2, height/2 - 3 * center.tileBuffer]
    setupFactoryLocations()
    setupPlacementLocations()
    setupBoardLocations()
    event_loop()

def setupFactoryLocations():
    numFactories = len(factories)

    dist = 200
    delta = 2 * math.pi / numFactories
    for i in range(numFactories):
        factories[i].loc = [int(width/2 + dist * math.cos(delta * i)), int(height/2 - dist * math.sin(delta * i))]

def setupPlacementLocations():
    numPlacements = len(playerPlacements)

    delta = width / (numPlacements)
    y = height - int(7.5 * Placement.tileBuffer)
    for i in range(numPlacements):
        playerPlacements[i].loc = [int(delta * i + 0.5 * Placement.tileBuffer), y]

def setupBoardLocations():
    numBoards = len(playerBoards)

    delta = width / numBoards
    y = height - int(7.5 * Board.tileBuffer)
    for i in range(numBoards):
        playerBoards[i].loc = [int(delta * i + 8 * Board.tileBuffer), y]

def draw():
    global target
    screen.fill((0,0,0))
    if target == None:
        center.draw(screen)
        for f in factories:
            f.draw(screen)
        for p in playerPlacements:
            p.draw(screen)
        for b in playerBoards:
            b.draw(screen)
    else:
        # This is where I would also upscale the target to be really big so it would be easy to select tiles
        target.draw(screen)
        if type(target) == Placement or type(target) == Board:
            if target == playerPlacements[activePlayer] or target == playerBoards[activePlayer]:
                playerPlacements[activePlayer].draw(screen)
                playerBoards[activePlayer].draw(screen)
    # print("Attempting to draw!")
    pygame.display.flip()
    # Eventually add the board and placements

def event_loop():
    global playingRound
    global selectedTiles
    global target
    global activePlayer
    while True:
        # Or maybe it would be better to just draw a black box over the player and go from there?
        draw()
        pygame.display.flip()
        if not checkEnd():
            if not playingRound:
                roundStart()
                activePlayer = center.startingPlayer
                playingRound = True
            if (len(center.tiles) != 0 or not factoriesEmpty() or selectedTiles != None) and playingRound:
                draw()
                pygame.display.flip()
                # if activePlayer != 0:
                #     # takeAITurn(activePlayer)
                #     takeTurn(activePlayer)
                # else:
                #     takeTurn(activePlayer)
                # # clock.tick(30)
                # activePlayer = (activePlayer + 1) % len(playerBoards)
            else:
                playingRound = False
                # The round is over now. Score everything and reset the round for the next round
                # scores[center.startingPlayer] -= 1
                playerPlacements[center.startingPlayer].addScoreloss([1])
                for i in range(len(playerPlacements)):
                    scores[i] -= playerPlacements[i].calculateScoreloss()
                    tiles = playerPlacements[i].moveTiles()
                    playerPlacements[i].discardTiles(Bag, tiles)
                    score = playerBoards[i].scoreBoard(tiles)
                    scores[i] += score
                # Things are scored. Now reset for the next round.
                resetForRound()
                print("The scores are now: ")
                for p in range(len(scores)):
                    print("Player", p, "has score:", scores[p])
        else:
            finalScoring()
            print(scores)

        for event in pygame.event.get():
            if event.type == QUIT:
                sys.exit(0)
            if event.type == KEYDOWN:
                if event.key == K_q:
                    raise TypeError("Time to quit!")
            if event.type == MOUSEBUTTONDOWN:
                pos = pygame.mouse.get_pos()
                # Test to see if the mouse is within any of the factories
                if center.rect.collidepoint(pos):
                    if target == center:
                        # Need to check to see if I hit a tile, if so, add it!
                        for t in target.tiles:
                            if t.rect.collidepoint(pos):
                                selectedTiles = target.take(t.value, activePlayer)
                                # Where would you like to place said tiles?
                                target = playerPlacements[activePlayer]
                                if not target.possiblePlacemat(selectedTiles):
                                    # Can't place the tiles anywhere
                                    target.addScoreloss(selectedTiles)
                                    # Move to next player
                                    target = None
                                    selectedTiles = None
                                    activePlayer = (activePlayer + 1) % len(playerBoards)
                                break
                        if target == center:
                            target = None
                    else:
                        target = center
                else:
                    if target is None:
                        for f in factories:
                            if Util.dist(pos, f.loc) < f.radius:
                                target = f
                                break
                    elif type(target) is Factory:
                        for t in target.tiles:
                            if t.rect.collidepoint(pos):
                                # Select this tile!
                                selectedTiles = target.take(t.value, center)
                                # Where would you like to place said tiles?
                                target = playerPlacements[activePlayer]
                                if not target.possiblePlacemat(selectedTiles):
                                    # Can't place the tiles anywhere
                                    target.addScoreloss(selectedTiles)
                                    # Move to next player
                                    target = None
                                    selectedTiles = None
                                    activePlayer = (activePlayer + 1) % len(playerBoards)
                                break
                        if selectedTiles == None:
                            target = None
                    if target is None:
                        for p in playerPlacements:
                            if p.rect.collidepoint(pos):
                                target = p
                                break
                    elif type(target) == Placement and selectedTiles != None:
                        # Check for selection of row to determine where to place the tiles!
                        for row in range(len(target.placements)+1):
                            low = (row + 1) * target.tileBuffer + target.loc[1] - Tile.width / 2
                            up = (row + 1) * target.tileBuffer + target.loc[1] + Tile.width / 2
                            if pos[1] >= low and pos[1] <= up:
                                # Then row is the selected row!
                                if row == 5:
                                    # This handles intentional throwaway
                                    target.addScoreloss(selectedTiles)
                                    selectedTiles = []
                                    break
                                try:
                                    selectedTiles = target.addTiles(selectedTiles, row)
                                except AssertionError as e:
                                    # This means that the player tried to add the tiles to a row that already had tiles
                                    # Or an invalid row
                                    # Or the row already contains different colored tiles
                                    # Or the row has a completed tile of the same type
                                    break
                                # Confirm that nothing is impossible
                                if len(selectedTiles) != 0 and not target.possiblePlacemat(selectedTiles):
                                    # Can't place the tiles anywhere
                                    target.addScoreloss(selectedTiles)
                                    # Move to next player
                                    target = None
                                    selectedTiles = None
                                    activePlayer = (activePlayer + 1) % len(playerBoards)
                        if len(selectedTiles) == 0:
                            # Move to next player
                            target = None
                            selectedTiles = None
                            activePlayer = (activePlayer + 1) % len(playerBoards)
                    elif type(target) == Placement:
                        target = None
                    elif target == center:
                        target = None
                    if target is None:
                        for b in playerBoards:
                            if b.rect.collidepoint(pos):
                                target = b
                                break
                    elif type(target) == Board:
                        target = None

if __name__ == "__main__":
    # playGame()
    start_pygame()
