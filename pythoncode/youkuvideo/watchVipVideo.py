#-*-coding:utf-8-*-

import webbrowser

class WatchVipVideo():

    def OpenUrl(self):
        str=""
        str1=self.videourl
        str2=self.channel
        if str2=="Channel 1":
            str="http://www.wmxz.wang/video.php?url="
        elif str2=="Channel 2":
            str="http://goudidiao.com/?url="
        elif str2 == "Channel 3":
            str="http://api.baiyug.cn/vip/index.php?url="
        elif str2 == "Channel 4":
            str="http://www.a305.org/weixin.php?url="
        elif str2=="Channel 5":
            str="http://www.vipjiexi.com/tong.php?url="
        elif str2 == "Channel 6":
            str="http://jx.aeidu.cn/index.php?url="
        elif str2 == "Channel 7":
            str="http://www.sonimei.cn/?url="
        elif str2=="Channel 8":
            str="https://api.vparse.org/?url="
        elif str2 == "Channel 9":
            str="https://jx.maoyun.tv/index.php?id="
        elif str2 == "Channel 10":
            str="http://pupudy.com/play?make=url&id="
        elif str2=="Channel 11":
            str="http://www.qxyingyuan.vip/play?make=url&id="
        elif str2 == "Channel 12":
            str="http://appapi.svipv.kuuhui.com/svipjx/liulanqichajian/browserplugin/qhjx/qhjx.php?id="
        elif str2 == "Channel 13":
            str="http://api.xfsub.com/index.php?url="
        elif str2 == "Channel 14":
            str="https://jiexi.071811.cc/jx.php?url="
        elif str2 == "Channel 15":
            str="http://q.z.vip.totv.72du.com/?url="
        elif str2=="Channel 16":
            str="http://jx.api.163ren.com/vod.php?url="
        elif str2 == "Channel 17":
            str="http://www.sfsft.com/admin.php?url="
        elif str2 == "Channel 18":
            str="http://v.renrenfabu.com/jiexi.php?url="
        else:
            str="https://api.vparse.org/?url="
        
        webbrowser.open(str+str1)


if __name__ == '__main__':
    watchVipVideo  = WatchVipVideo()
    watchVipVideo.channel = 'Channel 1'
    watchVipVideo.videourl = 'http://v.youku.com/v_show/id_XMzgzOTgyMzc4MA==.html?spm=a2hmv.20009921.posterMovieGrid86981.5~5~5~1~3!2~A&s=c6c62a475a5d4a14ab48'
    watchVipVideo.OpenUrl()