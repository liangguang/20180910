# -*- coding: utf-8 -*-

import requests, traceback
import re,threading
import os
from bs4 import BeautifulSoup

def getHTMLText(url):
	print(url)
	try:
		r = requests.get(url,timeout=30)
		r.raise_for_status()
		r.encoding = r.apparent_encoding
		return r.text
	except:
		print("")

def downloadImgs(nameUrl):
    name = nameUrl[nameUrl.rfind('/') + 1:nameUrl.rfind('.')]
    root = 'enterdesk/'+name
    if not os.path.exists(root):
        os.mkdir(root)
    #print(nameUrl+'|'+root)
    html = getHTMLText(nameUrl)
    #print(html)
    soup_p = BeautifulSoup(html,'lxml')
    #print(soup_p)
    #return
    tab = soup_p.find('div',class_='swiper-wrapper')
    hrefs = tab.select('div')
    num = 0
    for href in hrefs:
        num = num + 1
        #imgurl = href.a.img.get('data-original')
        imgurl = href.a.get('href')
        title = href.a.img.get('title')
        html=getHTMLText('https://mm.enterdesk.com'+imgurl)
        #print(html)
        soup_p = BeautifulSoup(html,'lxml')
        #print(soup_p)
        img = soup_p.find('img',class_='arc_main_pic_img')
        imgurl = img.get('src')
        print('图片下载地址：{}'.format(imgurl))
        try:
            path = os.path.join(root,title)+str(num)+'.jpg'
            downloadOne(imgurl,path)
        except:
            traceback.print_exc()
            print('下载{}的图片{}异常'.format(name,title))
            
def downloadOne(imgurl,path):
    
    if not os.path.exists(path):
        r = requests.get(imgurl)
        r.raise_for_status()
        #使用with语句可以不用自己手动关闭已经打开的文件流
        with open(path,"wb") as f: #开始写文件，wb代表写二进制文件
            f.write(r.content)
            print('下载'+path+'完成')
    else:
        print(path + "文件已存在") 

def getPageUrls(text,name):
    re_pageUrl=r'href="(.+)">\s*<img src="(.+)" alt="'+name
    return re.findall(re_pageUrl,text)


def downPictures(text,root,name):
    pageUrls=getPageUrls(text,name)  
    titles=re.findall(r'alt="'+name+r'(.+)" ',text)
    for i in range(len(pageUrls)):
        pageUrl=pageUrls[i][0]
        path = root + titles[i]+ "//"
        if not os.path.exists(path):
            os.mkdir(path)
        if not os.listdir(path):             
            pageText=getHTMLText(pageUrl)
            totalPics=int(re.findall(r'<em>(.+)</em>）',pageText)[0])
            downUrl=re.findall(r'href="(.+?)" class="">下载图片',pageText)[0]
            cnt=1
            while(cnt<=totalPics):
                picPath=path+str(cnt)+".jpg"
                r=requests.get(downUrl)
                with open(picPath,'wb') as f:
                    f.write(r.content)
                    f.close()
                print('{} - 第{}张下载已完成\n'.format(titles[i],cnt))
                cnt+=1                        
                nextPageUrl=re.findall(r'href="(.+?)">下一张',pageText)[0]
                pageText=getHTMLText(nextPageUrl)
                downUrl=re.findall(r'href="(.+?)" class="">下载图片',pageText)[0]
    return

def getEnterdeskMM():
    urls = []
    for j in range(1,146):
        nameUrl = 'https://mm.enterdesk.com/'+str(j)+'.html'
        urls.append(nameUrl)
    if not os.path.exists('enterdesk'):
        os.mkdir('enterdesk')
    for url in urls:
        try:
            html=getHTMLText(url)
            soup_p = BeautifulSoup(html,'lxml')
            tab = soup_p.find('div',class_='egeli_pic_m')
            hrefs = tab.select('dd')
            for href in hrefs:
                nameUrl = href.a.get('href')
                #print(nameUrl)
                try:
                    downloadImgs(nameUrl)
                    #break
                except:
                    traceback.print_exc()
                    print('下载{}的图片异常'.format(nameUrl))
        except Exception:
            traceback.print_exc()
            print('解析{}异常'.format(url))
        #break

 
if __name__ == '__main__':
   getEnterdeskMM()