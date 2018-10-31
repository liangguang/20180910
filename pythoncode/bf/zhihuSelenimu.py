# -*- coding: utf-8 -*-

from selenium import webdriver
import time, re,requests,os,time,random,traceback
import urllib.request,threading
from bs4 import BeautifulSoup
import html.parser


def getHtml(page):
    driver = webdriver.Chrome(executable_path = "C:\\Users\\CDV\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver")  # 打开浏览器
    driver.get("https://www.zhihu.com/question/267782048/answers/created?page="+str(page)) # 打开想要爬取的知乎页面 

    # 模拟用户操作
    def execute_times(times):
        for i in range(times):
            print('第'+str(i)+'次点击') 
            driver.execute_script("window.scrollTo(0, "+str(1000 * i)+");")
            time.sleep(3)
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")    
    execute_times(12)

    result_raw = driver.page_source  # 这是原网页 HTML 信息
    result_soup = BeautifulSoup(result_raw, 'html.parser')# 然后将其解析
    result_bf = result_soup.prettify()  # 结构化原 HTML 文件
    with open("pages/page_"+str(page)+".txt", 'w',encoding="utf-8") as zhpage:  # 存储路径里的文件夹需要事先创建。
        zhpage.write(result_bf)
    zhpage.close()
    print("爬取回答页面成功!!!")
    driver.quit()
    return result_soup

def readTxt(path):
    f = open(path,'r',encoding='utf-8')
    strTxt = f.read()
    f.close()
    return strTxt

def getVideoApi(text,path):

    pattern = re.compile(r'https://www.zhihu.com/video/\d*?\"')
    result = pattern.findall(text)
    with open(path, 'w') as url:
        for num in result:
            #print(num)
            links = str(num) + "\n"
            links = links.replace('www.zhihu.com/video','lens.zhihu.com/api/videos')
            #print(links)
            url.write(links)
        url.close()
    #print(len(result))
    return len(result)

def getVideoInfo(path):
    list_url = []
    with open(path, 'r',encoding='utf-8') as f:
        while True:
            line = f.readline()
            if not line:
                break
            list_url.append(line.replace('"\n',''))
        f.close()
        print('读取完成')
    return list_url

def downloadVideo(v_url):
    headers = {
        'User-Agent': 
        'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36'
    }
    r = requests.get(v_url,headers=headers,timeout=30)
    result = r.json()
    #print(result)
    title = result['title']
    if len(title) < 2:
        title = str(time.time())
    path = 'video/'+title+'.mp4'
    hd = result['playlist']['hd']
    hd_url = ''
    if hd:
        hd_url = hd['play_url']
    else:
        hd_url = result['playlist']['ld']['play_url']

    size = 0
    if os.path.exists(path):
        print(path + '文件已存在')
        return True
    response = requests.get(hd_url,headers=headers,stream=True) # stream属性必须带上
    chunk_size = 1024 # 每次下载的数据大小
    content_size = int(response.headers['content-length']) # 总大小
    if response.status_code == 200:
        print('[文件大小]:%0.2f MB' %(content_size / chunk_size / 1024)) # 换算单位
        with open(path,'wb') as file:
            for data in response.iter_content(chunk_size=chunk_size):
                file.write(data)
                size += len(data) # 已下载的文件大小
        print('成功下载{}'.format(path))
    else:
        print('文件下载失败返回值:'+str(response.status_code))
        

def main():
    for i in range(1,328):
        try:
           getHtml(i)
           time.sleep(random.choice(range(5,8)))
        except Exception:
            pass
    dirs = os.listdir('pages/')
    num = 0
    for f in dirs:
        try:
            num = num + getVideoApi(readTxt('pages/'+f),'urls/'+f)
        except:
            pass
    #print(str(num))
    dirs = os.listdir('urls/')
    list_url = []
    for f in dirs:
        list_url = []
        try:
            list_url.extend(getVideoInfo('urls/'+f))  
        except Exception:
            traceback.print_exc()
            pass
    
    threads = []
    for url in list_url:
        if len(threads) < 9:
            t = threading.Thread(target=downloadVideo,args=(url))
            threads.append(t)
            t.start()
        else:
            downloadVideo(url)
        

def runNow():
    for i in range(1,328):
        try:
            getHtml(i)
            time.sleep(random.choice(range(1,3)))
            try:
                num = getVideoApi(readTxt('pages/page_'+str(i)+'.txt'),'urls/page_'+str(i)+'.txt')
                print(str(num))
                try:
                    list_url = getVideoInfo('urls/page_'+str(i)+'.txt')
                    for url in list_url:
                        try:
                            downloadVideo(url)
                        except Exception:
                            traceback.print_exc()
                            pass
                        
                except Exception:
                    traceback.print_exc()
                    pass
            except:
                traceback.print_exc()
                pass
        except Exception:
            traceback.print_exc()
            pass

       
    #print(str(num))
    dirs = os.listdir('urls/')
    list_url = []
    for f in dirs:
        list_url = []
        try:
            list_url.extend(getVideoInfo('urls/'+f))  
        except Exception:
            traceback.print_exc()
            pass
    
    threads = []
    for url in list_url:
        if len(threads) < 9:
            t = threading.Thread(target=downloadVideo,args=(url))
            threads.append(t)
            t.start()
        else:
            downloadVideo(url)
        

if __name__ == '__main__':
    runNow()
