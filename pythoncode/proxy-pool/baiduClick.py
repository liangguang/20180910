# -*- coding: utf-8 -*-

from selenium import webdriver
import time, re,requests,os,time,random,traceback
import urllib.request,threading
from bs4 import BeautifulSoup
import html.parser
from selenium.webdriver.common.keys import Keys
import userAgent


def openUrl(url,user_agent):
    try:  
        options = webdriver.ChromeOptions() 
        WIDTH = 320
        HEIGHT = 640
        PIXEL_RATIO = 3.0
        UA = user_agent
        mobileEmulation = {"deviceMetrics": {"width": WIDTH, "height": HEIGHT, "pixelRatio": PIXEL_RATIO}, "userAgent": UA}
        #mobileEmulation = {'deviceName': 'iPhone X'}
        options.add_experimental_option('mobileEmulation', mobileEmulation)
        #options.add_argument('--user-agent='+user_agent)
        driver = webdriver.Chrome(
        executable_path = "C:\\Users\\CDV\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver",
        chrome_options=options)
        driver.get(url)
        time.sleep(random.choice(range(15,20))) #模拟阅读时间
        driver.set_window_size(400,800)
        execute_times(driver,3)
        result_raw = driver.page_source  # 这是原网页 HTML 信息
        driver.close()
        return result_raw
    except:
        traceback.print_exc()
    
def execute_times(driver,times):
        for i in range(times):
            print('第'+str(i)+'次点击') 
            driver.execute_script("window.scrollTo(0, "+str(800 * i)+");")
            time.sleep(5)
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);") 

def PhantomJSopen(url,user_agent):
    print(url)
    driver = webdriver.PhantomJS(executable_path = 'G:\\phantomjs-2.1.1-windows\\bin\\phantomjs')
    driver.get(url)
    time.sleep(random.choice(range(3,8))) #模拟阅读时间
    print(driver.title)


phone_user_agent_list = [
            #手机百度
            "Mozilla/5.0 (Linux; Android 6.0.1; OPPO A57 Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 T7/9.1 baidubrowser/7.18.21.0 (Baidu; P1 6.0.1)",
			"Mozilla/5.0 (Linux; Android 4.4.2; CHM-TL00H Build/HonorCHM-TL00H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 T7/9.1 baidubrowser/7.18.21.0 (Baidu; P1 4.4.2)",
			"Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; H7S Build/KTU84Q) AppleWebKit/534.24 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.24 T5/2.0 baidubrowser/5.0.3.0 (Baidu; P1 4.4.4)",
			"Mozilla/5.0 (Linux; Android 6.0.1; ATH-AL00 Build/HONORATH-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 baidubrowser/7.9.12.0 (Baidu; P1 6.0.1)",
			"Mozilla/5.0 (Linux; U; Android 8.0.0; zh-cn; STF-AL10 Build/HUAWEISTF-AL10) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 Mobile Safari/537.36 baidubrowser/7.18.21.0 (Baidu; P1 8.0)",
			"Mozilla/5.0 (Linux; U; Android 8.0.0; zh-cn; STF-AL10 Build/HUAWEISTF-AL10) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 Mobile Safari/537.36 T7/9.3 baidubrowser/7.18.23.0 (Baidu; P1 8.0)",
			"Mozilla/5.0 (Linux; Android 6.0.1; HUAWEI RIO-TL00 Build/HuaweiRIO-TL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/49.0.2623.105 Mobile Safari/537.36 baidubrowser/6.0.3.201 (Baidu; P1 6.0.1)",
			"Mozilla/5.0 (Linux; Android 5.1.1; OPPO R9 Plusm A Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 T7/9.1 baidubrowser/7.18.20.0 (Baidu; P1 5.1.1)",
			"Mozilla/5.0 (Linux; Android 7.1.2; 8848 M4 Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 baidubrowser/7.11.4.204 (Baidu; P1 7.1.2)",
			"Mozilla/5.0 (Linux; Android 8.0; VTR-AL00 Build/HUAWEIVTR-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 T7/9.1 baidubrowser/7.18.20.0 (Baidu; P1 8.0.0)",
            "Mozilla/5.0 (Linux; Android 8.1.0; ALP-AL00 Build/HUAWEIALP-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.11 (Baidu; P1 8.1.0)",
			"Mozilla/5.0 (Linux; Android 6.0.1; OPPO A57 Build/MMB29M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.10 (Baidu; P1 6.0.1)",
			"Mozilla/5.0 (Linux; U; Android 4.1.2; zh-cn; HUAWEI MT1-U06 Build/HuaweiMT1-U06) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 baiduboxapp/042_2.7.3_diordna_8021_027/IEWAUH_61_2.1.4_60U-1TM+IEWAUH/7300001a/91E050E40679F078E51FD06CD5BF0A43%7C544176010472968/1",
			"Mozilla/5.0 (Linux; Android 7.0; VTR-AL00 Build/HUAWEIVTR-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 lite baiduboxapp/3.6.0.10 (Baidu; P1 7.0)",
			"Mozilla/5.0 (Linux; Android 8.1.0; PACM00 Build/O11019; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.11 (Baidu; P1 8.1.0)",
			"Mozilla/5.0 (Linux; Android 7.1.1; OPPO R11 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.11 (Baidu; P1 7.1.1)",
			"Mozilla/5.0 (Linux; Android 8.1.0; CLT-AL00 Build/HUAWEICLT-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.11 (Baidu; P1 8.1.0)",
			"Mozilla/5.0 (Linux; Android 8.0.0; BKL-AL00 Build/HUAWEIBKL-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.11 (Baidu; P1 8.0.0)",
			"Mozilla/5.0 (Linux; Android 8.1.0; vivo Y71A Build/OPM1.171019.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.11 (Baidu; P1 8.1.0)",
			"Mozilla/5.0 (Linux; Android 8.0; FRD-AL10 Build/HUAWEIFRD-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 T7/10.6 baiduboxapp/10.6.0.11 (Baidu; P1 8.0.0)"
        ]
def runopen():
    for f in os.listdir():
        if f.endswith('txt'):
            list_ua = userAgent.readLine(f)
            for ua in list_ua:
                openUrl('https://rs.mbd.baidu.com/t7hmzrq?f=cp',ua)
                openUrl('https://rs.mbd.baidu.com/pyrzf6t?f=cp',ua)

if __name__ == '__main__':
   for ua in phone_user_agent_list:
        PhantomJSopen('https://rs.mbd.baidu.com/t7hmzrq?f=cp',ua)
        PhantomJSopen('https://rs.mbd.baidu.com/pyrzf6t?f=cp',ua)