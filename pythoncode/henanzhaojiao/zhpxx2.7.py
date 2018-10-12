# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime,time
import traceback
import mySqlite
from SendMail import SendMail
from bs4 import BeautifulSoup
reload(sys) 
sys.setdefaultencoding("utf-8")

class ZhaoPin():
    def __init__(self):
        self.headers = {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'connection': 'keep-alive',
        'user-agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36'
        }
        self.domain = ['http://www.haedu.cn']
        self.url = 'http://zhaojiao.haedu.cn/dishikuaisutongdao/'
        self.targets = ['zhengzho','xinxiang']
    def show(self,h=9,m=30):
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
             print('\t\n河南招教网招聘信息汇总下载')
             print('\t\n下载地址' + self.url)
             print('*' * 60)
             self.run()

    def showNow(self):
        print('*' * 60)
        print('\t\n河南招教网招聘信息汇总下载')
        print('\t\n下载地址' + self.url)
        print('*' * 60)
        self.run()    

    def run(self):
      for target in self.targets:
        self.url_addr = self.url + target
        response = requests.get(self.url_addr, headers=self.headers)
        if not response.status_code == 200:
            print('请求失败,地址有误'+self.url_addr)
            return False
        print('请求地址:' + self.url_addr)
        response.encoding = 'utf-8'
        self.html = response.text
        soup = BeautifulSoup(self.html,'html.parser')
        urls = soup.select('.main ul li')
        print(len(urls))
        for item in urls:
            pushtime = item.span.get_text()
            title = item.a.get_text()
            url_c = item.a['href']
            print(self.getOne(pushtime,target))
            if(self.getOne(pushtime,target) > 0):
                print(title + '已发送')
                continue
            self.saveOne(pushtime,target,title,url_c,'')
            r = requests.get(url_c)
            r.encoding = 'utf-8'
            self.html = r.text
            #解析内容页
            soup_c = BeautifulSoup(self.html,'html.parser')
            articetext = soup_c.select('.main')[0].get_text() #文章内容
            #print(articetext)
            matchFlag = re.search(u'辅导员|化学|长垣',articetext)
            if matchFlag:
               print(pushtime+'|'+title + ':匹配到了')
               SendMail.mail(SendMail(),title,url_c+ '\n\t' +articetext)
            else:
              print('文章:' + title + '未匹配到')
    
    def getOne(self,pushtime,city):
        #查询记录
        querysql = 'select count(*) from zpinfo where pushtime = ? and city= ?'
        data = (pushtime,city)
        return mySqlite.getById(querysql,data)
    
    def saveOne(self,pushtime,city,title,address,emailTo):
        insertsql = 'insert into zpinfo values (?,?,?,?,?)'
        data = (pushtime,city,title,address,emailTo)
        mySqlite.saveInfo(insertsql,data)

if __name__ == '__main__':
    zp = ZhaoPin()
    mySqlite.init()
    #zp.showNow()
    zp.show()