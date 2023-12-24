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

xr, yr, zr, vxr, vyr, vzr = sympy.symbols("xr, yr, zr, vxr, vyr, vzr")

equations = []
answers = []

for i, hs in enumerate(hailstones):
    equations.append((xr - hs.position_x) * (hs.velocity_y - vyr) - (yr - hs.position_y) * (hs.velocity_x - vxr))
    equations.append((yr - hs.position_y) * (hs.velocity_z - vzr) - (zr - hs.position_z) * (hs.velocity_y - vyr))
    if i < 2:
        continue
    answers = [soln for soln in sympy.solve(equations) if all(x % 1 == 0 for x in soln.values())]
    if len(answers) == 1:
        break

answer = answers[0]

print(answer[xr] + answer[yr] + answer[zr])
