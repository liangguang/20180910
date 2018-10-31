# -*- coding: utf-8 -*-

import requests, traceback
import re
import os
from bs4 import BeautifulSoup


headers = {
    'User-Agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36'
}

def getHTMLText(url):
    r = requests.get(url,headers=headers,timeout=30)
    r.raise_for_status()
    r.encoding = r.apparent_encoding
    return r.text	

def downloadOne(imgurl,path):
    try:
        if not os.path.exists(path):
            r = requests.get(imgurl,headers=headers,timeout=30)
            r.raise_for_status()
            #使用with语句可以不用自己手动关闭已经打开的文件流
            with open(path,"wb") as f: #开始写文件，wb代表写二进制文件
                f.write(r.content)
                print('下载'+path+'完成')
        else:
            print(path + "文件已存在") 
    except Exception:
        traceback.print_exc()
        print('')
    


def main():
    keyword = 'girl'
    urls = ['https://www.pexels.com/search/'+keyword+'/?page={}'.format(str(i))
            for i in range(1,500)]
    for url in urls:
        print(url)
        try:
            text = getHTMLText(url)
            #print(text)
            patt = re.compile('<img srcset="(.*?)\?auto=.+')
            contents = re.findall(patt, text)
            #print(contents)
            for nameUrl in contents:
                name = nameUrl[nameUrl.rfind('/') + 1:]
                path = 'pexels/girls/'+name
                downloadOne(nameUrl,path)
        except Exception:
            traceback.print_exc()
       

if __name__ == '__main__':
    main()