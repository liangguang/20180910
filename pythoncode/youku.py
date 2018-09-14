# -*- coding: utf-8 -*-

from bs4 import BeautifulSoup
import urllib.request
import traceback
import re,string
import os
import sys
from urllib.parse import quote


base_curl="" #全局变量
path=''

#检索每一集url
def opencurl( strobj ):
	arrycurl=[]
	mpHtml = urllib.urlopen( strobj ).read()
	soup = BeautifulSoup(mpHtml,"html5lib")
	curl = soup.find_all('div',class_='item')
	for line in curl:
		if line.a:
			obj = 'http:'+line.a.get('href')
			arrycurl.append(obj)

	print('共找到'+str(len(arrycurl))+'集')
	cmd  = raw_input("\n是否要下载[Y/N]:") #等待用户输入
	if cmd=='Y' or cmd == 'y' :
		print('yes')
		for curl in arrycurl:
			objcurl(curl) #开始下载每一集
	else:
		print('no')
	
def objcurl(strobj):#把每一集下载到指定目录
#	flag = os.system('python3 /home/pi/Git/you-get/you-get' + ' ' + '-o '+path+ ' ' + strobj )
	flag = os.system('you-get' + ' ' + '-o '+path+ ' ' + strobj )
	if flag != 0:
		print('=========================')
		print('errr' +' ' +strobj)
		print('=========================')

def download(html, path):
	try:
		with open(r'' + path, 'wb') as file:
			file.write(html)
			file.flush()
	except Exception:
		traceback.print_exc()
		print(path+'下载失败')
	else:
		print(path + '下载成功')
#main
#检测输入参数
if len(sys.argv)!=3:
	print ('useage:py youkufid.py + path + 剧名')
	exit (0)
else:
	path=sys.argv[1]
	base_curl='http://www.soku.com/search_video/q_'+sys.argv[2]
if not os.path.exists(path): # 判断目录是否存在
	os.makedirs(path)  # 创建多级目录


try:
	base_curl = quote(base_curl, safe=string.printable)
	mpHtml = urllib.request.urlopen(base_curl).read()
	soup = BeautifulSoup(mpHtml,"html.parser",from_encoding="utf-8")
	download(mpHtml,os.path.join(path,'123.html'))
	curl = soup.find('h2',class_='base_name').a.get('href')
except AttributeError:
	traceback.print_exc()
	print ('没有找到该剧的资源!!!!')
	exit(-1)
if curl:
	opencurl(curl)
else:
	exit(0)
