# -*- coding: utf-8 -*-

import traceback
import re
import os

def main(path = 'Win4000'):
    dirs = os.listdir(path)
    list_f = []
    for dir_f in dirs:
        print(path+'/'+dir_f)
        if os.path.isdir(path+'/'+dir_f):   
            files = os.listdir(path+'/'+dir_f)
            #print(len(files))        
            if len(files) == 0:
                os.removedirs(path+'/'+dir_f)
            else:
                for f in files:
                    #fpath = os.path.abspath(f)
                    if list_f.count(f) > 0:
                        os.remove(path+'/'+dir_f+'/'+f)
                        print('删除'+path+'/'+dir_f+'/'+f+'成功')
                    else:
                        list_f.append(f)
           if len(files) == 0:
                os.removedirs(path+'/'+dir_f)

    print(len(list_f))
 
if __name__ == '__main__':
    main()