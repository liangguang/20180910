#encoding:utf8

import threading
import time

data = 0

def func(sleeptime):
    global data
    print(threading.currentThread().getName())
    time.sleep(sleeptime)
threads = []

for i in range(0,40):
    t = threading.Thread(target=func,args=(i,))
    threads.append(t)

num = 0
for t in threads:
    t.start()
    while True:
        #判断正在运行的线程数量,如果小于5则退出while循环,
        #进入for循环启动新的进程.否则就一直在while循环进入死循环
        if(len(threading.enumerate()) < 5):
            break
