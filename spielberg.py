#author: Jonas Hinge

import sys
import csv
import random

sys.setrecursionlimit(50000)


# Contraction algorithm
def contraction_algorithm(G):
	#for actor in G:
		#G[actor][2].append(actor)
	print len(G)
	if(len(G) == 2):
		return G

	else:
		ran_u = random.choice(G.keys())
		while len(G[ran_u][1]) < 1:
			print 'trying to find ran_u'
			ran_u = random.choice(G.keys())
		ran_v = random.choice(G[ran_u][1])
		while ran_v not in G:
			print 'trying to find ran_v'
			ran_v = random.choice(G[ran_u][1])
		G[ran_u][2].append(ran_v)
		del G[ran_v]
		#ran_v = G[ran_u][2][]
		#print ran_u, ran_v
		contraction_algorithm(G)
	return 0
		






# building the graph
graph = {}
with open('spielberg.csv','rb') as csvfile:
	reader = csv.reader(csvfile, delimiter=';')
	for row in reader:
		if(not(row[0][0] == "#")):
			movie_id = row[1]
			actor_id = row[6]
			graph[actor_id] = (movie_id, [], [actor_id])


for actor in graph:
	for act in graph:
		if(graph[actor][0] == graph[act][0] and actor != act):
			graph[actor][1].append(act)


print contraction_algorithm(graph)


