#-*-coding:utf-8-*-

import os
import time
import json
import re
import requests
import urllib3
import urllib


basepath = os.path.abspath(os.path.dirname(__file__))  # 当前模块文件的根目录


def get_response(url, info='image url', *args, **kwargs):
    '''捕获request.get()方法的异常，比如连接超时、被拒绝等
    如果请求成功，则返回响应体；如果请求失败，则返回None，所以在调用get_response()函数时需要先判断返回值
    '''
    s = requests.session()
    s.keep_alive = False
    urllib3.disable_warnings()  # 使用requests库请求HTTPS时,因为忽略证书验证,导致每次运行时都会报异常（InsecureRequestWarning），这行代码可禁止显示警告信息

    try:
        resp = requests.get(url, *args, **kwargs)
        resp.raise_for_status()
    except requests.exceptions.HTTPError as errh:
        # In the event of the rare invalid HTTP response, Requests will raise an HTTPError exception (e.g. 401 Unauthorized)
      
        pass
    except requests.exceptions.ConnectionError as errc:
        # In the event of a network problem (e.g. DNS failure, refused connection, etc)
        
        pass
    except requests.exceptions.Timeout as errt:
        # If a request times out, a Timeout exception is raised. Maybe set up for a retry, or continue in a retry loop
        
        pass
    except requests.exceptions.TooManyRedirects as errr:
        # If a request exceeds the configured number of maximum redirections, a TooManyRedirects exception is raised. Tell the user their URL was bad and try a different one
        
        pass
    except requests.exceptions.RequestException as err:
        # catastrophic error. bail.
       
        pass
    except Exception as err:
        pass
    else:
        return resp


def setup_down_path():
    '''设置图片下载后的保存位置，所有图片放在同一个目录下'''
    down_path = os.path.join(basepath, 'gank')
    if not os.path.isdir(down_path):
        os.mkdir(down_path)
    return down_path


def get_links():
    '''获取所有图片的下载链接'''
    # 捕获request.get方法的异常，比如连接超时、被拒绝等
    resp = get_response('http://gank.io/api/data/%E7%A6%8F%E5%88%A9/1000/1', info='API')
    if not resp:  # 请求失败时，resp为None，不能往下执行
        return
    dict_obj = json.loads(resp.content)
    links = [item['url'] for item in dict_obj['results']]
    #print(str(links))
    return links


def Schedule(blocknum, blocksize, totalsize):
    """
        blocknum:已经下载的数据块
        blocksize:数据块的大小
        totalsize:远程文件的大小
    """
    per = blocknum * blocksize / totalsize * 100.0
    if per > 100:
        per = 100
    print('当前下载进度： %.2f%%'%per)

def download_pic(url,name):
    filename = 'gank/{}.jpg'.format(name)
    print('开始下载:{}'.format(name))
    urllib.request.urlretrieve(url, filename, Schedule)#使用urllib.request.urlretrieve方法下载图片并返回当前下载进度
    time.sleep(1)


if __name__ == '__main__':
    setup_down_path()
    links = get_links()
    print(links)
    num = 0
    for url in links:
        num = num + 1
        if num < 100:
           continue
        try:
           download_pic(url,url[url.rfind('/') + 1:])
        except:
           pass
        
