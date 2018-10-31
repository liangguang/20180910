# -*- coding: utf-8 -*-

import requests, traceback
import re,threading
import os,time,random
from bs4 import BeautifulSoup


headers = {
    'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36'
}

def getHTMLText(url):
    r = requests.get(url,headers=headers,timeout=10)
    r.raise_for_status()
    r.encoding = r.apparent_encoding
    return r.text	

def downloadImgs(nameUrl,root='youmeitu'):
    if not os.path.exists(root):
        os.mkdir(root)

    imgs_url = 'http://www.youmeitu.com'+nameUrl    
    #print(nameUrl+'|'+root)
    html=getHTMLText(imgs_url)
    soup_p = BeautifulSoup(html,'lxml')
    tab = soup_p.find('div',class_='NewPages')
    pages = tab.find('li').get_text()
    num = pages[pages.find('共') + 1:pages.find('页')]
    print(num)
    for page in range(1,int(num)):
        img_url = 'http://www.youmeitu.com'+nameUrl.replace('.','_'+str(page)+'.')
        print(img_url)
        html=getHTMLText(img_url)
        soup_p = BeautifulSoup(html,'lxml')
        tab = soup_p.find('div',id='ArticleId60')
        href = tab.find('p')
        imgurl = href.a.img.get('src')
        title = href.a.img.get('title')
        path = os.path.join(root,title)+'.jpg'
        try:
            print(imgurl + '|'+ path)
            downloadOne(imgurl,path)
        except:
            traceback.print_exc()
            print('下载{}的图片{}异常'.format(name,title))
            
def downloadOne(imgurl,path):
    
    if not os.path.exists(path):
        r = requests.get(imgurl,timeout=5)
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

def getMeiNv():
    urls = []
    for s in range(1,215):
        nameUrl = 'http://www.youmeitu.com/meinv/list_'+str(s)+'.html'
        urls.append(nameUrl)
    #threads = []
    for url in urls:
        try:
            html=getHTMLText(url)
            #print(html)
            soup_p = BeautifulSoup(html,'lxml')
            tab = soup_p.find('div',class_='TypeList')
            hrefs = tab.select('li')
            for href in hrefs:
                nameUrl = href.a.get('href')
                    
                try:
                #   if len(threads) < 9:
                #        t = threading.Thread(target=downloadImgs,args=(nameUrl,))
                #        threads.append(t)
                #        t.start()
                #    else:
                #        downloadImgs(nameUrl)    
                   downloadImgs(nameUrl)
                except:
                    traceback.print_exc()
                    print('下载{}的图片异常'.format(nameUrl))
        except Exception:
            traceback.print_exc()
            print('解析{}异常'.format(url))
        
        #time.sleep(int(format(random.randint(2,5)))) # 设置随机等待时间
        #break

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
            tab = soup_p.find('div',class_='Left_bar')
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
    getMeiNv()