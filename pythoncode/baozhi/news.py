# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime, time, traceback , random,  hashlib
from contextlib import closing
from urllib.request import urlopen
from bs4 import BeautifulSoup
import bdfy
from SendMail import SendMail
import schedule, threading


# 
# https://entertain.naver.com/ranking

def downloadImg(imgurl,path):
        if not os.path.exists(path):
            r = requests.get(imgurl)
            r.raise_for_status()
            #使用with语句可以不用自己手动关闭已经打开的文件流
            with open(path,"wb") as f: #开始写文件，wb代表写二进制文件
                f.write(r.content)
            print('下载'+path+'完成')
        else:
            print(path + "文件已存在")

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

def getHtml(base_url,headers):
	response = requests.get(base_url,headers=headers, timeout=30)
	response.encoding = 'utf-8'
	html = response.text
	soup = BeautifulSoup(html,'lxml')
	return (html,soup)

def getNBCNews():
	now = int(time.time())
	timeArray = time.localtime(now)
	Ymd = time.strftime('%Y-%m-%d', timeArray)
	if Ymd not in os.listdir():
	  os.mkdir(Ymd)

	base_url = 'https://www.nbcnews.com'
	headers = {
			'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
	}
	result = getHtml(base_url + '/tech-media',headers)
	soup = result[1]
	newslist = soup.findAll('article')
	index = 0
	news_url = []
	for news in newslist:
		index = index + 1
		#print(news)
		href = news.find('a')
		if href:
			href = news.find('a').get('href')
			news_url.append(base_url + href)
	for new_url in news_url:
		print(new_url) 
		try:
			result = getHtml(new_url,headers)
			soup = result[1]
			article = soup.find('div',class_='article')
			if not article:
				continue
			
			img_url = article.find('picture')
			imgpath = None
			if img_url:
				img_url = img_url.find('img').get('src')
				img_name = new_url[new_url.rfind('/') + 1:] + '.jpg'
				imgpath = os.path.join(Ymd,img_name)
				downloadImg(img_url,imgpath)

			text = article.get_text()
			#print(len(text))
			#result = bdfy.translate(text)
			dst = ''
			#dst = result['trans_result'][0].get('dst')
			#print('原文',text,'译文',dst)
			SendMail.mail(SendMail,img_url,text+ '\n' +dst,imgpath)


		except :
			traceback.print_exc()
			print('解析：',new_url,'失败')


#https://www.hollywoodreporter.com/topic/tv
def getHollywoodNews():
	now = int(time.time())
	timeArray = time.localtime(now)
	Ymd = time.strftime('%Y-%m-%d', timeArray)
	if Ymd not in os.listdir():
	  os.mkdir(Ymd)

	base_url = 'https://www.hollywoodreporter.com'
	headers = {
			'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
	}
	topics = ['/topic/movies','/topic/tv','/topic/entertainment-industry','/topic/technology']
	for topic in topics:
		result = getHtml(base_url + topic,headers)
		soup = result[1]
		newslist = soup.findAll('article')
		news_url = []
		for news in newslist:
			href = news.find('a')
			if href:
				href = news.find('a',class_='topic-card__link').get('href')
				if href.index('http') < 0:
					href = base_url + href
				news_url.append(href)
		#print('获取连接数：',str(len(news_url)))
		for new_url in news_url:
			print(new_url) 
			try:
				result = getHtml(new_url,headers)
				soup = result[1]
				
				title = soup.find('h1',class_='article__headline')
				if not title:
					continue
				title = title.get_text().replace('\n','') #标题
				deck = soup.find('h2',class_='article__deck')
				if not deck:
					deck = 'no deck'
				deck = deck.get_text().replace('\n','') # 副标题

				text = soup.find('div',class_='article__body')
				if not text:
					continue	
				
				result = bdfy.translate(title)
				title_dst = result['trans_result'][0].get('dst')
				print('原文',title,'译文',title_dst)	
				result = bdfy.translate(deck)
				deck_dst = result['trans_result'][0].get('dst')
				print('原文',deck,'译文',deck_dst)

				srcText = ''
				dstText = ''
				ps = text.select('p')
				for p in ps:
					#if len(p.get_text()) < 10:
					#	continue
					#result = bdfy.translate(p.get_text().replace('\n',''))
					#print(result)
					#dstText = result['trans_result'][0].get('dst')
					srcText += p.get_text().replace('\n','')
					#dstText += dstText

				img_url = soup.find('figure').find('img')
				imgpath = None
				if img_url:
					img_url = img_url.get('src')
					img_name = new_url[new_url.rfind('/') + 1:] + '.jpg'
					imgpath = os.path.join(Ymd,img_name)
					downloadImg(img_url,imgpath)
				
				#result = bdfy.translate(srcText)
				#dst = result['trans_result'][0].get('dst')
				#print('原文',srcText,'译文',dst)
				SendMail.mail(SendMail,
				title_dst, 
				title +'|'+deck +'|'+ srcText+ '\n'+ title_dst+'|'+deck_dst+'|'+dstText,
				imgpath)

			except :
				traceback.print_exc()
				print('解析：',new_url,'失败')


def getKorHtml(base_url,headers):
	response = requests.get(base_url,headers=headers, timeout=30)
	response.encoding = 'UTF-8'
	html = response.text
	soup = BeautifulSoup(html,'lxml')
	return (html,soup)

def getKorNews():
	now = int(time.time())
	timeArray = time.localtime(now)
	Ymd = time.strftime('%Y-%m-%d', timeArray)
	if Ymd not in os.listdir():
	  os.mkdir(Ymd)
	base_url = 'https://entertain.naver.com'
	headers = {
		'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
	}

	result = getKorHtml(base_url + '/ranking',headers)
	soup = result[1]
	newslist = soup.select('#ranking_news li')
	news_url = []
	for news in newslist:
		href = news.find('a')
		if href:
			href = news.find('a').get('href')
			href = base_url + href
			news_url.append(href)
	#print('获取连接数：',str(len(news_url)))
	for new_url in news_url:
		print(new_url) 
		try:
			result = getKorHtml(new_url,headers)
			soup = result[1]
			title = soup.find('h2',class_='end_tit')
			if not title:
				continue
			title = title.get_text().strip().replace('\n','') #标题
			text = soup.find('div',id="articeBody")
			if not text:
				continue	
			
			result = bdfy.translateOther(title,'kor','zh')
			print(result)
			title_dst = result['trans_result'][0].get('dst')
			#print('原文',title,'译文',title_dst)	
			srcText = text.get_text().strip().replace('\n','')
			dstText = ''		
			#if len(srcText) > 1000:
				#nowText = srcText
				#dstText = ''
				#while len(nowText) > 1000:
				#	result = bdfy.translateOther(nowText[0:1000],'kor','zh')
				#	dstText += result['trans_result'][0].get('dst')
				#	nowText = nowText[1000:]
				#result = bdfy.translateOther(nowText[len(srcText)/1000 * 1000 :],'kor','zh')
				#dstText += result['trans_result'][0].get('dst')
				
			#else:	
			#	result = bdfy.translateOther(srcText,'kor','zh')
			#	dstText = result['trans_result'][0].get('dst')

			img_url = text.find('img')
			imgpath = None
			if img_url:
				img_url = img_url.get('src')
				img_name = title_dst + '.jpg'
				imgpath = os.path.join(Ymd,img_name)
				downloadImg(img_url,imgpath)

			SendMail.mail(SendMail,
			title_dst, 
			title +'|'+ srcText+ '\n'+ title_dst+'|'+dstText,
			imgpath)

		except :
			traceback.print_exc()
			print('解析：',new_url,'失败')
	

def nbctask():
    threading.Thread(target=getNBCNews).start()
 
def hollywoodtask():
    threading.Thread(target=getHollywoodNews).start()

def kortask():
    threading.Thread(target=getKorNews).start()

def run():
	schedule.every().day.at("13:02").do(nbctask)
	schedule.every().day.at("13:04").do(hollywoodtask)
	schedule.every().day.at("13:06").do(kortask)
	while True:
		schedule.run_pending()
		time.sleep(1)

if __name__ == '__main__':	
	#run()
	getKorNews()
	
