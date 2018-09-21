# -*- coding: utf-8 -*-

def readFileOne(file):
    with open(file) as f:
        line = f.readline()
        while line:
            print(line)
            line = f.readline()

def readFileTwo(file):
    with open(file) as f:
        lines = f.readlines()
        for i in range(0,len(lines),2):
            print(lines[i]+':'+lines[i+1])

readFileTwo('ykaddr.txt')
        