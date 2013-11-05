#author : Jonas Hinge

import sys
import csv
import random
import time

DEBUG_LOG = 0

def random_line(mov_list):
	# 10000 to make sure we dont search forever
	for i in range(0,10000):
		ran_num = random.randint(0,len(mov_list)-1)
		if(DEBUG_LOG): print 'checking: ' + str(mov_list[ran_num])
		if(mov_list[ran_num][0] >= 8):
			global random_line_total
			random_line_total += i
			return True
	# no movies with rank 8 or above
	return False

def line_after_line(mov_list):
	ran_num = random.randint(0,len(mov_list)-1)
	for i in range(0,len(mov_list)):
		# end of list, start from beginning
		if(ran_num+i > len(mov_list)):
			ran_num = 0
		if(DEBUG_LOG): print 'checking: ' + str(mov_list[ran_num+i])
		if(mov_list[ran_num+i][0] >= 8):
			global line_after_line_total
			line_after_line_total += i
			return True
	# no movies with rank 8 or above
	return False


horror_movies = []

with open('horror_movies_random.csv','rb') as csvfile:
	reader = csv.reader(csvfile)
	for row in reader:
		horror_movies.append( (float(row[0].split(';')[0]), row[0].split(';')[1]) )


# perform the search
line_after_line_total = 0
start = time.clock()
for j in range(0,20):
	if(line_after_line(horror_movies)):
		if(DEBUG_LOG): print 'found a movie!'
print 'WITH MEMORY:'
print 'avg number of lines: ' + str(line_after_line_total/20)
print 'time: ' + str(time.clock() - start)

print ''

random_line_total = 0
start = time.clock()
for j in range(0,20):
	if(random_line(horror_movies)):
		if(DEBUG_LOG): print 'found a movie!'
print 'WITHOUT MEMORY:'
print 'avg number of lines: ' + str(random_line_total/20)
print 'time: ' + str(time.clock() - start)


