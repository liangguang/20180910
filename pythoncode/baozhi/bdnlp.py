#/usr/bin/env python
#coding=utf8
 
import requests
import hashlib
import synonym
import random
from aip import AipNlp
import traceback

client = None

def init():
    APP_ID = '14677924' #你的appid
    API_KEY = 'ZR6etvQwOOeaEHfyKsKiSdu8'
    SECRET_KEY = 'ji2N8Wr763nFxVlnsKVrfykPD56Xmsgy' #你的密钥
    global client
    client = AipNlp(APP_ID, API_KEY, SECRET_KEY)
    #print(client)

def translate(text):
    cis = []
    global client
    if not client:
       init()
    try:
        response = client.lexer(text)
        print(response)
        result = response.items
        for r in result:
            ci = r.item
            if len(ci) = 2 or lne(ci) = 4:
                cis.append(ci)
        return cis
    except Exception:
        traceback.print_exc()
        print('do noting')
    return cis

def nplParse(title,text,imgs):
    cis = bdnlp.translate(text)
    #去重复
    for ci in list(set(cis)):
        record = synonym.getSynoRecord(ci)
        if not record:
            continue
        if ci = record[0]:
            text = text.replaceAll(ci,record[1])
        else:
            text = text.replaceAll(ci,record[1])
    return text



if __name__ == "__main__":
    translate(u'大卫艾伯纳现场称能参与到《一路惊奇》这部电影中，不仅可以和中国的优秀导演联合执导，也可以相互了解中外电影的不同。并且这个故事的动作喜剧元素十分吸引我，也恰好是我所擅长的视效能发挥大作用的地方，相信可以让中国观众获得情节与视觉的双重享受。我想我还会让《一路惊奇》走向海外，让更多观众看到中国电影的魅力。睿影视的陈思睿总裁也上台讲述了影片的由来：“这部电影的灵感事来自于《史密斯夫妇》，也可以说《一路惊奇》是中国版的《史密斯夫妇》，具体的详细情节还是要保密的，但是可以透露的是这部电影是公路类型偏喜剧电影，我们无论是剧本的打磨还是场地的拍摄都筹备了很久，绝对是一部值得期待的电影')