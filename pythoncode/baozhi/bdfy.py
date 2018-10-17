#/usr/bin/env python
#coding=utf8
 
import requests
import hashlib
import urllib
import random
import traceback

def translate(text):
    appid = '20181016000220072' #你的appid
    secretKey = 'rDYSK0y8kii9m9m5CVzk' #你的密钥
    httpClient = None
    myurl = 'http://api.fanyi.baidu.com/api/trans/vip/translate'
    q = text
    fromLang = 'en'
    toLang = 'zh'
    salt = random.randint(32768, 65536)

    try:
        sign = appid+q+str(salt)+secretKey
        m1 = hashlib.md5()
        m1.update(sign.encode(encoding='UTF-8'))
        sign = m1.hexdigest()
        myurl = myurl+'?appid='+appid+'&q='+ urllib.parse.quote(q) +'&from='+fromLang+'&to='+toLang+'&salt='+str(salt)+'&sign='+sign
        response = requests.get(myurl)
        response.enconding = 'utf-8'
        return response.json()
    except Exception:
        traceback.print_exc()
        print('do noting')
    finally:
        if httpClient:
            httpClient.close()
    return False

#kor韩语 zh中文 en英文
def translateOther(text,fromLang,toLang):
    appid = '20181016000220072' #你的appid
    secretKey = 'rDYSK0y8kii9m9m5CVzk' #你的密钥
    httpClient = None
    myurl = 'http://api.fanyi.baidu.com/api/trans/vip/translate'
    q = text
    fromLang = fromLang
    toLang = toLang 
    salt = random.randint(32768, 65536)

    try:
        sign = appid+q+str(salt)+secretKey
        m1 = hashlib.md5()
        m1.update(sign.encode(encoding='UTF-8'))
        sign = m1.hexdigest()
        myurl = myurl+'?appid='+appid+'&q='+ urllib.parse.quote(q) +'&from='+fromLang+'&to='+toLang+'&salt='+str(salt)+'&sign='+sign
        response = requests.get(myurl)
        print(response)
        response.enconding = 'utf-8'
        return response.json()
    except Exception:
        traceback.print_exc()
        print('do noting')
    finally:
        if httpClient:
            httpClient.close()
    return False