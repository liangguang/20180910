# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime, time, traceback , random
from contextlib import closing
from urllib.request import urlopen
from bs4 import BeautifulSoup
import schedule, threading
 #获取main 所在目录
parentdir=os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
 #把目录加入环境变量
sys.path.insert(0,parentdir)
import synonym


def parseUrl(newsUrl):
    print(newsUrl)
    


def getNewList(url):
    print(url)
    headers = {
		'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
	}
    r = synonym.getHtml(url,headers)
    result = r[0] 
    newList = result[result.find('ent:') + 5:-3]
    #json.loads(newList)
    pattern = re.compile(r'http://ent.163.com/.*?\.html')
    result = pattern.findall(newList)
    return result
    #print(result)

if __name__ == '__main__':
    newslist = getNewList('http://ent.163.com/special/00032IAD/ent_json.js')
    for url in newslist:
        parseUrl(url)

    