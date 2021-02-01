# -*- coding: utf-8 -*-

import requests
import json
import re
import os
import sys
import datetime
import time
import traceback
import random
import hashlib
from contextlib import closing
from urllib.request import urlopen
from bs4 import BeautifulSoup

#
# https://zh.wikihow.com/Category:%E4%BA%BA%E9%99%85%E5%85%B3%E7%B3%BB


def downloadText(text, path):
        if os.path.exists(path):
            #print(path + "文件已存在")
            return True
        try:
           with open(path, 'w', encoding='utf-8') as file:
              file.write(text)
              file.flush()
        except Exception:
           traceback.print_exc()
           print(path+'下载失败')
        else:
           print(path + '下载成功')


def getHtml(base_url, headers):
	response = requests.get(base_url, headers=headers, timeout=300)
	response.encoding = 'utf-8'
	html = response.text
	soup = BeautifulSoup(html, 'lxml')
	return (html, soup)


def getNews(base_url):
	print(base_url)
	headers = {
			'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
	}
	result = getHtml(base_url, headers)
	#print(result[0])
	soup = result[1]
	newslist = soup.findAll('div',class_='responsive_thumb')
	#print(newslist)
	index = 0
	news_url = []
	for news in newslist:
		index = index + 1
		#print(news)
		href = news.find('a')
		if href:
			href = news.find('a').get('href')
			news_url.append(href)
	for new_url in news_url:
		#print(new_url)
		try:
			result = getHtml(new_url, headers)
			soup = result[1]
			title = soup.find('title').text
			#if os.path.exists(title+'.html'):
			#	continue
			#print(title)
			title = title.replace(':','').strip()
			print(title)
			downloadText(result[0],title+'.html')
			break
		except :
			traceback.print_exc()
			print('解析：',new_url,'失败')

if __name__ == '__main__':
	for i in range(1,8):	
		base_url = 'https://zh.wikihow.com/Category:%E4%BA%BA%E9%99%85%E5%85%B3%E7%B3%BB?pg='+str(i);
		getNews(base_url)
	
