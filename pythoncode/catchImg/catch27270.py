#-*-coding:utf-8-*-

import os
import time
import json,threading
import re,traceback
import requests
import urllib3
import urllib
from bs4 import BeautifulSoup


basepath = os.path.abspath(os.path.dirname(__file__))  # 当前模块文件的根目录

headers = {
    'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36'
}

def setup_down_path(path):
    '''设置图片下载后的保存位置，所有图片放在同一个目录下'''
    down_path = os.path.join(basepath, path)
    if not os.path.isdir(down_path):
        os.mkdir(down_path)
    return down_path



def Schedule(blocknum, blocksize, totalsize):
    """
        blocknum:已经下载的数据块
        blocksize:数据块的大小
        totalsize:远程文件的大小
    """
    per = blocknum * blocksize / totalsize * 100.0
    if per > 100:
        per = 100
    print('当前下载进度： %.2f%%'%per)

def download_pic(url,path):
    name = url[url.rfind('/') + 1:url.rfind('.')]
    filename = path + '/{}.jpg'.format(name)
    print('开始下载:{}'.format(name))
    urllib.request.urlretrieve(url, filename, Schedule)#使用urllib.request.urlretrieve方法下载图片并返回当前下载进度
    time.sleep(1)

def download_page(url):
    try:
        res = requests.get(url, headers=headers)
        if res.status_code == 200:
            res.encoding = 'gb2312'
            return res.text
    except:
        traceback.print_exc()
        return None

def parseUrl(url,path):
    html = download_page(url)
    if not html:
       return False
    soup_p = BeautifulSoup(html,'lxml')
    picbox = soup_p.find('div',id='picBody')
    imgurl = picbox.find('img').get('src')
    download_pic(imgurl,path)
    page = soup_p.find('li',id='pageinfo')
    pageNum = page.get('pageinfo')
    for i in range(1,int(pageNum)):
       try:
          nexturl = url.replace('.html','_'+str(i+1)+'.html')
          html = download_page(nexturl)
          if not html:
             continue
          soup_p = BeautifulSoup(html,'lxml')
          picbox = soup_p.find('div',id='picBody')
          imgurl = picbox.find('img').get('src')
          download_pic(imgurl,path)
       except:
          traceback.print_exc()
          pass

 
def parsePage(url,path):
    html = download_page(url)
    if not html:
       return False
    soup_p = BeautifulSoup(html,'lxml')
    lis = soup_p.find_all('li',class_='wenshen')
    for li in lis:
        herf = li.a.get('href')
        title = li.a.get('title')
        folder_path = os.path.join(path,title)
        if not os.path.isdir(folder_path):
           os.mkdir(folder_path)
        try:
            parseUrl(herf,folder_path)
        except:
            traceback.print_exc()
            pass



if __name__ == '__main__':
    links = ['http://www.27270.com/word/dongwushijie/list_8_'+str(i)+'.html' for i in range(1,196)]
    setup_down_path('dongwu')
    threads = []
    for url in links:
        try:
            t = threading.Thread(target=parsePage,args=(url,'dongwu'))
            #t.setDaemon(True)#设置为后台线程，这里默认是False，设置为True之后则主线程不用等待子线程
            threads.append(t)
        except:
           traceback.print_exc()
           pass

    for t in threads:
        t.start()
        while True:
            print(len(threading.enumerate()))
            #判断正在运行的线程数量,如果小于5则退出while循环,
            #进入for循环启动新的进程.否则就一直在while循环进入死循环
            if(len(threading.enumerate()) < 5):
                break
            time.sleep(1)

        
