import sys

file = "FASTBot.log"
if len(sys.argv) > 1:
    file = sys.argv[1]
f = open(file)

values = [eval(line.split(':')[-1]) for line in f][1:]

print("First: ", values[0])

values = values[1:]

print("Count: ", len(values))
print("Mean:  ", sum(values) / len(values))
print("Median:", sorted(values)[len(values) // 2])
print("Min:   ", min(values))
print("Max:   ", max(values))

