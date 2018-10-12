# -*- coding: utf-8 -*-

import requests
import re
import os
import schedule
import time
from aip import AipSpeech

APP_ID = '9449387'
API_KEY = 'YGouKqxd5CDXs3byEHquVZ3Y'
SECRET_KEY = '25a189df5666fe98e0de51a0f3f9ece8 '
client = AipSpeech(APP_ID, API_KEY, SECRET_KEY)


def getWeather():
	url = 'http://www.weather.com.cn/data/sk/101010100.html'
	r = requests.get(url)
	r.encoding = 'utf-8'
	#print(r.text)
	data = r.json()['weatherinfo']
	print(data)
	data = data['city'] + '当前时间' +data['time'] + '，温度' + data['temp'] + ',' +data['WD'] + data['WS'] 
	result  = client.synthesis(data, 'zh', 1, {
		'vol': 5,
	})
	# 识别正确返回语音二进制 错误则返回dict 参照下面错误码
	if not isinstance(result, dict):
		with open('auido.mp3', 'wb') as f:
			f.write(result)
		os.system('auido.mp3')

#getWeather()
schedule.every().minutes.do(getWeather)

while True:
	schedule.run_pending()
	time.sleep(1)