# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime,time
import traceback , random,threading
from urllib.parse import urlparse
from contextlib import closing
from urllib.request import urlopen
from bs4 import BeautifulSoup
import poolSqlite


def get_ip_list(url,headers):
        web_data = requests.get(url, headers=headers)
        soup = BeautifulSoup(web_data.text, 'lxml')
        ips = soup.find_all('tr')
        ip_list = []
        for i in range(1, len(ips)):
            ip_info = ips[i]
            tds = ip_info.find_all('td')
            if tds[8].text.find('天') > 0:
                ip_list.append(tds[5].text+'://'+tds[1].text + ':' + tds[2].text)
        return ip_list

def get66ip():
    proxy_url = 'http://www.66ip.cn/nmtq.php?getnum=1000&isp=0&anonymoustype=0&start=&ports=&export=&ipaddress=&area=0&proxytype=2&api=66ip'

    proxy_headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
    }
    web_data = requests.get(proxy_url, headers=proxy_headers)
    pattern  = re.compile(r'\d+\.\d+\.\d+\.\d+\:\d+')
    ips = pattern.findall(web_data.text)
    for i in range(1, len(ips)):
            ip_info = ips[i]
            try:
                ip = ip_info[0:ip_info.find(':')]          
                port = ip_info[ip_info.find(':') + 1:]
                httpType = 'http'
                othreType = '高匿'
                address = httpType + '://'+ip + ':'+port
                proxies = {httpType : address}
                response = requests.get('http://www.ip38.com/', headers=proxy_headers, proxies=proxies)
                if response.status_code == 200:
                    print(str(proxies)+'有效')   
                    sql =  'insert into proxy_ip (`address`,`ip`,`port`,`httpType`,`otherType`) values (?,?,?,?,?)'
                    data = (address,ip,port,httpType,othreType)
                    poolSqlite.saveInfo(sql,data)
            except:
                traceback.print_exc()
                print(str(proxies) + '不可用,继续下一个')

def getyqie():
    for page in range(1,21):
        proxy_url = 'http://ip.yqie.com/proxygaoni/index_'+str(page)+'.htm'
        proxy_headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
        }
        web_data = requests.get(proxy_url, headers=proxy_headers)
        soup = BeautifulSoup(web_data.text, 'lxml')
        ips = soup.find_all('tr')
        for i in range(1, len(ips)):
            ip_info = ips[i]
            try:
                tds = ip_info.find_all('td')
                ip = tds[1].text          
                port = tds[2].text
                httpType = tds[4].text.lower()
                othreType = '高匿'
                address = httpType + '://'+ip + ':'+port
                proxies = {httpType : address}
                response = requests.get('http://www.ip38.com/', headers=proxy_headers, proxies=proxies)
                if response.status_code == 200:
                    print(str(proxies)+'有效')   
                    sql =  'insert into proxy_ip (`address`,`ip`,`port`,`httpType`,`otherType`) values (?,?,?,?,?)'
                    data = (address,ip,port,httpType,othreType)
                    poolSqlite.saveInfo(sql,data)
            except:
                traceback.print_exc()
                print(str(proxies) + '不可用,继续下一个')   

def getNimadili():
    for page in range(1,2001):
        proxy_url = 'http://www.nimadaili.com/gaoni/'+str(page)
        proxy_headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
        }
        web_data = requests.get(proxy_url, headers=proxy_headers)
        soup = BeautifulSoup(web_data.text, 'lxml')
        ips = soup.find_all('tr')
        for i in range(1, len(ips)):
            ip_info = ips[i]
            try:
                tds = ip_info.find_all('td')
                time = tds[5].text
                if time.find('天') == -1:
                    continue
                ip = tds[0].text          
                port = ''
                httpType = tds[1].text.lower()
                othreType = tds[2].text
                address = httpType + '://'+ip
                proxies = {httpType : address}
                response = requests.get('http://www.ip38.com/', headers=proxy_headers, proxies=proxies)
                if response.status_code == 200:
                    print(str(proxies)+'有效')   
                    sql =  'insert into proxy_ip (`address`,`ip`,`port`,`httpType`,`otherType`) values (?,?,?,?,?)'
                    data = (address,ip,port,httpType,othreType)
                    poolSqlite.saveInfo(sql,data)
            except:
                #traceback.print_exc()
                print(str(proxies) + '不可用,继续下一个')   



def getKuaiDili():
    for page in range(1,21):
        proxy_url = 'https://www.kuaidaili.com/free/inha/'+str(page)
        proxy_headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
        }
        web_data = requests.get(proxy_url, headers=proxy_headers)
        soup = BeautifulSoup(web_data.text, 'lxml')
        ips = soup.find_all('tr')
        for i in range(1, len(ips)):
            ip_info = ips[i]
            try:
                tds = ip_info.find_all('td')
                ip = tds[0].text          
                port = tds[1].text
                httpType = tds[3].text.lower()
                othreType = tds[2].text
                address = httpType + '://'+ip + ':'+port
                proxies = {httpType : address}
                response = requests.get('http://www.ip38.com/', headers=proxy_headers, proxies=proxies)
                if response.status_code == 200:
                    print(str(proxies)+'有效')   
                    sql =  'insert into proxy_ip (`address`,`ip`,`port`,`httpType`,`otherType`) values (?,?,?,?,?)'
                    data = (address,ip,port,httpType,othreType)
                    poolSqlite.saveInfo(sql,data)
            except:
                #traceback.print_exc()
                print(str(proxies) + '不可用,继续下一个')   

def getIp3366():
    num = 0
    for page in range(1,11):
        proxy_url = 'http://www.ip3366.net/?stype=1&page='+str(page)
        proxy_headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
        }
        web_data = requests.get(proxy_url, headers=proxy_headers)
        soup = BeautifulSoup(web_data.text, 'lxml')
        ips = soup.find_all('tr')
        for i in range(1, len(ips)):
            ip_info = ips[i]
            try:
                tds = ip_info.find_all('td')
                ip = tds[0].text          
                port = tds[1].text
                httpType = tds[3].text.lower()
                othreType = tds[2].text
                address = httpType + '://'+ip + ':'+port
                proxies = {httpType : address}
                response = requests.get('http://www.ip38.com/', headers=proxy_headers, proxies=proxies)
                if response.status_code == 200:
                    print(str(proxies)+'有效')   
                    sql =  'insert into proxy_ip (`address`,`ip`,`port`,`httpType`,`otherType`) values (?,?,?,?,?)'
                    data = (address,ip,port,httpType,othreType)
                    poolSqlite.saveInfo(sql,data)
                    num = num + 1
            except:
                #traceback.print_exc()
                print(str(proxies) + '不可用,继续下一个')   

    print('获取云代理ip'+num+'个')

def getXiCiIps(p_type):
    ipNum = 0
    #print(time.localtime)

    for num in range(1,20):
        proxy_url = 'http://www.xicidaili.com/'+p_type+'/'+str(num)
        proxy_headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
        }

        web_data = requests.get(proxy_url, headers=proxy_headers)
        soup = BeautifulSoup(web_data.text, 'lxml')
        ips = soup.find_all('tr')
        for i in range(1, len(ips)):
            ip_info = ips[i]
            try:
                tds = ip_info.find_all('td')
                if tds[8].text.find('天') > 0:
                    #ip_list.append(tds[5].text+'://'+tds[1].text + ':' + tds[2].text)
                    ip = tds[1].text          
                    port = tds[2].text
                    httpType = tds[5].text.lower()
                    othreType = tds[4].text
                    address = httpType + '://'+ip + ':'+port
                    proxies = {httpType : address}
                    response = requests.get('https://ip.cn/', headers=proxy_headers, proxies=proxies)
                    if response.status_code == 200:
                        print(str(proxies)+'有效')   
                        sql =  'insert into proxy_ip (`address`,`ip`,`port`,`httpType`,`otherType`) values (?,?,?,?,?)'
                        data = (address,ip,port,httpType,othreType)
                        poolSqlite.saveInfo(sql,data)
                        ipNum = ipNum + 1
            except:
                traceback.print_exc()
                print(str(proxies) + '不可用,继续下一个')  
    print('获取西祠代理IP'+str(ipNum)+'个')
    #print(time.localtime)

if __name__ == '__main__':
    poolSqlite.init()
    thread_xici = threading.Thread(target=getXiCiIps, args=('nn',))
    thread_xici.start()
    thread_ip = threading.Thread(target=getIp3366)
    thread_ip.start()
    thread_kuai = threading.Thread(target=getKuaiDili)
    thread_kuai.start()
    thread_nm = threading.Thread(target=getNimadili)
    thread_nm.start()
    thread_yq = threading.Thread(target=getyqie)
    thread_yq.start()
    thread_66ip = threading.Thread(target=get66ip)
    thread_66ip.start()