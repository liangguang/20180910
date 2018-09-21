# -*- coding: utf-8 -*-

import urllib.request
import re
import os
import threading
from bs4 import BeautifulSoup

def donwload(url, i):
	urlt = url + str(i) 
	#print(urlt)
	htmlt = urllib.request.urlopen(urlt).read()
	soupt = BeautifulSoup(htmlt,'html.parser')
	urlt_s = soupt.select('.listright dt .img')
	#print(urlt_s)
	for item in urlt_s:
		#try:
			urlt_s_v = 'http://www.tingkew.com' + item['href'] 
			#print(item['href'])
			#print(urlt_s_v)
			htmlt_s = urllib.request.urlopen(urlt_s_v).read()
			soupt_s = BeautifulSoup(htmlt_s,'html.parser')
			urls_s_v_t = soupt_s.select('.playyouku a')
			#print(urls_s_v_t)
			if(len(urls_s_v_t) > 0):
				urls_t = 'http://www.tingkew.com' + urls_s_v_t[0]['href']
				title = urls_s_v_t[0].text
				#print(urls_t + ' ' + title)
				htmlt_d = urllib.request.urlopen(urls_t).read()
				soupt_d = BeautifulSoup(htmlt_d,'html.parser')
				urls_d = soupt_d.select('.playother4 a')
				if(len(urls_d) > 0):
					donwloadUrl = urls_d[0]['href']
					with open ('ykaddr-s.txt','a+') as fd:
						fd.write(title +'\n'+ donwloadUrl+'\n' )
						#fd.flush()
					#os.system('you-get' + ' ' + donwloadUrl)
		#except Exception:
		#	print(item['href']+'解析出现错误')
	print('第'+str(i) +'页解析结束')

url = 'http://www.tingkew.com/search.php?k=%E8%AF%B4%E8%AF%BE-37-0-'
thread_list = []
for i in range(1, 23):
    thread_list.append(threading.Thread(target=donwload, args=(url, i,)))
for t in range(0,len(thread_list)):
    thread_list[t].start()
    print("No." + str(t) + " thread start")

#if __name__ == '__main__':
#	url = input('输入地址')
#	donwload(url)	
#data = 'http://v.youku.com/v_show/id_XMzgxNDM3MDgwOA==.html'
#os.system('you-get' + ' ' + data )