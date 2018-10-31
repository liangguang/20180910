# -*- coding: utf-8 -*-

from selenium import webdriver
import time, re,requests,os,time,random,traceback
import urllib.request,threading
from bs4 import BeautifulSoup
import html.parser
from selenium.webdriver.common.keys import Keys
#from pyvirtualdisplay import Display #配置无界面chrome用



def openUrl(PROXY):
    
    #driver = webdriver.Chrome(executable_path = "C:\\Users\\CDV\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver")  # 无代理打开浏览器
    #driver.get("https://ip.cn")
    # 模拟用户操作   
    options = webdriver.ChromeOptions()
    options.add_argument('--proxy-server='+PROXY)  
    #options.add_argument('--ignore-certificate-errors')
    
    #display = Display(visible=0, size=(800, 600))
    #display.start()
    print(str(options))
    driver = webdriver.Chrome(executable_path = "C:\\Users\\CDV\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver",chrome_options=options)  #设置代理打开浏览器
    #driver.set_window_size(800,600) #设置浏览器窗口的大小
    driver.get("https://www.toutiao.com/i6615874582152741390")
    #driver.get("https://ip.cn")
    time.sleep(random.choice(range(5,8))) #模拟阅读时间
    execute_times(driver,3)
    #elem = driver.find_element_by_tag_name('textarea')
    #elem.click()
    #elem.send_keys("pycon")
    #elem.send_keys(Keys.RETURN)
    result_raw = driver.page_source  # 这是原网页 HTML 信息
    #result_soup = BeautifulSoup(result_raw, 'html.parser')# 然后将其解析
    driver.quit()
    return result_raw

def execute_times(driver,times):
        for i in range(times):
            print('第'+str(i)+'次点击') 
            driver.execute_script("window.scrollTo(0, "+str(800 * i)+");")
            time.sleep(10)
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);") 

if __name__ == '__main__':
    openUrl('')