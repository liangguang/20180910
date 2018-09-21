# -*- coding: utf-8 -*-

# -*- coding: utf-8 -*-

import urllib.request
import re
import os
import threading

def donwload(url, i):
	print(url + str(i))
	os.system('you-get' + ' --debug ' + url + str(i))

url = 'http://www.qutuu.cn/tags/概念设计/'
thread_list = []
for i in range(1, 10):
    thread_list.append(threading.Thread(target=donwload, args=(url, i,)))
for t in range(0,len(thread_list)):
    thread_list[t].start()
    print("No." + str(t) + " thread start")
    while True:
        if len(threading.enumerate()) < 10:
            break	
			
#if __name__ == '__main__':
#	url = input('输入地址')
#	donwload(url)	
#data = 'http://v.youku.com/v_show/id_XMzgxNDM3MDgwOA==.html'
#os.system('you-get' + ' ' + data )