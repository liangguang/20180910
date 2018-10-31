# -*- coding: utf-8 -*-

from selenium import webdriver
import time, re,requests,os,time
import urllib.request
from bs4 import BeautifulSoup
import html.parser


def getHtml():
    driver = webdriver.Chrome(executable_path = "C:\\Users\\CDV\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver")  # 打开浏览器
    driver.get("https://www.zhihu.com/question/267782048") # 打开想要爬取的知乎页面 

    # 模拟用户操作
    def execute_times(times):

        for i in range(times):
            driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
            time.sleep(2)
            print('第'+str(i)+'次点击')          

    execute_times(10)

    result_raw = driver.page_source  # 这是原网页 HTML 信息
    result_soup = BeautifulSoup(result_raw, 'html.parser')# 然后将其解析
    result_bf = result_soup.prettify()  # 结构化原 HTML 文件
    with open("raw_result.txt", 'w',encoding="utf-8") as girls:  # 存储路径里的文件夹需要事先创建。
        girls.write(result_bf)
    girls.close()
    print("爬取回答页面成功!!!")

    return result_soup

def parseHtml(result_soup):
    #print(str(result_soup))
    noscript_all = ''
    with open("noscript_meta.txt", 'wb') as noscript_meta:
        noscript_nodes = result_soup.find_all('div',class_='RichText-video')
        noscript_inner_all = ""
        #print(str(noscript_nodes))
        for noscript in noscript_nodes:
            noscript_inner = noscript.get_text()
            noscript_inner_all += noscript_inner + "\n"
            noscript_all = html.parser.unescape(noscript_inner_all).encode('utf-8')  # 将内部内容转码并存储
        noscript_meta.write(noscript_all)
        noscript_meta.close()
        print("爬取RichText-video标签成功!!!")
    return noscript_all

def downloadClip(tags):
    img_soup = BeautifulSoup(tags, 'html.parser')
    img_nodes = img_soup.find_all('iframe')
    with open("img_meta.txt", 'w') as img_meta:
        count = 0
        for img in img_nodes:
            if img.get('src') is not None:
                img_url = img.get('src')
                print(img_url)
                line = str(count) + "\t" + img_url + "\n"
                img_meta.write(line)
                urllib.request.urlretrieve(img_url, "image/" + str(count) + ".jpg")  # 一个一个下载图片
                count += 1
    img_meta.close()
    print("下载成功")

def readTxt(path):
    f = open(path,'r',encoding='utf-8')
    strTxt = f.read()
    f.close()
    return strTxt

def getVideoApi(text):

    pattern = re.compile(r'https://www.zhihu.com/video/\d*?\"')
    result = pattern.findall(text)
    with open("videoUrl.txt", 'w') as url:
        for num in result:
            #print(num)
            links = str(num) + "\n"
            links = links.replace('www.zhihu.com/video','lens.zhihu.com/api/videos')
            #print(links)
            url.write(links)
        url.close()
    print(len(result))

def getVideoInfo():
    list_url = []
    with open("videoUrl.txt", 'r',encoding='utf-8') as f:
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
    
    text = readTxt('raw_result.txt')
    #print(text)
    getVideoApi(text)
    
    list_url = getVideoInfo()
    num = 0 
    for url in list_url:
        #print(url)
        num = num + 1
        downloadVideo(url)
        if num > 5:
            break

    #result_soup = getHtml()
    #result_soup = BeautifulSoup(text, 'html.parser')# 然后将其解析
    #tags = parseHtml(result_soup)
    #downloadClip(tags) 
    #time.sleep(10)
    
if __name__ == '__main__':
    main()
