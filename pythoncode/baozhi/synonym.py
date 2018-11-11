# -*- coding: utf-8 -*-

import sqlite3
import requests, json, re, os, sys, datetime, time, traceback , random
from bs4 import BeautifulSoup

DB_FILE_PATH = 'synonym.db'
SHOW_SQL = True

def get_conn(path):
    conn = sqlite3.connect(path)
    if path:
        return conn
    else:
        conn = None
        print('内存上面:[:memory:]')
        return sqlite3.connect(':memory:')

def get_cursor(conn):
    if conn is not None:
        return conn.cursor()
    else:
        return get_conn('').cursor()

def close_all(conn, cu):
    try:
        if cu is not None:
            cu.close()
    finally:
        if cu is not None:
            cu.close()

def drop_table(conn, table):

    if table is not None and table != '':
        sql = 'DROP TABLE IF EXISTS ' + table
        if SHOW_SQL:
            print('执行sql:[{}]'.format(sql))
        cu = get_cursor(conn)
        cu.execute(sql)
        conn.commit()
        print('删除数据库表[{}]成功!'.format(table))
        close_all(conn, cu)
    else:
        print('the [{}] is empty or equal None!'.format(sql))

def create_table(conn, sql):
    '''创建数据库表：student'''
    if sql is not None and sql != '':
        cu = get_cursor(conn)
        if SHOW_SQL:
            print('执行sql:[{}]'.format(sql))
        cu.execute(sql)
        conn.commit()
        print('创建数据库表[student]成功!')
        close_all(conn, cu)
    else:
        print('the [{}] is empty or equal None!'.format(sql))


def save(conn, sql, data):
    '''插入数据'''
    if sql is not None and sql != '':
        if data is not None:
            cu = get_cursor(conn)
            for d in data:
                if SHOW_SQL:
                    print('执行sql:[{}],参数:[{}]'.format(sql, d))
                cu.execute(sql, d)
                conn.commit()
            close_all(conn, cu)
    else:
        print('the [{}] is empty or equal None!'.format(sql))

def saveOne(conn, sql):
    '''插入数据'''
    if sql is not None and sql != '':
        cu = get_cursor(conn)
        if SHOW_SQL:
            print('执行sql:[{}]'.format(sql))
        cu.execute(sql)
        conn.commit()
        close_all(conn, cu)
    else:
        print('the [{}] is empty or equal None!'.format(sql))

def saveInfo(sql,data):
    conn = get_conn(DB_FILE_PATH)
    save(conn,sql,data)

def saveUrl(url,text,orgin):
    conn = get_conn(DB_FILE_PATH)
    sql = 'insert into history_url values (?,?,?,?)'
    data = (url,orgin,datetime.time)
    save(conn,sql,data)

def getByUrl(url):
    sql = 'select * from history_url where href = ?'
    return getById(sql, (url,))


def create_url_table():
    create_table_sql = '''CREATE TABLE `history_url` (
                          `href` varchar(20) DEFAULT NULL,
                          `content` text DEFAULT NULL,
                          'orgin' text DEFAULT NUll,
                          `logtime` TIMESTAMP default (datetime('now', 'localtime')),
                           PRIMARY KEY (`href`)
                        )'''
    conn = get_conn(DB_FILE_PATH)
    create_table(conn,create_table_sql)

def getSynoRecord(text):
    sql = 'select * from synonym where one = ? or two = ?'
    return getById(sql,(text,text))

def getById(sql,data):
    conn = get_conn(DB_FILE_PATH)
    cu = get_cursor(conn)
    cu.execute(sql, data)
    print(sql)
    r = cu.fetchone()
    if len(r) > 0:
       #print(r)
       return r
    close_all(conn,cu)

def downloadImg(imgurl,path):
        if not os.path.exists(path):
            r = requests.get(imgurl)
            r.raise_for_status()
            #使用with语句可以不用自己手动关闭已经打开的文件流
            with open(path,"wb") as f: #开始写文件，wb代表写二进制文件
                f.write(r.content)
            print('下载'+path+'完成')
        else:
            print(path + "文件已存在")

def downloadText(text, path,encoding):
        try:
           with open(path, 'w',encoding=encoding) as file:
              file.write(text)
              file.flush()
        except Exception:
           traceback.print_exc()
           print(path+'下载失败')
        else:
           print(path + '下载成功')

def getHtml(base_url,headers,encoding):
	response = requests.get(base_url,headers=headers, timeout=30)
	response.encoding = encoding
	html = response.text
	soup = BeautifulSoup(html,'lxml')
	return (html,soup)

if __name__ == '__main__':
    #getById('select * from syno where one =? or two= ?',('哀求','哀求'))
    #create_url_table()
    #drop_table(get_conn(DB_FILE_PATH),'history_url')
    saveUrl('test','title','sina')