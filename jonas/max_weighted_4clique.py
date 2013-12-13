import pickle
import sys
import MySQLdb as mdb
from pprint import pprint
import time

USE_CACHE = False

SQL_GET_ALL_MOVIES = """
	SELECT id FROM movies
	WHERE year >= 2005
"""

SQL_GET_ROLES_OF_MOVIE = """
	SELECT actor_id, first_name, last_name
	FROM imdb.movies movies 
	LEFT OUTER JOIN imdb.roles roles ON movies.id = roles.movie_id 
	LEFT OUTER JOIN imdb.actors actors ON actors.id = roles.actor_id 
	WHERE actors.id IS NOT NULL AND movies.id = %d
"""

def build_graph():
	adj = {}
	edges = 0
	con = mdb.connect('127.0.0.1', 'root', 'geekchamp3!', 'imdb')
	with con:
		cur = con.cursor()
		cur.execute(SQL_GET_ALL_MOVIES)
		rows = cur.fetchall()
		for row in rows:
			movie_id = long(row[0])
			cur_two = con.cursor()
			roles = SQL_GET_ROLES_OF_MOVIE % int(movie_id)
			cur_two.execute(roles)
			rows_two = cur_two.fetchall()
			for rt in rows_two:
				actor = str(long(rt[0])) + "_" + str(rt[1]) + "_" + str(rt[2])
				for rt_other in rows_two:
					actor_other = str(rt_other[0]) + "_" + str(rt_other[1]) + "_" + str(rt_other[2])
					if(actor != actor_other):
						#print "check for actor: " + actor + " against: " + actor_other + " in movie with id: " + str(movie_id)
						if(actor in adj):
							# check if other actor is already present and increment
							if(actor_other in adj[actor]):
								adj[actor][actor_other] += 1
							else:
								adj[actor][actor_other] = 1
						else:
							adj[actor] = {actor_other: 1}
						edges += 1
	return adj, edges/2


def naive_max_edgeweighted_4clique(G):
	current_clique = ((),-1)
	cliques_total = {}
	for v, v_neighbours in G.iteritems():
		for u, u_wt in v_neighbours.iteritems():
			for w, w_wt in v_neighbours.iteritems():
				if(w in G[u]):
					for z, z_wt in v_neighbours.iteritems():
						if(z in G[w] and z in G[u] and z != w and z != u):
							update_discovered(cliques_total,v,u,w,z,get_clique_weight(G,v,u,w,z))
							if(get_clique_weight(G,v,u,w,z) > current_clique[1]):
								current_clique = ((v,u,w,z),get_clique_weight(G,v,u,w,z))
								print "Found a clique higher: " + "v: " + v + ", u: " + u + ", w: " + w + ", z: " + z
								print "Weight of clique: " + str(get_clique_weight(G,v,u,w,z))
	return cliques_total


def optimized_max_edgeweighted_4clique(G):
	current_clique = ((),-1)
	cliques_total = {}
	print "Counting triangles..."
	triangles, cliques = node_iterator_plus_plus_w_ids(G);
	#triangles, cliques = node_iterator(G);
	print "Finished counting triangles"
	for v,u,w in triangles:
		v_adj,u_adj,w_adj = G[v].keys(),G[u].keys(),G[w].keys()
		intersection_list = intersect(G, v_adj, u, w)
		if(len(intersection_list) > 0):
			for z in intersection_list:
				weight = get_clique_weight(G,v,u,w,z)
				update_discovered(cliques_total,v,u,w,z,weight)
				if(weight > current_clique[1]):
					print "Found a clique higher: " + "v: " + v + ", u: " + u + ", w: " + w + ", z: " + z
					print "Weight of clique: " + str(weight)
					current_clique = ((v,u,w,z),weight)
	return cliques_total


def node_iterator_plus_plus_extension_4cliques(G):
	current_clique = ((),-1)
	cliques_total = {}
	run_count = 0
	for v, v_neighbours in G.iteritems():
		for u, u_wt in v_neighbours.iteritems():
			#if(v.split("_")[0] > u.split("_")[0]):
			if(higher_degree(G,u,v)):
				for w, w_wt in v_neighbours.iteritems():
					#if(u.split("_")[0] > w.split("_")[0]):
					if(higher_degree(G,w,u)):
						if(w in G[u]):
							for z, z_wt in v_neighbours.iteritems():
								#if(w.split("_")[0] > z.split("_")[0]):
								if(higher_degree(G,z,w)):
									run_count += 1
									if(z in G[w] and z in G[u]):
										update_discovered(cliques_total,v,u,w,z,get_clique_weight(G,v,u,w,z))
										if(get_clique_weight(G,v,u,w,z) > current_clique[1]):
											current_clique = ((v,u,w,z),get_clique_weight(G,v,u,w,z))
											print "Found a clique higher: " + "v: " + v + ", u: " + u + ", w: " + w + ", z: " + z
											print "Weight of clique: " + str(get_clique_weight(G,v,u,w,z))
	print "Run count: " + str(run_count)
	return cliques_total


# TRIANGLE COUNTING

def node_iterator(G):
	triangles = []
	cliques_total = {}
	for v, v_neighbours in G.iteritems():
		for u, u_wt in v_neighbours.iteritems():
			for w, w_wt in v_neighbours.iteritems():
				if(w in G[u]):
					triangles.append((v,u,w))
					update_discovered(cliques_total,v,u,w,"0_0",-1)
					#unique_key = int(v)+int(u)+int(w)
					#triangles[unique_key] = 0

	return triangles, cliques_total

'''def node_iterator_plus_plus(G):
	triangles = []
	cliques_total = {}
	for v, v_neighbours in G.iteritems():
		for u, u_wt in v_neighbours.iteritems():
			if(len(G[u]) >= len(v_neighbours)):
				for w, w_wt in v_neighbours.iteritems():
					if(len(G[w]) >= len(G[u])):
						if(w in G[u]):
							triangles.append((v,u,w))
							update_discovered(cliques_total,v,u,w,"0_0",-1)
							#unique_key = int(v)+int(u)+int(w)
							#triangles[unique_key] = 0

	return triangles, cliques_total'''

def node_iterator_plus_plus_w_ids(G):
	triangles = []
	cliques_total = {}
	for v, v_neighbours in G.iteritems():
		for u, u_wt in v_neighbours.iteritems():
			#if(u.split("_")[0] > v.split("_")[0]):
			if(higher_degree(G,u,v)):
				for w, w_wt in v_neighbours.iteritems():
					#if(w.split("_")[0] > u.split("_")[0]):
					if(higher_degree(G,w,u)):
						if(w in G[u]):
							triangles.append((v,u,w))
							update_discovered(cliques_total,v,u,w,"0_0",-1)
							#unique_key = int(v)+int(u)+int(w)
							#triangles[unique_key] = 0

	return triangles, cliques_total

# HELPERS
def get_clique_weight(adj,v,u,w,z):
	# median
	aux = [adj[v][u],adj[v][w],adj[v][z],adj[u][w],adj[u][z],adj[w][z]]
	aux.sort()
	return (aux[2]+aux[3]) / 2


'''def intersect(a, b):
    """ return the intersection of two lists """
    return list(set(a) & set(b))'''

def intersect(adj, a, b_key, c_key):
    """ return the intersection of two lists """
    intersection = []
    for i in a:
    	if(i in adj[b_key] and i in adj[c_key]):
    		intersection.append(i)
    return intersection

# higher degree heuristic
def higher_degree(G,u,v):
	if(len(G[u]) > len(G[v])):
		return True
	elif(len(G[u]) == len(G[v]) and u.split("_")[0] > v.split("_")[0]):
		return True
	else:
		return False

def update_discovered(D,v,u,w,z,weight):
	id_aux = [int(v.split("_")[0]), int(u.split("_")[0]), int(w.split("_")[0]), int(z.split("_")[0])]
	id_aux.sort()
	clique_key = str(id_aux[0]) + "_" + str(id_aux[1]) + "_" + str(id_aux[2]) + "_" + str(id_aux[3])
	#clique_key = int(v.split("_")[0]) + int(u.split("_")[0]) + int(w.split("_")[0]) + int(z.split("_")[0])
	if(clique_key in D):
		D[clique_key] = (D[clique_key][0]+1, D[clique_key][1], D[clique_key][2])
	else:
		D[clique_key] = (1, weight, (v,u,w,z))



# RUN

# build graph
if not USE_CACHE:
	print "Building graph from DB..."
	graph,edges = build_graph();
	with open('cache.pickle', 'wb') as handle:
		pickle.dump(graph, handle)
else:
	print "Building graph from cache..."
	with open('cache.pickle', 'rb') as handle:
		graph,edges = pickle.load(handle)
print "Finished building graph"
#pprint(graph)

#(2, -1, ('58423_Daniel_Brochu', '543807_Sonja_Ball', '123290_Bruce_Dinsmore', '0_0'))
# test graph
#pprint(graph["58423_Daniel_Brochu"])
#pprint(graph["543807_Sonja_Ball"])

print "n = " + str(len(graph))
print "m = " + str(edges)

print "Running algorithm..."

start_time = time.time()

# NAIVE
#cliques = naive_max_edgeweighted_4clique(graph)

# OPTIMIZED
#cliques = optimized_max_edgeweighted_4clique(graph)

# OPTIMAL
cliques = node_iterator_plus_plus_extension_4cliques(graph)

print "Algorithm execution time: " + str(time.time() - start_time)

# TESTING TRIANGLE COUNTING
#triangles, cliques = node_iterator(graph)
#triangles, cliques = node_iterator_plus_plus(graph)
#triangles, cliques = node_iterator_plus_plus_w_ids(graph)


'''for t,t_v in cliques.iteritems():
	if(t_v[0] != 6):
		print t_v'''
#pprint(cliques)
print "Number of cliques: " + str(len(cliques))








