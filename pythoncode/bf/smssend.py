# -*- coding: utf-8 -*-

import sys
from twilio.rest import Client

class SmsSend():
      
    def sendSms(self,smstext):
        self.account_sid = "ACd4d865c7dd7d7a6e74713d94aec45659"
        self.auth_token = "6145c0d0768e989245b210dbe75c2888"
        self.client = Client(self.account_sid, self.auth_token)
        self.fromPhone = '+13852176315'
        self.toPhone = '+8613167302958'
         # 区号+你的手机号码  # 你的 twilio 电话号码 # 消息
        message = self.client.messages.create(to=self.toPhone, from_=self.fromPhone,body=smstext)
        print(self.fromPhone +'|'+ self.toPhone +'|'+ smstext)
        
    def show(self):
        print('*' * 60)
        print('\t\n短信发送测试')
        print('*' * 60)
        #self.toPhone = input('请输入发送到的手机号')
        smstext = input('输入发送的信息')
        self.sendSms(smstext)

if __name__ == '__main__':
    smsSend  = SmsSend()
    smsSend.show()