# -*-coding:utf-8-*-
import urllib.request
from bs4 import BeautifulSoup


def getHtml(url):
    """获取url页面"""
    headers = {'User-Agent':'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36'}
    req = urllib.request.Request(url,headers=headers)
    req = urllib.request.urlopen(req)
    content = req.read().decode('utf-8')

    return content


def getComment(url):
    """解析HTML页面"""
    html = getHtml(url)
    soupComment = BeautifulSoup(html, 'html.parser')

    comments = soupComment.findAll('span', 'short')
    onePageComments = []
    for comment in comments:
        # print(comment.getText()+'\n')
        onePageComments.append(comment.getText()+'\n')

    return onePageComments


if __name__ == '__main__':
    f = open('review.txt', 'w', encoding='utf-8')
    for page in range(10):  # 豆瓣爬取多页评论需要验证。
        url = 'https://book.douban.com/subject/2353023/comments?start=' + str(20*page) + '&limit=20&sort=new_score&status=P'
        for i in getComment(url):
            f.write(i)
        print('\n')
