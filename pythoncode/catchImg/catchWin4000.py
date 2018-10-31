# -*- coding: utf-8 -*-

import requests, traceback
import re
import os
from pypinyin import pinyin, lazy_pinyin
from bs4 import BeautifulSoup

def getHTMLText(url):
	try:
		r = requests.get(url,timeout=30)
		r.raise_for_status()
		r.encoding = r.apparent_encoding
		return r.text
	except:
		print("")

def downloadImgs(nameUrl):
    name = nameUrl[nameUrl.rfind('/') + 1:nameUrl.rfind('.')]
    root = 'Win4000/'+name
    if not os.path.exists(root):
        os.mkdir(root)
    print(nameUrl+'|'+root)
    html=getHTMLText(nameUrl)
    soup_p = BeautifulSoup(html,'lxml')
    tab = soup_p.find('div',class_='Left_bar')
    hrefs = tab.select('li')
    for href in hrefs:
        imgurl = href.a.img.get('data-original')
        #imgurl = href.a.img.get('src')
        title = href.a.img.get('title')
        path = os.path.join(root,title)+'.jpg'
        try:
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


def main():  
    name=input("请输入你喜欢的明星的名字:")
    nameUrl="http://www.win4000.com/mt/"+''.join(lazy_pinyin(name))+".html"   
    print(nameUrl)     
    try:
        text=getHTMLText(nameUrl)
        if not re.findall(r'暂无(.+)!',text):       
            root = "D://pics//"+name+"//"
            if not os.path.exists(root):
                os.mkdir(root)
            downPictures(text,root,name)
            try:
                nextPage=re.findall(r'next" href="(.+)"',text)[0]
                while(nextPage):
                    nextText=getHTMLText(nextPage)
                    downPictures(nextText,root,name)              
                    nextPage=re.findall(r'next" href="(.+)"',nextText)[0]
            except IndexError:
                print("已全部下载完毕")
    except TypeError:
        print("不好意思，没有{}的照片".format(name))
    return

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

def getAllStar():
    #http://www.win4000.com/mt/starlst_0_0_1.html
    urls = []
    for s in range(3):
        for j in range(1,6):
            nameUrl = 'http://www.win4000.com/mt/starlst_0_'+str(s)+'_'+str(j)+'.html'
            urls.append(nameUrl)
    for s in range(1,5):
        for j in range(1,6):
            nameUrl = 'http://www.win4000.com/mt/starlst_'+str(s)+'_0_'+str(j)+'.html'
            urls.append(nameUrl)
    for j in range(1,6):
        nameUrl = 'http://www.win4000.com/mt/ribennx_'+str(j)+'.html'
        urls.append(nameUrl)
    
    print(len(urls))
    for url in urls:
        print(url)
        try:
            html=getHTMLText(url)
            soup_p = BeautifulSoup(html,'lxml')
            #print(soup_p)
            tab = soup_p.find('div',class_='Left_bar')
            #print(tab)
            #return
            hrefs = tab.select('li')
            for href in hrefs:
                nameUrl = href.a.get('href')
                try:
                    downloadImgs(nameUrl)
                except:
                    traceback.print_exc()
                    print('下载{}的图片异常'.format(nameUrl))
        except Exception:
            traceback.print_exc()
            print('解析{}异常'.format(url))

def getMeiNv():
    urls = []
    for s in range(2,8):
        for j in range(1,6):
            nameUrl = 'http://www.win4000.com/meinvtag'+str(s)+'_'+str(j)+'.html'
            urls.append(nameUrl)
    for url in urls:
        print(url)
        try:
            html=getHTMLText(url)
            soup_p = BeautifulSoup(html,'lxml')
            #print(soup_p)
            tab = soup_p.find('div',class_='Left_bar')
            #print(tab)
            #return
            hrefs = tab.select('li')
            for href in hrefs:
                nameUrl = href.a.get('href')
                try:
                    downloadImgs(nameUrl)
                except:
                    traceback.print_exc()
                    print('下载{}的图片异常'.format(nameUrl))
        except Exception:
            traceback.print_exc()
            print('解析{}异常'.format(url))

def getWallpaper():
    urls = []
    for s in range(191,210):
        for j in range(1,6):
            nameUrl = 'http://www.win4000.com/wallpaper_'+str(s)+'_0_0_'+str(j)+'.html'
            urls.append(nameUrl)

    for s in [2285,2286,2287,2357,2358,2361]:
        for j in range(1,6):
            nameUrl = 'http://www.win4000.com/wallpaper_'+str(s)+'_0_0_'+str(j)+'.html'
            urls.append(nameUrl) 

    for url in urls:
        print(url)
        try:
            html=getHTMLText(url)
            soup_p = BeautifulSoup(html,'lxml')
            #print(soup_p)
            tab = soup_p.find('div',class_='Left_bar')
            #print(tab)
            #return
            hrefs = tab.select('li')
            for href in hrefs:
                nameUrl = href.a.get('href')
                try:
                    downloadImgs(nameUrl)
                except:
                    traceback.print_exc()
                    print('下载{}的图片异常'.format(nameUrl))
        except Exception:
            traceback.print_exc()
            print('解析{}异常'.format(url))


 
if __name__ == '__main__':
    main()