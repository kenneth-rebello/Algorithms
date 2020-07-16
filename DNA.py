#!/bin/python3

import math
import os
import random
import re
import sys
from collections import deque
Trie = []

def find_next(current, value):
    for node in Trie[current]['next_states']:
        if Trie[node]['value'] == value:
            return node
    return None

def add_keyword(keyword):
    current_state = 0
    j = 0
    child = find_next(current_state, keyword[j])
    while child != None:
        current_state = child
        j += 1
        if j < len (keyword):
            child = find_next(current_state, keyword[j])
        else:
            break
    for i in range(j,len(keyword)):
        node = {'value':keyword[i],'next_states':[],'fail_state':0,'output':[], 'index':i}
        Trie.append(node)
        Trie[current_state]["next_states"].append(len(Trie)-1)
        current_state = len(Trie)-1

    Trie[current_state]['output'].append(keyword)

def set_fail_states():
    q = deque()
    child = 0
    for node in Trie[0]['next_states']:
        Trie[node]['fail_state'] = 0
        q.append(node)
    
    while q:
        r = q.popleft()
        for child in Trie[r]['next_states']:
            q.append(child)

            state = Trie[r]['fail_state']
            while find_next(state,Trie[child]['value']) == None and state!=0:
                state = Trie[state]['fail_state']

            Trie[child]['fail_state'] = find_next(state,Trie[child]['value'])

            if Trie[child]['fail_state'] == None:
                Trie[child]['fail_state'] = 0

            Trie[child]['output'] = Trie[child]['output'] + Trie[Trie[child]['fail_state']]['output']

def init_tree(keywords):
    Trie.append({'value':'', 'next_states':[],'fail_state':0,'output':[],'index':-1})
    for keyword in keywords:
        add_keyword(keyword)
    set_fail_states()


f = open("TestCase1.txt", "r")
n = int(f.readline())
code = []
genes = f.readline().rstrip().split()
health = list(map(int, f.readline().rstrip().split()))
init_tree(list(set(genes)))
top = 0
bottom = float('inf')

s = int(f.readline())

healthMap = {}
for k in range(len(genes)):
    if genes[k] in healthMap:
        healthMap[genes[k]][k] = health[k]
    else:    
        healthMap[genes[k]] = [0]*len(genes)
        healthMap[genes[k]][k] = health[k]

count = 0
for s_itr in range(s):
    firstLastd = f.readline().split()
    first = int(firstLastd[0])
    last = int(firstLastd[1])
    d = firstLastd[2]
    temp_g = genes[first:last+1]
    temp_h = health[first:last+1]
    
    res = []
    current_state = 0
    benefit = 0
    count += 1
    print(count)
    for i in range(len(d)):
        while find_next(current_state, d[i]) is None and current_state!=0:
            current_state = Trie[current_state]['fail_state']
        current_state = find_next(current_state, d[i])

        if current_state is None:
            current_state = 0
        else:
            for j in Trie[current_state]['output']:
                res.append(j)

    for gene in set(res):
        benefit += res.count(gene) * sum(healthMap[gene][first:last+1])
         

    if benefit > top:
        top = benefit
    elif benefit < bottom:
        bottom = benefit
    
    
print(str(bottom)+" "+str(top))
