# -*- coding: utf-8 -*-

import urllib.request
from urllib.request import quote
import re,requests
import os,traceback
import threading
from bs4 import BeautifulSoup

def donwload(url, i):
    print(url)

def runThreads():
    thread_list = []
    for i in range(1, 10):
        thread_list.append(threading.Thread(target=donwload, args=('', i,)))
    for t in range(0,len(thread_list)):
        thread_list[t].start()
        print("No." + str(t) + " thread start")
        while True:
            #判断正在运行的线程数量,如果小于10则退出while循环,
            #进入for循环启动新的进程.否则就一直在while循环进入死循环
            if len(threading.enumerate()) < 10:
                break


def getHtml(base_url,headers,encoding):
	response = requests.get(base_url,headers=headers, timeout=30)
	response.encoding = encoding
	html = response.text
	soup = BeautifulSoup(html,'lxml')
	return (html,soup)

def writeText(text, path,encoding):
        try:
           with open(path, 'w',encoding=encoding) as file:
              file.write(text)
              file.flush()
        except Exception:
           traceback.print_exc()
           print(path+'下载失败')
        else:
           print(path + '下载成功')

def getTop():
    url = 'http://top.baidu.com/category?c=513&fr=topbuzz_b1_c513'
    headers = {
			'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
	}
    response = requests.get(url,headers=headers, timeout=30)
    response.encoding = 'gb2312'
    html = response.text
    soup = BeautifulSoup(html,'lxml')
    div = soup.find('div',id='flist')
    lis = div.find_all('li')
    list_url = []
    for li in lis:
        title = li.a.get('title')
        href = li.a.get('href').strip()
        if href.startswith('.'):
            list_url.append((title, href.replace('.','http://top.baidu.com')))
    return list_url

def paserTop(l_u):
    headers = {
		'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
	}
    
    if l_u[0] not in os.listdir():
	        os.mkdir(l_u[0])
    ret = getHtml(l_u[1],headers,'gb2312')
    html = ret[0]
    writeText(html,l_u[0]+'/index.html','gb2312')
    soup = ret[1]
    table = soup.find('table',class_='list-table')    
    trs = table.select('tr')
    for tr in trs:
        tds = tr.find_all('td')
        if not tds:
            continue
        a_urls =  tds[2].find_all('a')
        print(a_urls[0].text + '|' +a_urls[0].get('href'))
        print(a_urls[2].text + '|' +a_urls[2].get('href'))
        break

            

def run():
    print('start......')
   
    list_u = getTop()
    for l_u in list_u:
        paserTop(l_u)
        break

if __name__ == '__main__':	
    run()

