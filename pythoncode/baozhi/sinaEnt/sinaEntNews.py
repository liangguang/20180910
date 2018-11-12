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
import bdnlp
from SendMail import SendMail

headers = {
		'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
	}

def parseUrl(news):
    #print(news)
    url = news['url']
    title = news['title']
    title = re.sub(r'[\/:*?"<>|]', "", title)
    imgs = news['images']
    r = synonym.getHtml(url,headers,'utf-8')
    soup = r[1]
    if not os.path.exists(title):
        os.makedirs(title)

    synonym.downloadText(r[0],title + '/index.html','utf-8')
    content = soup.find('div',id='artibody')
    ps = content.find_all('p')
    text = ''
    for p in ps:
        text = text + p.text
    synonym.downloadText(text,title + '/src.txt','utf-8')
    #print(text)
    files = []
    for n in range(len(imgs)):
        img_url = imgs[n]['u']
        synonym.downloadImg(img_url, title + '/' + str(n) +'.jpg')
        files.append(title + '/' + str(n) +'.jpg')
    files.append(title + '/src.txt')
    synonym.saveUrl(url,text,'sina')
    return(title,text,files)
  


def getNewList(url):
    #print(url)
    r = synonym.getHtml(url,headers,'utf-8')
    result = json.loads(r[0])
    newList = result['result']['data']
    #print(len(newList))
    #print(newList[0])
    return newList

def getApi(url = 'http://ent.sina.com.cn/rollnews.shtml'):
    print(url)
    r = synonym.getHtml(url,headers,'utf-8')
    soup = r[1]
    channelList = soup.find('div',id='channelList')
    aF = channelList.find('a')
    pageid = aF.get('pageid')
    lid = aF.get('s_id')
    api = 'http://feed.mix.sina.com.cn/api/roll/get?pageid='+pageid+'&lid='+lid+'&k=&num=100&page=1'
    #print(api)
    return api

def run():
    newslist = getNewList(getApi())
    for newsjson in newslist:
        try:
            if newsjson['categoryid'] is not '1':
                continue
            r = synonym.getByUrl(newsjson['url'])
            #print(r)
            if r is not None: #没有事None
                continue
            news = parseUrl(newsjson)
            text = bdnlp.nplParse(news[1])
            synonym.downloadText(text,news[0] + '/dest.txt','utf-8')
            files = news[2]
            files.append(news[0] + '/dest.txt')
            SendMail.mail(SendMail,news[0],news[1] + '\n' + text,files)
        except:
            traceback.print_exc()
        #break

if __name__ == '__main__':
    run()

    