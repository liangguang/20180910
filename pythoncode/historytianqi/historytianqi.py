# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime,time
import traceback
import tianqiSqlite
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
        self.domain = ['www.tianqihoubao.com']
        self.base_url = 'http://tianqihoubao.com'

    def run(self):
        self.getHtml(self.base_url,None)
        #print(self.html)
        soup_p = BeautifulSoup(self.html,'html.parser')
        provices = soup_p.find('table',cellpadding='1')
        if not provices:
            return False
        provices = provices.select('tr td')
        p_num = 0
        for provice in provices:
            p_num = p_num + 1
            #print(provice)
            p_name = provice.a.get_text()
            print('开始查询:'+ p_name + '天气')
            p_url = self.base_url + '/' + provice.a.get('href')
            #p_title = provice.a.get('title')
            #print(str(p_num) + ':' + p_name + ':' + p_url)
            self.getHtml(p_url,None)
            soup_city = BeautifulSoup(self.html,'html.parser') 
            citys = soup_city.find('table',cellpadding='1')
            if not citys:
                continue
            citys = citys.select('tr td')
            c_num = 0
            for city in citys:
                c_num = c_num + 1 
                if p_num == 1 and c_num < 9:
                    continue
                c_name = city.a.get_text()
                print('开始查询:'+ p_name + '|' + c_name + '天气信息')
                c_url = self.base_url + '/' + city.a.get('href')
                c_url = c_url.replace('top','lishi')
                print(str(p_num) +'>'+p_name +'省: '+str(c_num)+ '>' +c_name+ '市: 地址' +c_url)
                self.getHtml(c_url,None)
                soup_years = BeautifulSoup(self.html,'html.parser') 
                years = soup_years.find('div',id='content')
                months = years.select('ul li a')
                for month in  months:
                    url = month['href']
                    if not url.startswith('/'):
                        url = self.base_url + '/lishi/' + url
                    else:
                        url = self.base_url + url
                    print(url)
                    self.getHtml(url,None)
                    soup_months = BeautifulSoup(self.html,'lxml') 
                    days = soup_months.select('table tr')
                    #print(days)
                    for day in days:
                        tds = day.select('td')
                        if not tds[0].a:
                            continue
                        rq = tds[0].get_text().strip().replace('/', '|').replace(' ', '')
                        tq = tds[1].get_text().strip().replace('/', '|').replace(' ', '')
                        wd = tds[2].get_text().strip().replace('/', '|').replace(' ', '')
                        fx = tds[3].get_text().strip().replace('/', '|').replace(' ', '')
                        #print(p_name +'|'+ c_name )
                        if(self.getOne(p_name,c_name,rq) > 0):
                                print(p_name +c_name + rq + '已存在')
                                continue
                        insertsql = 'insert into history_tianqi (`province`,`city`, `t_time`, `tq`,`wd`,`fx`) values (?,?,?,?,?,?)'
                        data = (p_name,c_name,rq,tq,wd,fx)
                        tianqiSqlite.saveInfo(insertsql,data)
                        #return
    
    def download(self, html, path):
        try:
           with open(path, 'w') as file:
              file.write(html)
              file.flush()
        except Exception:
           traceback.print_exc()
           print(path+'下载失败')
        else:
           print(path + '下载成功')

    def getHtml(self,url,encoding):
        response = requests.get(url, headers=self.headers)
        if not response.status_code == 200:
           print('请求失败,地址有误'+url)
           return False
        #print('请求地址:' + url)
        response.encoding = encoding
        if not encoding:
            response.encoding = 'gb2312'
        self.html = response.text
        return response.text

    def getOne(self,province,city,rq):
        #查询记录
        querysql = 'select count(*) from history_tianqi where province = ? and city= ? and t_time = ?'
        data = (province,city,rq)
        return tianqiSqlite.getById(querysql,data)        


if __name__ == '__main__':
    history = Tianqi()
    tianqiSqlite.init()
    history.run()
