# -*- coding: utf-8 -*-

import sqlite3
import os

'''
 其中conn对象是数据库链接对象，而对于数据库链接对象来说，具有以下操作：

        commit()            --事务提交
        rollback()          --事务回滚
        close()             --关闭一个数据库链接
        cursor()            --创建一个游标

    cu = conn.cursor()
    这样我们就创建了一个游标对象：cu
    在sqlite3中，所有sql语句的执行都要在游标对象的参与下完成
    对于游标对象cu，具有以下具体操作：

        execute()           --执行一条sql语句
        executemany()       --执行多条sql语句
        close()             --游标关闭
        fetchone()          --从结果中取出一条记录
        fetchmany()         --从结果中取出多条记录
        fetchall()          --从结果中取出所有记录
        scroll()            --游标滚动

'''


#数据库文件绝句路径
DB_FILE_PATH = 'proxyIP.db'
#表名称
TABLE_NAME = 'proxy_ip'
#是否打印sql
SHOW_SQL = True

def get_conn(path):

    conn = sqlite3.connect(path)
    if path:
        #print('硬盘上面:[{}]'.format(path))
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

###############################################################
####            创建|删除表操作     START
###############################################################
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

###############################################################
####            创建|删除表操作     END
###############################################################

def close_all(conn, cu):
    try:
        if cu is not None:
            cu.close()
    finally:
        if cu is not None:
            cu.close()

###############################################################
####            数据库操作CRUD     START
###############################################################

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
###############################################################
####            数据库操作CRUD     END
###############################################################

def deleteById(data):
    conn = get_conn(DB_FILE_PATH)
    cu = get_cursor(conn)
    for d in data:
        cu.execute('delete from proxy_ip where address = ?', d)
        conn.commit()
    close_all(conn, cu)
def getById(sql,data):
    conn = get_conn(DB_FILE_PATH)
    cu = get_cursor(conn)
    cu.execute(sql, data)
    r = cu.fetchone()
    if len(r) > 0:
       return r[0]
    close_all(conn,cu)

def getAll(sql):
    conn = get_conn(DB_FILE_PATH)
    cu = get_cursor(conn)
    cu.execute(sql)
    r = cu.fetchall()
    return r
    
def saveInfo(sql,data):
    conn = get_conn(DB_FILE_PATH)
    cu = get_cursor(conn)
    cu.execute(sql, data)
    conn.commit()
    close_all(conn, cu)

def init():
    charge_table_sql = 'SELECT count(*) FROM sqlite_master where name=' + '\'proxy_ip\''
    create_table_sql = '''CREATE TABLE `proxy_ip` (
                          `address` varchar(20) NOT NULL,
                          `ip` varchar(20) NOT NULL,
                          `port` varchar(200) DEFAULT NULL,
                          `httpType` varchar(200) DEFAULT NULL,
                          `othertype` varchar(200) DEFAULT NULL,
                          PRIMARY KEY (`address`)
                        )'''
    conn = get_conn(DB_FILE_PATH)
    charge_create_table(conn, create_table_sql,charge_table_sql)

if __name__ == '__main__':
    #main()
    init()