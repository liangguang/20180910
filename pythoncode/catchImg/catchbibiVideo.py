#-*-coding:utf-8-*-

import requests,os,traceback
import random,threading
import time,json

#tag_general_week
#tag_general_month

def get_json(url,num,tag):
    headers = {
        'User-Agent': 
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36'
    }

    params = {
        'page_size': 10,
        'next_offset': str(num),
        'tag': tag,
        'platform': 'pc',
        'type': 'tag_general_week'
    }

    try:
        html = requests.get(url,params=params,headers=headers)
        return html.json()

    except BaseException:
        print('request error')
        pass

def download(url,path):
    
    size = 0
    headers = {
        'User-Agent': 
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36'
    }
    if os.path.exists(path):
        print(path + '文件已存在')
        return True
    response = requests.get(url,headers=headers,stream=True) # stream属性必须带上
    chunk_size = 1024 # 每次下载的数据大小
    content_size = int(response.headers['content-length']) # 总大小
    if response.status_code == 200:
        print('[文件大小]:%0.2f MB' %(content_size / chunk_size / 1024)) # 换算单位
        with open(path,'wb') as file:
            for data in response.iter_content(chunk_size=chunk_size):
                file.write(data)
                size += len(data) # 已下载的文件大小
        print('成功下载{}'.format(path))

def getTab():
    url = 'http://api.vc.bilibili.com/clip/v1/video/zonelist?page=total&platform=pc'
    headers = {
            'User-Agent': 
            'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36'
        }
    html = requests.get(url,headers=headers)  
    #print(html.json())
    text = html.json()
    tags = text['data']
    #print(type(tags))
    taglist = []
    for k,v in tags.items():
        if k == '全部':
            v={'tags':['本周热门']}    
        taglist.extend(v['tags'])
        #print(str(v))
    return taglist
    #for tag in taglist:
    #    print(tag)


def run(tag,path):
    for i in range(10):
            url = 'http://api.vc.bilibili.com/board/v1/ranking/top?'
            num = i*10 + 1
            html = get_json(url,num,tag)
            infos = html['data']['items']
            #print(info)
            for info in infos:
                title = info['item']['description'] # 小视频的标题
                video_url = info['item']['video_playurl'] # 小视频的下载链接
                print(title)

                # 为了防止有些视频没有提供下载链接的情况
                try:
                    download(video_url,path+'/'+'%s.mp4' %title)
                    print('成功下载一个!')
                except BaseException:
                    print('凉凉,下载失败')
                    pass

            time.sleep(int(format(random.randint(2,8)))) # 设置随机等待时间

def start():
    threads = []
    folder = 'bibi/'
    taglist = getTab()
    for tag in taglist:
        path = folder+tag
        if not os.path.exists(path):
            os.mkdir(path)
        t = threading.Thread(target=run,args=(tag,path))
        #t.setDaemon(True)#设置为后台线程，这里默认是False，设置为True之后则主线程不用等待子线程
        threads.append(t)
        #run(tag,path)
    for t in threads:
        t.start()
        while True:
            print(len(threading.enumerate()))
            #判断正在运行的线程数量,如果小于5则退出while循环,
            #进入for循环启动新的进程.否则就一直在while循环进入死循环
            if(len(threading.enumerate()) < 5):
                break
            time.sleep(1)

def getNew():
    url = 'http://api.vc.bilibili.com/clip/v1/video/search?'
    #'page_size=30&next_offset=180000&tag=&need_playurl=1&order=new&platform=pc'
    path = 'bibi/最新180000'
    if not os.path.exists(path):
        os.mkdir(path)
    num = 0 
    #next_offset = 180000
    next_offset = 100000
    for i in range(1000):
        print('第{}次循环,next_offset={}'.format(str(i),str(next_offset)))
        html = get_new_json(url,next_offset)
        has_more = html['data']['has_more']
        if has_more != 1:
            break
        next_offset = html['data']['next_offset']
        infos = html['data']['items']
        threads = []
        for info in infos:
            title = info['item']['description'] # 小视频的标题
            video_url = info['item']['video_playurl'] # 小视频的下载链接
            print(title)
            # 为了防止有些视频没有提供下载链接的情况
            try:
                if len(threads) < 9:
                    t = threading.Thread(target=download,args=(video_url,path+'/'+'%s.mp4' %title))
                    threads.append(t)
                    t.start()
                else:
                    download(video_url,path+'/'+'%s.mp4' %title)
                num = num + 1
                print('成功下载'+str(num)+'个!')
            except BaseException:
                traceback.print_exc()
                print('凉凉,下载失败')
                pass

            time.sleep(int(format(random.randint(2,8)))) # 设置随机等待时间




def get_new_json(url,num):
    headers = {
        'User-Agent': 
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36'
    }

    params = {
        'page_size': 30,
        'next_offset': str(num),
        'tag': '',
        'platform': 'pc',
        'need_playurl': 1,
        'order':'new'
    }

    try:
        html = requests.get(url,params=params,headers=headers)
        return html.json()

    except BaseException:
        print('request error')
        pass


if __name__ == '__main__':
    #start()
    getNew()