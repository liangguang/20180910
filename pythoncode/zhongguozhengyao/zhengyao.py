# -*- coding: utf-8 -*-

import requests, json, re, os, sys, datetime,time
import traceback , random
import zhengyaoSqlite
from urllib.parse import urlparse
from contextlib import closing
from urllib.request import urlopen
from bs4 import BeautifulSoup

class Zhengyao(object):
    def __init__(self):
        self.headers = {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'accept-encoding': 'gzip, deflate, br',
        'accept-language': 'zh-CN,zh;q=0.9',
        'cache-control': 'max-age=0',
        'upgrade-insecure-requests': '1',
            'user-agent': 'Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1'
        }
        self.domain = ['cpc.people.com.cn']
        self.img_base = 'http://cpc.people.com.cn'
        self.base_url = 'http://cpc.people.com.cn/GB/64162/394696/index.html'
        self.df_base_url = 'http://ldzl.people.com.cn'
        self.df_url = 'http://ldzl.people.com.cn/dfzlk/front/personProvince1.htm'
        self.proxies = {} 

    
    def indexHtml(self):
        #self.getProxyId() #获取代理
        self.getHtml(self.base_url,None)
        self.download(self.html,'index.html')
        soup_p = BeautifulSoup(self.html,'lxml')
        centers = soup_p.find('div',class_='nav w1068')
        if not centers:
            return False
        departments = centers.select('ul li')
        #print(os.listdir())
        persons = soup_p.select('.p_content .p_tab')
        for index in range(len(departments)):
            if index < 11:
                continue
            department = departments[index]
            depar_name = department.get_text()
            print(depar_name)
            if depar_name not in os.listdir():
                os.mkdir(depar_name)
            if len(departments) - 2 > index:
                self.getIndexPerson(persons[index],depar_name)
            elif len(departments) - 2 == index:
                self.getShehui(persons[index],depar_name)
            else:
                self.getDifang(depar_name)


    def getIndexPerson(self,person,depar_name):
        names = person.select('tr td')
        for name in names:
            if name.a:
                p_jl =  name.a['href']
                if not  'http' in p_jl:
                    p_jl = self.img_base + p_jl
                p_img = self.img_base + name.a.img['src']
                p_name = name.p.get_text()
                path = os.path.join(depar_name,p_name) + '.html'
                print(p_jl,p_img,p_name)
                self.getHtml(p_jl,None)
                soup_p = BeautifulSoup(self.html,'lxml')
                if  soup_p.find('div',class_='people_text') is None:
                    if  soup_p.find('div',class_='show_text') is None:
                        if soup_p.find('div',class_='p2j_con03') is None:
                            text = '无'
                        else:
                            text = soup_p.find('div',class_='p2j_con03').get_text()
                    else:
                       text = soup_p.find('div',class_='show_text').get_text()
                else:
                    text = soup_p.find('div',class_='people_text').get_text()
                self.download(text, os.path.join(depar_name,p_name) + '-简历.txt')
                self.download(self.html,path)
                self.downloadImg(p_img,os.path.join(depar_name,p_name) + '.jpg')
    
    def getShehui(self,person,depar_name):
        print('社会社会')
        names = person.select('tr td')
        for name in names:
            imgurl = name.img['src']
            if name.a:
                imgurl = name.a.img['src']
            if not  'http' in imgurl:
                imgurl = self.img_base + imgurl
            text = name.get_text().replace('\n','')
            self.downloadImg(imgurl,os.path.join(depar_name,text) + '.jpg')
    
    def getDifang(self,depar_name):
        print(depar_name)
        base_html = self.getHtml(self.df_url,None)
        soup_p = BeautifulSoup(base_html,'lxml')
        content = soup_p.find('div',class_='p4_con')
        provinces = content.select('div.fl ul li')
        num = 0
        index = 0
        for province in provinces:
            index = index + 1
            #if index < 33:
            #   continue
            href = province.a.get('href')
            name = province.get_text()
            #print(name , href)
            path = os.path.join(depar_name,name)
            if not os.path.exists(path):
                os.makedirs(path)
            if href:
                href = self.df_base_url + href
                num += self.downloadDfInfo(href,path,name)
        
        print('地方领导共'+str(num)+'人')
        return num


    def downloadDfInfo(self,href,path,name):
        #print(href)
        html = self.getHtml(href,None)
        downlaodPath = os.path.join(path,name) + '.html'
        self.download(html,downlaodPath)
        soup_p = BeautifulSoup(html,'lxml')
        box1 = soup_p.find('div',class_='box01')
        box2 = soup_p.find('div',class_='box02')
        box3 = soup_p.find('div',class_='box03')
        self.df_jl_url = 'http://ldzl.people.com.cn/dfzlk/front/'
        p_hrefs = []
        if box1:
            persons = box1.select('dl dt')
            for person in persons:
                p_href = self.df_jl_url + person.a.get('href') #人物简历
                p_hrefs.append((p_href,path))
               # print(p_href)


        if box2:    
            persons = box2.select('a')
            for person in persons:
                p_href = self.df_jl_url + person.get('href') #人物简历
                #print(p_href)
                p_hrefs.append((p_href,path))
        if box3:
            persons = box3.select('ul li')
            for person in persons:
                child = person.find('i').get_text() #人物简历
                childpath = os.path.join(path,child)
                #print(child)
                if not os.path.exists(childpath):
                    os.makedirs(childpath)
                for a in person.select('a'):
                    p_href = self.df_jl_url + a.get('href')
                    #print(p_href)
                    p_hrefs.append((p_href,childpath))
        print(name+'共'+str(len(p_hrefs))+'人')
        self.downloadDfjianli(p_hrefs)
        self.getProxyId() #获取代理
        return len(p_hrefs)

    def downloadDfjianli(self,listPerson):
        for arr in listPerson:
            href = arr[0]
            if href.endswith('personPage.htm'):
                continue
            path = arr[1]
            print(href,path)
            html = self.getHtml(href,'GBK')
            soup_p = BeautifulSoup(html,'lxml')
            name = soup_p.findAll('div',class_='fr')
            jianli = soup_p.find('div',class_='box01').get_text()
            #print(name[1])
            imgurl = name[1].select('dl dt')[0].img['src']
            imgurl = self.df_base_url + imgurl
            p_name = name[1].select('dl b')[0].get_text()
            p_name = p_name.replace('\n','').replace('\t','')
            downlaodPath = os.path.join(path,p_name) + '.jpg'
            self.downloadImg(imgurl,downlaodPath)
            downlaodPath = os.path.join(path,p_name) + '.html'
            self.download(html,downlaodPath)
            downlaodPath = os.path.join(path,p_name) + '-简历.txt'
            self.download(jianli,downlaodPath)
            #time.sleep(random.choice(range(1,3)))
           

    def downloadImg(self,imgurl,path):
        if not os.path.exists(path):
            r = requests.get(imgurl, proxies=self.proxies ,timeout=30)
            r.raise_for_status()
            #使用with语句可以不用自己手动关闭已经打开的文件流
            with open(path,"wb") as f: #开始写文件，wb代表写二进制文件
                f.write(r.content)
            print('下载'+path+'完成')
        else:
            print(path + "文件已存在")
    
    def download(self, html, path):
        if os.path.exists(path):
            print(path + "文件已存在")
            return True
        try:
           with open(path, 'w',encoding='utf-8') as file:
              file.write(html)
              file.flush()
        except Exception:
           traceback.print_exc()
           print(path+'下载失败')
        else:
           print(path + '下载成功')

    def getHtml(self,url,encoding):
        response = requests.get(url, headers=self.headers, proxies=self.proxies, timeout=60)
        if not response.status_code == 200:
           print('请求失败,地址有误'+url)
           return ''
        response.encoding = encoding
        if not encoding:
            response.encoding = 'GB2312'
        self.html = response.text
        return response.text

    def get_ip_list(self,url, headers):
        web_data = requests.get(url, headers=headers)
        soup = BeautifulSoup(web_data.text, 'lxml')
        ips = soup.find_all('tr')
        ip_list = []
        for i in range(1, len(ips)):
            ip_info = ips[i]
            tds = ip_info.find_all('td')
            ip_list.append(tds[1].text + ':' + tds[2].text)
        return ip_list
    
    def get_random_ip(self,ip_list):
        proxy_list = []
        for ip in ip_list:
            proxy_list.append('http://' + ip)
        proxy_ip = random.choice(proxy_list)
        proxies = {'http': proxy_ip}
        print(proxies)
        return proxies
    
    def getProxyId(self):
        proxy_url = 'http://www.xicidaili.com/nt/'
        proxy_headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36'
        }
        ip_list = self.get_ip_list(proxy_url, headers=proxy_headers)
        while True:
            try:
                self.proxies = self.get_random_ip(ip_list)
                response = requests.get('http://ldzl.people.com.cn/dfzlk/front/firstPage.htm', headers=self.headers, proxies=self.proxies)
                if response.status_code == 200:
                    print(self.proxies)
                    print('可用')
                    break
            except:
                print(str(self.proxies) + '不可用,继续下一个')


if __name__ == '__main__':
    zhengyao = Zhengyao()
    zhengyao.indexHtml()
    #zhengyao.getProxyId()