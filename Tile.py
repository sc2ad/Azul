import pygame

class Tile:
    width = 20
    def __init__(self, value):
        if type(value) == int:
            assert 5 > value >= 0, "Value must be a valid integer!" 
            self.value = value
            self.name = ConvertValueToName(value)
        elif type(value) == str:
            self.name = value
            self.value = ConvertNameToValue(value)
        else:
            raise ValueError("Need to construct with either an int or a string, not: "+str(value))
        self.loc = [0,0]
        self.rect = None
    def __repr__(self):
        return self.name
    def draw(self, screen):
        color = (0,0,255)
        if self.name == "yellow":
            color = (255,255,0)
        elif self.name == "red":
            color = (255,0,0)
        elif self.name == "black":
            color = (50,50,50)
        elif self.name == "teal":
            color = (0,204,204)
        try:
            self.rect = pygame.Rect(self.loc[0] - self.width/2, self.loc[1] - self.width/2, self.width, self.width)
            pygame.draw.rect(screen, color, self.rect)
        except Exception:
            # Pygame either doesn't exist or isn't set up properly.
            print("Pygame not loaded, ignoring!")
def ConvertNameToValue(name):
    value = -1
    if name == "blue":
        value = 0
    elif name == "yellow":
        value = 1
    elif name == "red":
        value = 2
    elif name == "black":
        value = 3
    elif name == "teal":
        value = 4
    else:
        raise ValueError("Need to provide a reasonable string to be parsed! "+value+" does not count!")
    return value
def ConvertValueToName(value):
    assert type(value) == int and 5 > value >= 0, "Value must be from 0-4"
    name = ""
    if value == 0:
        name = "blue"
    elif value == 1:
        name = "yellow"
    elif value == 2:
        name = "red"
    elif value == 3:
        name = "black"
    elif value == 4:
        name = "teal"
    return name