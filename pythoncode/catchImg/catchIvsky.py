# -*- coding: utf-8 -*-

import os, sys, io, urllib, requests, re, chardet, time
import multiprocessing, traceback
from bs4 import BeautifulSoup

headers = {
    'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36'
}

urls=['http://www.ivsky.com/tupian/index_{}.html'.format(str(i))
		for i in range(1,101)]

def Schedule(blocknum, blocksize, totalsize):
    """
        blocknum:已经下载的数据块
        blocksize:数据块的大小
        totalsize:远程文件的大小
    """
    per = blocknum * blocksize / totalsize * 100.0
    if per > 100:
        per = 100
    print('当前下载进度： %.2f%%'%per)

def download_page(url):
    try:
        res = requests.get(url, headers=headers)
        if res.status_code == 200:
            res.encoding = chardet.detect(res.content).get('encoding')#通过chardet库的detect方法获取页面的编码
            return res.text
    except:
        traceback.print_exc()
        return None

def parser_html(html):
    if not html:
        return
    patt = re.compile('<img src="(.*?)" alt="(.*?)"></a>', re.S)#获取图片的链接和名字
    content = re.findall(patt, html)
    return content #返回图片链接和名字列表

def download_pic(url,name,root):
    if not os.path.exists(root):
        os.mkdir(root)
    filename = root + '/{}.jpg'.format(name)
    print('开始下载:{}'.format(name))
    urllib.request.urlretrieve(url, filename, Schedule)#使用urllib.request.urlretrieve方法下载图片并返回当前下载进度
    time.sleep(1)




if __name__ == '__main__':
    for url in urls:
        html = download_page(url)
        #print(html)
        patt = re.compile('<p><a href=\"(.*?)\">(.*?)</a>', re.S)#获取图片的链接和名字
        hrefs = re.findall(patt, html)
        #print(hrefs)
        for href in hrefs:
            imgurl = href[0]
            if len(imgurl) < 5:
                continue
            try:
                imgurl = 'http://www.ivsky.com' + imgurl
                title =href[1]
                root = 'ivsky/'+title
                if not os.path.exists(root):
                    os.mkdir(root)
                html = download_page(imgurl)
                #print(html)
                contents = parser_html(html)
                #print(contents)
                #break
                for content in contents:
                    p = multiprocessing.Process(target=download_pic, args=(content[0],content[1],root))
                    #启动进程和连接进程
                    p.start()
                    p.join()
            except:
                traceback.print_exc()
                print(imgurl+'解析失败...')
            #break
        #break
           
           