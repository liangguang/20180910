import requests, json, re, os, sys
import urllib.request
from bs4 import BeautifulSoup

class ZhaoPin():
    def __init__(self):
        self.headers = {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'accept-encoding': 'gzip, deflate',
        'accept-language': 'zh-CN,zh;q=0.9',
        'cache-control': 'no-cache',
        'connection': 'keep-alive',
        'upgrade-insecure-requests': '1',
        'user-agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36'
        }
        self.domain = ['www.gaoxiaojob.com']
        self.url = 'http://www.gaoxiaojob.com/zhaopin/total/'

    def show(self):
        print('*' * 60)
        print('\t\n高校人才网招聘信息汇总下载')
        print('\t\n下载地址' + self.url)
        print('*' * 60)
        
        self.run()
    
    def run(self):
        #pageNo = input('输入页数:')
        pageNo = '1'
        if not pageNo:
           pageNo = 'index_1.html'
        else:
           pageNo = 'index_'+pageNo+'.html'

        response = requests.get(self.url + pageNo, headers=self.headers)
        if not response.status_code == 200:
            print('请求失败,地址有误'+self.url + pageNo)
            return False
        print('请求地址:'+self.url + pageNo)
        self.download(self.url + pageNo, pageNo)
        
        # 解析汇总页
        soup = BeautifulSoup(self.html,'html.parser')
        urls = soup.select('.list_b_info.right')
        for item in urls:
            #print(item.h2.a['title'])
            #print(item.h2.a['href'])
            dir = item.h2.a['title']
            url_c = item.h2.a['href']
            if not os.path.exists(dir):
               os.makedirs(dir)
            self.download(item.h2.a['href'],dir +'/'+item.h2.a['title']+'.html')
            #解析列表页
            soup_c = BeautifulSoup(self.html,'html.parser', from_encoding="gb18030")
            urls_c = soup_c.select('.article_body p a')
            count = 0
            for item_c in urls_c:
                if item_c.span:
                   count = count + 1
                   url_t = item_c['href']
                   name = item_c.get_text()
                   self.download(url_t,dir +'/'+ str(count) +self.replaceName(name) +'.html')

    def download(self, url, path):
        print(url)
        html = urllib.request.urlopen(url).read()
        with open(r'' + path, 'wb') as file:
              file.write(html)
              file.flush()
        self.html = html
        print(path + '下载成功')
    def replaceName(self, name):
        for c in r'\/:*?"<>|/':
           name = name.replace(c, '')  
        return name

if __name__ == '__main__':
    zp = ZhaoPin()
    zp.show()