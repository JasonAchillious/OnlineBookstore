import time
import random

a1=(2019,1,1,0,0,0,0,0,0)
#begin time
a2=(2021,12,31,23,59,59,0,0,0)
#end time
start=time.mktime(a1)
end=time.mktime(a2)

for i in range(0):
    t=random.randint(start,end)
    date=time.localtime(t)
    print(str(date.tm_year)+"-"+
          str(date.tm_mon)+
          '-'+str(date.tm_mday)+
          " "+ str(date.tm_hour)+
          ":"+str(date.tm_min)
          +":"+str(date.tm_sec))