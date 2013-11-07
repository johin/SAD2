#Author: Jesper Jensen - jejen@itu.dk
#Imports
import time
import os
import sys

#Variabels
linkedList = []


#Classes
class Node: 
  def __init__(self, cargo=None, next=None): 
    self.car = cargo 
    self.cdr = next    
  def __str__(self): 
    return str(self.car)

#Functions
def importData(fileName1, fileName2):
	global linkedList
	with open(fileName1, 'r') as f1:
		for line in f1:
			if not(line.startswith('#')):
				if(line.spilt('\t').length < 2):
					line.split(' ')
				

				tNode = Node(line.split('\t')[0], line.split('\t')[1])
				linkedList.append(tNode)


