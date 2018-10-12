# -*- coding: utf-8 -*-

from threading import Timer
import requests 
import os
from requests_toolbelt import MultipartEncoder

# id    wx0f6a28765a0f9708
#secret c4876b335a27af8e00d2a2a4fc3ac6fa

def send_news():
    url = 'https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx0f6a28765a0f9708&secret=c4876b335a27af8e00d2a2a4fc3ac6fa'
    r = requests.get(url)
    access_token = r.json()['access_token']
    expires_in = r.json()['expires_in']
    print(access_token,str(expires_in))
    video = 'https://v1.ergengtech.com/footage/2018/06/07/4a22d7b7f38a9e147d368da3cb77709a/540p.mp4'
    filename = '1'
    os.system('you-get' + ' -O '+ filename +' '+ video )
    up_url= 'https://api.weixin.qq.com/cgi-bin/media/upload?access_token='+ access_token+'&type=video'
    files = open(filename+'.mp4','rb')
    #m = MultipartEncoder({
    #    {'media':files}
    #})
    data = {'filename':filename}
    media = {'media':files}
    r = requests.post(up_url,data=data, files=media)
    print(r.json())
if __name__ == "__main__":
    send_news()