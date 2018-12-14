import math

def dist(pos1, pos2):
    assert len(pos1) == len(pos2) == 2, "Lengths of positions must be 2!"
    return math.sqrt((pos2[0] - pos1[0]) ** 2 + (pos2[1] - pos1[1]) ** 2)