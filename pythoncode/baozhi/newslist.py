# -*- coding: utf-8 -*-

import urllib.request
from urllib.request import quote
import re
import os
import threading

def donwload(url, i):
	print(url + str(i))
	os.system('you-get' + ' --debug ' + url + str(i))

url = 'http://huaban.com/boards/favorite/design/'
thread_list = []
for i in range(1, 10):
    thread_list.append(threading.Thread(target=donwload, args=(url, i,)))
for t in range(0,len(thread_list)):
    thread_list[t].start()
    print("No." + str(t) + " thread start")
    while True:
        #判断正在运行的线程数量,如果小于10则退出while循环,
        #进入for循环启动新的进程.否则就一直在while循环进入死循环
        if len(threading.enumerate()) < 10:
            break	

