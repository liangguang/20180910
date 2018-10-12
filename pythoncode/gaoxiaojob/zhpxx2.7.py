# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime,time
import urllib
from SendMail import SendMail
from bs4 import BeautifulSoup
reload(sys) 
sys.setdefaultencoding("utf-8")

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

    #def show(self,h,m):
    def show(self,h=11,m=10):
        #h表示设定的小时，m为设定的分钟
        while True:
        # 判断是否达到设定时间，例如0:00
             while True:
                now = datetime.datetime.now()
                # 到达设定时间，结束内循环
                if now.hour==h and now.minute==m:
                     break
                # 不到时间就等30秒之后再次检测
                time.sleep(30)
                print(now)
             print('*' * 60)
             print('\t\n高校人才网招聘信息汇总下载')
             print('\t\n下载地址' + self.url)
             print('*' * 60)
             self.run()

    def showNow(self):
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
        soup = BeautifulSoup(self.html,'html.parser')
        urls = soup.select('.list_b_info.right')
        urlcount = 0
        for item in urls:
            if (urlcount > 2):
               break
            else:
              urlcount = urlcount + 1
              print('当前下载数量'+ str(urlcount))
            dir = item.h2.a['title']
            url_c = item.h2.a['href']
            if not os.path.exists(dir):
               os.makedirs(dir)
            fileName = dir +'/'+item.h2.a['title']+'.html'
            self.download(item.h2.a['href'],fileName)
            #解析列表页
            soup_c = BeautifulSoup(self.html,'html.parser', from_encoding="gb18030")
            urls_c = soup_c.select('.article_body p a')
            count = 0
            for item_c in urls_c:
                if item_c.span:
                   count = count + 1
                   url_t = item_c['href']
                   name = item_c.get_text()
                   childFileName = dir +'/'+ str(count) +self.replaceName(name) +'.html'
                   #print(childFileName) 
                   if os.path.exists(childFileName):
                      print(childFileName + '文件已存在')
                      continue
                   else:
                      self.download(url_t,childFileName)
                      soup_s = BeautifulSoup(self.html,'html.parser', from_encoding="gb18030")
                      try:
                          articetextBody = soup_s.select('.article_body')
                          if not articetextBody:
                            articetextBody = soup_s.select('.detail-content')
                          articetext = articetextBody[0].get_text()
                          matchFlag = re.search(u'辅导员|化学',articetext.decode('utf8'))
                          if matchFlag:
                            SendMail.mail(SendMail(),name,url_t+ '\n\t' +articetext)
                          else:
                            print('文章 名称:' + name + '未匹配到')
                      except Exception:
                         print( childFileName +' 解析内容失败')
    def download(self, url, path):
        print(url)
        try:
           html = urllib.urlopen(url).read()
           with open(r'' + path, 'wb') as file:
              file.write(html)
              file.flush()
           self.html = html
        except Exception:
           print(path+'下载失败')
        else:
           print(path + '下载成功')

    def replaceName(self, name):
        for c in r'\/:*?"<>|/':
           name = name.replace(c, '')  
        return name

if __name__ == '__main__':
    zp = ZhaoPin()
    #h = input('输入小时')
    #m = input('输入分钟')
    #zp.show(int(h),int(m))
    #zp.showNow()
    zp.show()
