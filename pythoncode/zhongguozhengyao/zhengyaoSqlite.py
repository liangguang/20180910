# -*- coding: utf-8 -*-

import sqlite3
import os


DB_FILE_PATH = 'zhengyao.db'
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

def charge_create_table(conn, createsql,chargetsql):

    if chargetsql is not None and chargetsql != '':
        cu = get_cursor(conn)
        if SHOW_SQL:
            print('执行sql:[{}]'.format(chargetsql))
        cu.execute(chargetsql)
        r = cu.fetchone()
        if len(r) > 0:
               for e in range(len(r)):
                  print(r[e])
        print('判断表是否存在返回值:'+ str(r[0]))
        if r[0] == 0:
            print('执行sql:[{}]'.format(createsql))
            cu.execute(createsql)
            conn.commit()
            print('创建数据库表成功!')
            close_all(conn, cu)
        else:
            print('数据库表已经创建')
    else:
        print('the [{}] is empty or equal None!'.format(chargetsql))


def close_all(conn, cu):
    try:
        if cu is not None:
            cu.close()
    finally:
        if cu is not None:
            cu.close()


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


def fetchall(conn, sql):
    '''查询所有数据'''
    if sql is not None and sql != '':
        cu = get_cursor(conn)
        if SHOW_SQL:
            print('执行sql:[{}]'.format(sql))
        cu.execute(sql)
        r = cu.fetchall()
        if len(r) > 0:
            for e in range(len(r)):
                print(r[e])
    else:
        print('the [{}] is empty or equal None!'.format(sql)) 

def fetchone(conn, sql, data):
    '''查询一条数据'''
    if sql is not None and sql != '':
        if data is not None:
            #Do this instead
            d = (data,) 
            cu = get_cursor(conn)
            if SHOW_SQL:
                print('执行sql:[{}],参数:[{}]'.format(sql, data))
            cu.execute(sql, d)
            r = cu.fetchone()
            if len(r) > 0:
               for e in range(len(r)):
                  print(r[e])
        else:
            print('the [{}] equal None!'.format(data))
    else:
        print('the [{}] is empty or equal None!'.format(sql))

def update(conn, sql, data):
    '''更新数据'''
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

def delete(conn, sql, data):
    '''删除数据'''
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

def getById(sql,data):
    conn = get_conn(DB_FILE_PATH)
    cu = get_cursor(conn)
    cu.execute(sql, data)
    r = cu.fetchone()
    if len(r) > 0:
       return r[0]
    close_all(conn,cu)

def saveInfo(sql,data):
    conn = get_conn(DB_FILE_PATH)
    cu = get_cursor(conn)
    cu.execute(sql, data)
    conn.commit()
    close_all(conn, cu)

def charge_create_table_bf():
    charge_table_sql = 'SELECT count(*) FROM sqlite_master where name=' + '\'zhengyao\''
    create_table_sql = '''CREATE TABLE `history_tianqi` (
                          `province` varchar(20) NOT NULL,
                          `city` varchar(20) NOT NULL,
                          `t_time` varchar(20) NOT NULL,
                          `tq` varchar(200) NOT NULL,
                          `wd` varchar(200) DEFAULT NULL,
                          `fx` varchar(200) DEFAULT NULL,
                          `logtime` TIMESTAMP default (datetime('now', 'localtime')),
                           PRIMARY KEY (`province`,`city`,`t_time`)
                        )'''
    conn = get_conn(DB_FILE_PATH)
    charge_create_table(conn, create_table_sql,charge_table_sql)

def init():
    charge_create_table_bf()