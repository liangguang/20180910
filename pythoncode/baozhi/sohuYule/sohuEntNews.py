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


def parseUrl(newsUrl):
    print(newsUrl)
    encoding = 'utf-8'
    if newsUrl.find('yule') > 0:
        encoding = 'gb2312'
        r = synonym.getHtml(newsUrl,headers,encoding)
    else:      
        r = synonym.getHtml(newsUrl,headers,encoding)
    soup = r[1]
    title = soup.find('title')
    title =re.sub(r'[\/:*?"<>|]', "", title.text)
    if not os.path.exists(title):
        os.makedirs(title)
    synonym.downloadText(r[0],title + '/index.html',encoding)
    if newsUrl.find('yule') > 0:
        content = soup.find('div',id='contentText')
    else:
        content = soup.find('article',id='mp-editor')
    #print(content.text)
    imgs = content.select('img')
    synonym.downloadText(content.text,title + '/src.txt',encoding)
    files = []
    for n in range(len(imgs)):
        url = imgs[n]['src']
        if not url.startswith('http'):
            url = 'http:'+ url
        synonym.downloadImg(url, title + '/' + str(n) +'.jpg')
        files.append(title + '/' + str(n) +'.jpg')
    files.append(title + '/src.txt')
    synonym.saveUrl(newsUrl,content.text,'sohu')
    return(title,content.text,files)



def getNewList(url):
    #print(url)
    r = synonym.getHtml(url,headers,'utf-8')
    result = r[0] 
    newList = result[result.find('item:') + 5:-1]
    listsohu = json.loads(newList)
    result = []
    for s in listsohu:
        #print(s[2])
        result.append(s[2])
    return result
    #print(result)

def getToday():
    now = int(time.time()) 
    #转换为其他日期格式,如:"%Y%m%d %H:%M:%S" 
    timeStruct = time.localtime(now) 
    strTime = time.strftime("%Y%m%d", timeStruct) 
    #print(strTime)
    return strTime

def run():
    newslist = getNewList('http://yule.sohu.com/_scroll_newslist/%s/news.inc' %(getToday()))
    for url in newslist:
        try:
            if url.find('picture') > 0 : #组图 or url.find('music') > 0
                continue
            r = synonym.getByUrl(url)
             #print(r)
            if r is not None: #没有是None
                continue
            news = parseUrl(url)
            text = bdnlp.nplParse(news[1])
            synonym.downloadText(text,news[0] + '/dest.txt','utf-8')
            files = news[2]
            files.append(news[0] + '/dest.txt')
            SendMail.mail(SendMail,news[0],news[1] + '\n' + text,files)
        except:
            traceback.print_exc()
            pass
        #break

if __name__ == '__main__':
    run()

    