# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime,time
import traceback
import mySqlite
from SendMail import SendMail
from bs4 import BeautifulSoup

class ZhaoPin():
    def __init__(self):
        self.headers = {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'connection': 'keep-alive',
        'user-agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36'
        }
        self.domain = ['http://www.haedu.gov.cn']
        self.url = 'http://www.haedu.gov.cn/jszp/'

    def showNow(self):
        print('*' * 60)
        print('\t\n 河南教育厅招聘信息')
        print('\t\n 地址' + self.url)
        print('*' * 60)
        while True:
            self.run()    
            time.sleep(3600) #休息一小时

    def run(self):
    
        response = requests.get(self.url, headers=self.headers)
        if not response.status_code == 200:
            print('请求失败,地址有误'+self.url)
            return False
        print('请求地址:' + self.url)
        response.encoding = 'utf-8'
        self.html = response.text
        soup = BeautifulSoup(self.html,'html.parser')
        urls = soup.select('.list ul li')
        print(len(urls))
        for item in urls:
            pushtime = item.span.get_text()
            title = item.a.get_text()
            url_c = item.a['href']
            print(self.getOne(pushtime,'hnjyt'))
            if(self.getOne(pushtime,'hnjyt') > 0):
                print(title + '已存在')
                continue
            self.saveOne(pushtime,'hnjyt',title,url_c,'')
            r = requests.get(url_c)
            r.encoding = 'utf-8'
            self.html = r.text
            #解析内容页
            soup_c = BeautifulSoup(self.html,'html.parser')
            articetext = soup_c.select('.article')[0].get_text() #文章内容
            #print(articetext)
            matchFlag = True #re.search(u'辅导员|化学',articetext)
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
    zp.showNow()