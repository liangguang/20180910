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


def create_url_table():
    create_table_sql = '''CREATE TABLE `history_url` (
                          `href` varchar(20) DEFAULT NULL,
                          `content` text DEFAULT NULL,
                          `logtime` TIMESTAMP default (datetime('now', 'localtime')),
                           PRIMARY KEY (`href`)
                        )'''
    conn = get_conn(DB_FILE_PATH)
    create_table(conn,create_table_sql)

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


def getHtml(base_url,headers):
	response = requests.get(base_url,headers=headers, timeout=30)
	response.encoding = 'utf-8'
	html = response.text
	soup = BeautifulSoup(html,'lxml')
	return (html,soup)

if __name__ == '__main__':
    getById('select * from syno where one =? or two= ?',('哀求','哀求'))