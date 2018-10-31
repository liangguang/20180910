# -*- coding: utf-8 -*-

from selenium import webdriver
import time, re,requests,os,time,random,traceback
import urllib.request,threading
from bs4 import BeautifulSoup
import html.parser
from selenium.webdriver.common.keys import Keys
import poolSqlite



def openUrl(PROXY,url):
    print(PROXY +'-'+url)
    if PROXY.find('https') > -1:
       return False
    #driver = webdriver.Chrome(executable_path = "C:\\Users\\CDV\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver")  # 无代理打开浏览器
    #driver.get("https://ip.cn")
    # 模拟用户操作       
    #PROXY = PROXY.replace('https','http')
    try:  
        proxy_headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
        }
        proxies = {'https':PROXY}
        response = requests.get('https://ip.cn', headers=proxy_headers, proxies=proxies)
        #print(response.text)
        if response.status_code != 200:
            poolSqlite.deleteById((PROXY,))
            print('delete'+PROXY+'success')
        print(response.status_code)
        options = webdriver.ChromeOptions()
        options.add_argument('--proxy-server='+PROXY)  
        options.add_argument('--ignore-certificate-errors')
        #不好使=start
        desired_capabilities = options.to_capabilities()
        desired_capabilities['proxy'] = {
            "httpProxy":PROXY.replace('http://',''),
            "ftpProxy":PROXY.replace('http://',''),
            "sslProxy":PROXY.replace('http://',''),
            "noProxy":None,
            "proxyType":"MANUAL",
            "class":"org.openqa.selenium.Proxy",
            "autodetect":False
        }
        #不好使=end
        #options.add_argument('--user-agent=iphone')
        #print(str(options._arguments))
        driver = webdriver.Chrome(
        executable_path = "C:\\Users\\CDV\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver",
        chrome_options=options)  #设置代理打开浏览器
        driver.set_window_size(400,400)
        driver.get(url)
        #driver.get("http://www.ip38.com/")
        time.sleep(random.choice(range(15,20))) #模拟阅读时间
        execute_times(driver,3)
        result_raw = driver.page_source  # 这是原网页 HTML 信息
        #driver.quit()
        return result_raw
    except:
        traceback.print_exc()
        poolSqlite.deleteById((PROXY,))
    finally:
        driver.quit()
    

   

def execute_times(driver,times):
        for i in range(times):
            print('第'+str(i)+'次点击') 
            driver.execute_script("window.scrollTo(0, "+str(800 * i)+");")
            time.sleep(5)
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);") 

def readerUrl(ip,url):
    print(ip+'|'+url)
    try:
        ua = random.choice(user_agent_list)
        #headerRandom = {'user-agent': ua}
        proxy_headers = {
            'accept-encoding': 'gzip, deflate, br',
            'accept-language': 'zh-CN,zh;q=0.9',
            'cache-control': 'no-cache',
            'pragma': 'no-cache',
            'upgrade-insecure-requests': '1',
            'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'user-agent': ua
            #'user-agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
        }
        proxies = {'https': ip}
        #print(str(proxies))
        response = requests.get(url, headers=proxy_headers, proxies=proxies)
        print(response.status_code)
        #print(response.text)
        time.sleep(random.choice(range(1,5)))
    except:
        traceback.print_exc()
        #print(ip+'请求失败。。。')
        #poolSqlite.deleteById((ip,))
        pass

user_agent_list = [
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1",
            "Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6",
            "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5",
            "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24",
            "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24"
        ]

def run():
    poolSqlite.init()
    list_ip = poolSqlite.getAll('select address from proxy_ip')
    #list_url = ['https://www.toutiao.com/i6615496289800946179/','https://www.toutiao.com/i6615870575988457987/','https://www.toutiao.com/i6617410368115327495/','https://www.toutiao.com/i6615874582152741390/']
    #list_url = ['http://kuaibao.qq.com/s/20181028A1HRUS00','http://kuaibao.qq.com/s/20181029A1P03E00']
    list_url = ['http://baijiahao.baidu.com/s?id=1615750502490602131','http://baijiahao.baidu.com/s?id=1615578671901142799','http://baijiahao.baidu.com/s?id=1615030091833269589']
    for ip in list_ip:
        for url in list_url:
            ip_p = ip[0]
            if ip_p.find(',') > 0:
                ip_p = ip_p[ip_p.find(',') + 1:]
            try:
                #openUrl(ip_p,url)
                readerUrl(ip_p,url)
            except:
                pass
            #break
        #break
if __name__ == '__main__':
    run()
    #openUrl('http://171.38.79.113:8123','https://www.toutiao.com/i6617410368115327495')