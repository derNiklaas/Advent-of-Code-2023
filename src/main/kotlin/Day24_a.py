import sympy


class Hailstone:
    def __init__(self, position_x, position_y, position_z, velocity_x, velocity_y, velocity_z):
        self.position_x = position_x
        self.position_y = position_y
        self.position_z = position_z
        self.velocity_x = velocity_x
        self.velocity_y = velocity_y
        self.velocity_z = velocity_z

        self.a = velocity_y
        self.b = -velocity_x
        self.c = velocity_y * position_x - velocity_x * position_y


hailstones = [Hailstone(*map(int, line.replace("@", ",").split(","))) for line in open("src/main/resources/Day24.txt")]

total = 0

for i, hs1 in enumerate(hailstones):
    for hs2 in hailstones[:i]:
        px, py = sympy.symbols("px py")
        answers = sympy.solve([hs.velocity_y * (px - hs.position_x) - hs.velocity_x * (py - hs.position_y) for hs in [hs1, hs2]])
        if not answers:
            continue
        x, y = answers[px], answers[py]
        if 200000000000000 <= x <= 400000000000000 and 200000000000000 <= y <= 400000000000000:
            if all((x - hs.position_x) * hs.velocity_x >= 0 and (y - hs.position_y) * hs.velocity_y >= 0 for hs in [hs1, hs2]):
                total += 1

print(total)
