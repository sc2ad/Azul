class AIType:
    MaxGain = 0
    MinLoss = 1
    MiniMax = 2
    NEAT = 3

class MaxGainStrategy:
    aiType = AIType.MaxGain
    @staticmethod
    def getChosenTiles(player, boards, placements, scores, factories, center):
        # Calculate all the scores that can be achieved from the current setup of factories and the center
        # Then use these scores to determine the best tiles to take in order to achieve the maximum possible gain
        # This is obviously suboptimal, as there could be an incredibly slim possibility of getting a large number of points
        # So this should probably be adapted to be a more "maximuze average gain" strategy instead, or something
        # REMEMBER THAT OUTPUT MUST BE VALID!
        pass
    @staticmethod
    def getChosenLocation(player, tiles, boards, placements, scores):
        # Given the tiles that this has been provided, place these tiles in the location that acheives the maximum (or maximum average gain)
        # That we are looking for.
        # For placement onto the board, however, it is probably reasonable to simply go with maximum gain.
        # REMEMBER THAT OUTPUT MUST BE VALID!
        pass
    @staticmethod
    def takeTurn(player, boards, placements, scores, factories, center):
        # Takes a full turn for the AI
        # This should use conventional methods to confirm that everything is legally done
        # As in: This method should call the various functions on placement, factories, and the center (legally)
        # in order to complete the turn
        pass
SELECTED_AI = MaxGainStrategy

def getChosenTiles(player, boards, placements, scores, factories, center):
    """Gets the tiles that would be chosen by a player
    THE TILES GOTTEN MUST BE LEGAL, THEY SHOULD BE EITHER FROM A FACTORY OR FROM THE CENTER
    THESE TILES MUST ALSO BE REMOVED FROM THE APPROPRIATE LOCATION
    """
    return SELECTED_AI.getChosenTiles(player, boards, placements, scores, factories, center)

def getChosenLocation(player, tiles, boards, placements, scores):
    """Gets the location of the tiles that can be placed
    MUST RETURN A VALID LOCATION (DOES NOT CONFLICT) AND MUST BE WITHIN -1 AND 4
    -1 CORESPONDS TO A "DISCARD" ACTION
    """
    out = SELECTED_AI.getChosenLocation(player, tiles, boards, placements, scores)
    assert out == -1 or len(placements[player].addTiles(tiles, out)) != len(tiles), "The length of the input and output tiles are the same, indicating that there was an invalid index provided!"
    return out

def takeTurn(player, boards, placements, scores, factories, center):
    """Takes a turn for an AI.
    This handles the taking of the tiles and the placing of the tiles.
    All of this should happen through conventional function calls (as in, the same function calls human players use)
    The tiles should be obtained from the factory/center and placed on the placemat (or placed into the discard)
    """
    SELECTED_AI.takeTurn(player, boards, placements, scores, factories, center)