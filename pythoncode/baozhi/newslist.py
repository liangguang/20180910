# -*- coding: utf-8 -*-

import urllib.request,time,random
from urllib.request import quote
import re,requests
import os,traceback
import threading
from bs4 import BeautifulSoup


selfheaders = {
		'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
}

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
    try:
        response = requests.get(base_url,headers=headers, timeout=30)
        response.encoding = encoding
        html = response.text
        soup = BeautifulSoup(html,'lxml')
        return (html,soup)
    except :
        pass
    time.sleep(random.choice(range(3,8)))

def downloadImg(imgurl,path):
    try:
        if not os.path.exists(path):
            r = requests.get(imgurl)
            r.raise_for_status()
            #使用with语句可以不用自己手动关闭已经打开的文件流
            with open(path,"wb") as f: #开始写文件，wb代表写二进制文件
                f.write(r.content)
                print('下载'+path+'完成')
        else:
            print(path + "文件已存在")
    except:
        pass
    time.sleep(random.choice(range(3,8)))

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
        time.sleep(random.choice(range(3,8)))

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
    list_news = []
    if l_u[0] not in os.listdir():
	        os.mkdir(l_u[0])
    ret = getHtml(l_u[1],headers,'gb2312')
    html = ret[0]
    writeText(html,l_u[0]+'/index.html','utf8')
    soup = ret[1]
    table = soup.find('table',class_='list-table')    
    trs = table.select('tr')
    for tr in trs:
        tds = tr.find_all('td')
        if not tds or len(tds) < 3:
            continue
        title = tds[1].a.text
        a_urls =  tds[2].find_all('a')
        list_news.append((title,a_urls[0].get('href'),a_urls[2].get('href'),l_u[0]))
        #break
    return list_news    


def parseNews(list_news):
    max = 0
    for news in list_news:
        if max > 20:
            continue
        max = max + 1
        try:    
            title = news[0]
            news_url = news[1]
            img_url = news[2]
            top_name = news[3]
            print(title+'|'+news_url+'|'+img_url)
            ret = getHtml(news_url,selfheaders,'utf8')
            if not os.path.exists(top_name +'/'+title):
                os.makedirs(top_name +'/'+title)
            writeText(ret[0],top_name+'/'+title+'/index_news.html','utf8')
            soup = ret[1]
            new_titles = soup.find_all('h3',class_='c-title')
            list_nts = []
            for new_title in new_titles:
                list_nts.append(new_title.a.get('href'))
            ret = getHtml(img_url,selfheaders,'utf8')       
            writeText(ret[0],top_name +'/'+title+'/index_img.html','utf8')
            pattern  = re.compile('\"objURL\":\"http.+?g\"')
            imgs = pattern.findall(ret[0])
            donwloadImgs = []
            for img in imgs:
                if img.find('?') > 0:
                    continue
                donwloadImgs.append(img[img.find(':') + 2:img.rfind('"')])
            downloadNews(list_nts,donwloadImgs,top_name +'/'+title)
        except:
            traceback.print_exc()
            pass 
        #break
def downloadNews(list_nts,downloadImgs,title):
    for n in range(len(list_nts)):
        if n > 5:
            continue
        ret = getHtml(list_nts[n],selfheaders,'utf-8')
        writeText(ret[0],title+'/'+str(n)+'.html','utf8')
        
    for n in range(len(downloadImgs)):
        img = downloadImgs[n]
        if n > 10:
            continue
        #print(img + '|'+ img[img.rfind('/') + 1:])
        downloadImg(img,title+'/'+img[img.rfind('/') + 1:])

def run():
    print('start......')
    list_u = getTop()
    for l_u in list_u:
        list_news = paserTop(l_u)
        parseNews(list_news)
        #downloadNews(list_download[0],list_download[1],list_download[2]) 
        #break


if __name__ == '__main__':	
    run()

