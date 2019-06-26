import random,string,re
uname_new=''
all_uname=[]
sum = 0
while sum>0:
    str = []
    lenght=10
    lenght1=int(lenght)
    while lenght1>0:
        uname = random.choice('1234567890'+string.ascii_letters)
        str.append(uname)
        uname_new = ''.join(str)
        lenght1 -=1
    if re.match('',uname_new):
        all_uname.append(uname_new)
        sum -= 1
for i in range(2,len(all_uname)):
    print(all_uname[i-2])