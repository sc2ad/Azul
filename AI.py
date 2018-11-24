
def getChosenTiles(player, boards, placements, scores, factories, center):
    """Gets the tiles that would be chosen by a player

    """
    pass

def getChosenLocation(player, tiles, boards, placements, scores):
    """Gets the location of the tiles that can be placed
    MUST RETURN A VALID LOCATION (DOES NOT CONFLICT) AND MUST BE WITHIN -1 AND 4
    -1 CORESPONDS TO A "DISCARD" ACTION
    """
    out = -1

    assert len(placements[player].addTiles(tiles, out)) != len(tiles), "The length of the input and output tiles are the same, indicating that there was an invalid index provided!"