import requests, json, re, os, sys, datetime,time
import traceback
import historySqlite
from urllib.parse import urlparse
from contextlib import closing
from urllib.request import urlopen
from bs4 import BeautifulSoup

class Tianqi(object):
    def __init__(self):
        self.headers = {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'accept-encoding': 'gzip, deflate, br',
        'accept-language': 'zh-CN,zh;q=0.9',
        'cache-control': 'max-age=0',
        'upgrade-insecure-requests': '1',
            'user-agent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1'
        }
        self.domain = ['www.lssdjt.com']
        self.base_url = 'http://www.lssdjt.com'
    def run(self):
        for m in range(1,13):
            for d in range(1,32):
                url = self.base_url + '/'+ str(m)+'/'+str(d)
                response = requests.get(url, headers=self.headers)
                if not response.status_code == 200:
                    print('请求失败,地址有误'+url)
                    continue
                print('请求地址:' + url)
                response.encoding = 'utf-8'
                self.html = response.text
                soup = BeautifulSoup(self.html,'html.parser')
                events = soup.select('.main ul .gong')
                if events:
                    for event in events:
                        #print(event)
                        if event.a:
                            historytime = event.a.em.get_text()
                            title = event.a.i.get_text()
                            event_url = event.a['href']
                            event_img = event.a.get('rel')
                            if not event_img:
                                event_img = ''
                            else:
                                event_img = event_img[0]
                            print(event_url)
                            print(event_img)
                            response = requests.get(event_url, headers=self.headers)
                            if not response.status_code == 200:
                                print('请求失败,地址有误'+url)
                                continue
                            #print('请求地址:' + url)
                            response.encoding = 'utf-8'
                            self.html = response.text
                            event_soup = BeautifulSoup(self.html,'html.parser')
                            content = event_soup.select('.post_public')[0]
                            print(content.get_text())
                            insertsql = 'insert into history_today (`event`,`eventtime`, `month`, `day`,`img`,`href`,`content`) values (?,?,?,?,?,?,?)'
                            data = (title,historytime,str(m),str(d),event_img,url,content.get_text())
                            historySqlite.saveInfo(insertsql,data)
                        else:
                            historytime = event.em.get_text()
                            title = event.i.get_text()
                            insertsql = 'insert into history_today (`event`,`eventtime`, `month`, `day`) values (?,?,?,?)'
                            data = (title,historytime,str(m),str(d))
                            historySqlite.saveInfo(insertsql,data)


if __name__ == '__main__':
    history = Tianqi()
    historySqlite.init()
    history.run()
