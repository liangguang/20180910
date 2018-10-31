# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime,time
import traceback , random
from urllib.parse import urlparse
from contextlib import closing
from urllib.request import urlopen
from bs4 import BeautifulSoup
import clickZan

headers = {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'accept-encoding': 'gzip, deflate, br',
        'accept-language': 'zh-CN,zh;q=0.9',
        'cache-control': 'max-age=0',
        'upgrade-insecure-requests': '1',
        'user-agent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1'
}

clickNum = 0

def get_ip_list(url,headers):
        web_data = requests.get(url, headers=headers)
        soup = BeautifulSoup(web_data.text, 'lxml')
        ips = soup.find_all('tr')
        ip_list = []
        for i in range(1, len(ips)):
            ip_info = ips[i]
            tds = ip_info.find_all('td')
            if tds[5].text == 'HTTP':
                ip_list.append(tds[5].text.lower()+'://'+tds[1].text + ':' + tds[2].text)
        return ip_list
    
def get_random_ip(ip_list):
        proxy_list = []
        for ip in ip_list:
            proxy_list.append('http://' + ip) 
        proxy_ip = random.choice(proxy_list)
        proxies = {'http': proxy_ip}
        print(proxies)
        return proxies


def getProxyId(num=1):
        global clickNum
        proxy_url = 'http://www.xicidaili.com/nt/'+str(num)
        proxy_headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
        }
        ip_list = get_ip_list(proxy_url, headers=proxy_headers)
        for ip in ip_list:
            try:
                proxies = {ip[0:ip.find(':') + 1]:ip}
                print(ip)
                response = requests.get('https://ip.cn/', headers=proxy_headers, proxies=proxies,timeout=5)
                if response.status_code == 200:
                    print(str(proxies)+'可用')
                    clickZan.openUrl(ip)
                    clickNum = clickNum + 1
            except:
                traceback.print_exc()
                print(str(proxies) + '不可用,继续下一个')  


if __name__ == '__main__':
    for num in range(1,5):
        getProxyId(num)
    print('点击{}次'.format(str(clickNum)))