# -*- coding: utf-8 -*-

import requests, traceback
import re,threading
import os
from bs4 import BeautifulSoup

def getHTMLText(url):
	#print(url)
	try:
		r = requests.get(url,timeout=30)
		r.raise_for_status()
		r.encoding = r.apparent_encoding
		return r.text
	except:
		print("")

def writeText(name,text):
    with open(name, 'a+',encoding="utf-8") as txt:  # 存储路径里的文件夹需要事先创建。
        txt.write(text)
    txt.close()

def download(url,path):
    
    size = 0
    headers = {
        'User-Agent': 
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36'
    }
    if os.path.exists(path):
        print(path + '文件已存在')
        return True
    response = requests.get(url,headers=headers,stream=True) # stream属性必须带上
    chunk_size = 1024 # 每次下载的数据大小
    content_size = int(response.headers['content-length']) # 总大小
    if response.status_code == 200:
        writeText('video.txt',path+"|"+url+ "\n")
        print('下载地址：'+url)
        print(path + '[文件大小]:%0.2f MB' %(content_size / chunk_size / 1024)) # 换算单位
        with open(path,'wb') as file:
            for data in response.iter_content(chunk_size=chunk_size):
                file.write(data)
                size += len(data) # 已下载的文件大小
                if content_size / size == 2:
                    print('文件已下载：%0.2f MB' %(size / chunk_size / 1024))
        print('成功下载{}'.format(path))

def main():

    root = '大疆航拍'
    urls = []
    for j in range(1,857):
        nameUrl = 'https://bbs.dji.com/forum.php?mod=forumdisplay&fid=60&orderby=dateline&filter=typeid&orderby=dateline&page='+str(j)
        urls.append(nameUrl)
    if not os.path.exists(root):
        os.mkdir(root)
    for url in urls:
        print('开始解析网址:'+url)
        try:
            html=getHTMLText(url)
            soup_p = BeautifulSoup(html,'lxml')
            ths = soup_p.find_all('th',class_='new')
            for th in ths:
                try:   
                   type = th.find('p').em.text
                   name = th.find('p').find('a',class_='xst').text
                   src = th.find('p').find('a',class_='xst').get('href')
                   #print(type + '|'+ name +'|https://bbs.dji.com/'+ src)
                   path = root + "/"+ type
                   if not os.path.exists(path):
                       os.mkdir(path)
                   vpath = path+'/'+name+'.mp4'
                   if os.path.exists(vpath):
                       print(vpath + ' 已存在,下一个')
                       break
                   html = getHTMLText('https://bbs.dji.com/'+ src)
                   #print('开始解析网页：https://bbs.dji.com/'+ src)
                   soup_p = BeautifulSoup(html,'lxml')
                   iframe = soup_p.find('iframe',target='_self')
                   if iframe is not None:
                      src = iframe.get('src')
                      html=getHTMLText(src)
                      vidoeUrls = re.findall(r'https://cn-videos.dji.net/video_trans/.+?/*.mp4',str(html))
                      if len(vidoeUrls) > 1:
                         videoUrl = vidoeUrls[1]
                      elif len(vidoeUrls) == 1:
                         videoUrl = vidoeUrls[0]
                      else:
                         print('未找到视频地址');
                         break
                      try:
                        download(videoUrl,vpath)
                      except Exception:
                        traceback.print_exc();
                        print('文件下载失败')
                      #print(videoUrl)
                      #break
                    #break
                except Exception:
                   traceback.print_exc()
                   print('解析信息异常')
                   #break
        except Exception:
            traceback.print_exc()
            print('解析{}异常'.format(url))
        #break
    #break
 
if __name__ == '__main__':
   main()