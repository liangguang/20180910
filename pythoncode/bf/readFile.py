# -*- coding: utf-8 -*-
import os
import traceback
import threading

def readFileOne(file):
    with open(file) as f:
        line = f.readline()
        while line:
            print(line)
            line = f.readline()

def readFileTwo(file):
    with open(file) as f:
        lines = f.readlines()
        for i in range(0,len(lines),2):
            #print(lines[i]+':'+lines[i+1])
            try: 
               folder = lines[i].strip('\n').strip()
               dir = os.path.join('E:\\QLDownload',folder)
               print(dir)
               if not os.path.exists(dir):
                    os.makedirs(dir)
               else:
                  if os.listdir(dir):
                      print(dir+'文件已下载')
                      continue
               download(dir,lines[i+1].strip('\n'))
               #t = threading.Thread(target=download, args=(dir,lines[i+1].strip('\n')))
               #t.start()
            except Exception:
               traceback.print_exc()
                #break

def download(dir,url):
   try:
        print(dir + '开始下载')
        os.system('you-get' + ' -o ' +dir +' '+ url)
        print(dir + '下载成功')
   except Exception:
        print( dir +'下载失败')
        traceback.print_exc()
    

def CEF(path):
    """
    CLean empty files, 清理空文件夹和空文件
    :param path: 文件路径，检查此文件路径下的子文件
    :return: None
    """
    files = os.listdir(path)  # 获取路径下的子文件(夹)列表
    for file in files:
        print('Traversal at'+ file)
        #print(os.listdir(path+'\\'+file))
        if os.path.isdir(path+'\\'+file):  # 如果是文件夹
            if not os.listdir(path+'\\'+file):  # 如果子文件为空   
                os.rmdir(path+'\\'+file)  # 删除这个空文件夹
                print(path+'\\'+file + 'Dispose over!')
        elif os.path.isfile(file):  # 如果是文件
            if os.path.getsize(file) == 0:  # 文件大小为0
                os.remove(file)  # 删除这个文件
        
    print(path + 'Dispose over!')

#CEF('E:\\QLDownload')
#readFileTwo('ykaddr.txt')
        