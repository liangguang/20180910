# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime, time, traceback , random
from contextlib import closing
from urllib.request import urlopen
from bs4 import BeautifulSoup
import schedule, threading


def downloadText(text, path):
        if os.path.exists(path):
            print(path + "文件已存在")
            return True
        try:
           with open(path, 'w',encoding='utf-8') as file:
              file.write(text)
              file.flush()
        except Exception:
           traceback.print_exc()
           print(path+'下载失败')
        else:
           print(path + '下载成功')

def readTxt(path):
    f = open(path,'r',encoding='utf-8')
    strTxt = f.read()
    f.close()
    return strTxt

def readLine(path):
	list_url = []
	with open(path, 'r',encoding='utf-8') as f:
		while True:
			line = f.readline()
			if not line:
				break
			list_url.append(line.replace('"\n',''))
		f.close()
	return list_url

def writeTxt(path,ua_str):
	with open(path, 'a+') as url:
		url.write(ua_str+'\n')
		url.close()


def getHtml(base_url,headers):
	response = requests.get(base_url,headers=headers, timeout=30)
	response.encoding = 'utf-8'
	html = response.text
	soup = BeautifulSoup(html,'lxml')
	return (html,soup)

def getUA(url):
	headers = {
		'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
	}
	result = getHtml(url,headers)
	soup = result[1]
	ua_list = soup.select('table tr')
	for ua in ua_list:
		tds = ua.find_all('td')
		writeTxt(tds[2].text+'.txt',tds[3].text)
		

def run():
	base_url = 'http://www.fynas.com/ua/search?b=&k=&page='
	for page in range(1,51):
		url = base_url + str(page)
		try:
			getUA(url)
		except:
			pass
		time.sleep(random.choice(range(3,5)))
if __name__ == '__main__':	
	run()
	
