#-*-coding:utf-8-*-

import urllib.request
import os,traceback
import re

def url_open(url):
    req = urllib.request.Request(url)
    req.add_header('User-Agent', 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36')
    response = urllib.request.urlopen(req)
    html = response.read()

    return html

def find_imgs(url):
    html = url_open(url).decode('utf-8')
    img_addrs = []

    pattern = re.compile(r'<img data-src="(.*?)".*?',re.S)
    img_addrs = re.findall(pattern, html)

    return img_addrs

def save_imgs(folder, img_addrs):
    for each in img_addrs:
        if each != "":
            each = "http:" + each
            print(each)
            filename = each.split('/')[-1]
            with open(filename, 'wb') as f:
                img = url_open(each)
                f.write(img)

def download(folder = 'poco'):
    if not os.path.exists(folder):
        os.mkdir(folder)
    os.chdir(folder)
    offset = 20327430


    for i in range(100000):
        url = "http://www.poco.cn/works/detail?works_id="
        url = url + str(offset) + ".htm"
        offset = offset - 1
        try:
            img_addrs = find_imgs(url)
            save_imgs(folder, img_addrs)
        except Exception:
            traceback.print_exc()
            pass
       


if __name__ == '__main__':
    download()
    