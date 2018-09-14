# -*- coding: UTF-8 -*-
 
import smtplib
import traceback
from email.mime.text import MIMEText
from email.utils import formataddr

class SendMail():

    def mail(self,text):
        self.my_sender='liangguangpu66@163.com'    # 发件人邮箱账号
        self.my_pass = 'mm123456'           # 发件人邮箱密码(当时申请smtp给的口令)
        self.my_user='1158200486@qq.com'      # 收件人邮箱账号，我这边发送给自己
        try:
            msg=MIMEText(text,'plain','utf-8')
            msg['From']=formataddr([self.my_sender,self.my_sender])  # 括号里的对应发件人邮箱昵称、发件人邮箱账号
            msg['To']=formataddr([self.my_user,self.my_user])              # 括号里的对应收件人邮箱昵称、收件人邮箱账号
            msg['Subject']="邮件主题-测试"                # 邮件的主题，也可以说是标题
            server=smtplib.SMTP("smtp.163.com", 25)  # 发件人邮箱中的SMTP服务器，端口是80
            server.login(self.my_sender, self.my_pass)  # 括号中对应的是发件人邮箱账号、邮箱密码
            server.sendmail(self.my_sender,[self.my_user,],msg.as_string())  # 括号中对应的是发件人邮箱账号、收件人邮箱账号、发送邮件
            server.quit()# 关闭连接
            print('发送成功')
        except Exception:# 如果 try 中的语句没有执行
            print('发送失败\t\n')
            traceback.print_exc()

         
if __name__ == '__main__':
    sendMail  = SendMail()
    sendMail.mail('测试信息。。。。')