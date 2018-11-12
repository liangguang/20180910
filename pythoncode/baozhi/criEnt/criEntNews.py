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
    #print(newsUrl)
    r = synonym.getHtml(newsUrl,headers,'utf-8')
    soup = r[1]
    title = soup.find('title')
    #print(title.text)
    title =re.sub(r'[\/:*?"<>|]', "", title.text)
    if not os.path.exists(title):
        os.makedirs(title)
    synonym.downloadText(r[0],title + '/index.html','utf-8')
    content = soup.find('div',id='abody')
    #print(content.text)
    imgs = content.select('img')
    synonym.downloadText(content.text,title + '/src.txt','utf-8')
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
    baseurl = 'http://ent.cri.cn'
    print(url)
    r = synonym.getHtml(url,headers,'utf-8')
    soup = r[1] 
    #print(soup)
    body = soup.find_all('ul')
    #print(body[1])
    lis = body[1].find_all('li')
    result = []
    #print(lis[0])
    for s in lis:
        a = s.find('a')
        result.append(baseurl + a.get('href'))
    return result
    #print(result)

def run(cat):
    try:
        newslist = getNewList('http://ent.cri.cn/roll/' + cat)
        for url in newslist:
            try:
                if url.find('picture') > 0: #组图
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
    except:
        pass    
    

if __name__ == '__main__':
    cats = ['cri/','bb/']
    for cat in cats:
        run(cat)

    