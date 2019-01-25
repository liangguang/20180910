# -*- coding: utf-8 -*-

from splinter import Browser

executable_path ={"executable_path":"C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver"} 

browser = Browser('chrome', **executable_path, headless=False)

browser.visit('http://www.baidu.com')