class Tile:
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
    def __repr__(self):
        return self.name
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