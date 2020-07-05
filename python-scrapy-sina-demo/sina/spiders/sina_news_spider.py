import re
from datetime import datetime

import scrapy
from bs4 import BeautifulSoup

import traceback
from sina.items import SinaItem

class SinaNewsSpider(scrapy.Spider):
    # 爬虫名字
    name = "sina_news"
    # 爬虫初始url
    start_urls = [
        'https://news.sina.com.cn/'
    ]
    custom_settings = {
        "LOG_LEVEL": 'ERROR'
    }

    # 此处对响应信息进行解析
    def parse(self, response):
        # print(response.url)
        # print(response.body)
        soup = BeautifulSoup(response.body, 'html.parser')
        tags = soup.find_all('a', href=re.compile(r'\d{4}-\d{2}-\d{2}.*?.shtml'))
        for tag in tags:
            # print(tag.get('href'))
            # print(tag.text.strip())
            # 将解析出来的url再次传递给scrapy发送请求，并指定返回结果的处理方法
            yield scrapy.Request(tag.get('href').strip(), callback=self.parse_details_and_continue_crawling)

    def parse_details_and_continue_crawling(self, response):
        '''
        第一步是解析当前页面
        :param response:
        :return:
        '''
        # print(response.url)
        # print(response.body)
        soup = BeautifulSoup(response.body, 'html.parser')
        try:
            # 解析标题
            title = self.extract_title(soup)
            if not title:
                raise Exception('title not found exception ' + response.url)
            # 解析时间
            pub_date = self.extract_date(soup)
            if not pub_date:
                raise Exception('pub_date not found exception ' + response.url)
            # else:
            #     print(pub_date)


            # 封装Item
            yield SinaItem(_id=response.url,title=title,pub_date=pub_date)
        except Exception as e:
            self.logger.error(str(e))
            # self.logger.error(traceback.format_exc())

        '''
        第二步解析新的url继续爬虫
        
        '''
        tags = soup.find_all('a', href=re.compile(r'\d{4}-\d{2}-\d{2}.*?.shtml'))
        for tag in tags:
            # print(tag.get('href'))
            # print(tag.text.strip())
            # 将解析出来的url再次传递给scrapy发送请求，并指定返回结果的处理方法
            yield scrapy.Request(tag.get('href').strip(), callback=self.parse_details_and_continue_crawling)


    def extract_title(self, soup):
        selectors = ['h1.main-title', 'h1#artibodyTitle', 'div.crticalcontent h1', 'h1#main_title',
                     'div.l_articleMain h1.l_tit', '.v-page-left h2']
        for selector in selectors:
            title = soup.select_one(selector)
            if title:
                return title.text.strip()

    def extract_date(self, soup):
        selectors = ['.date-source .date', '#page-tools .time-source .titer', 'div.crticalcontent div.txtdetail',
                     "#pub_date", ".news-info .time"
            , ".page-info .time-source", ".l_articleMain .l_infoBox span",
                     ".video-cont .text-video .bg-video .play-times"]
        pub_date = None
        for selector in selectors:
            pub_date = soup.select_one(selector)
            if pub_date:
                break
        if pub_date:
            date_pattern = r'(\d{4}).*?(\d{2}).*?(\d{2}).*?(\d{2}).*?(\d{2})'
            re_matches = re.findall(date_pattern, pub_date.text.strip())
            if re_matches:
                y, m, d, h, minute = [int(x) for x in re_matches[0]]
                return datetime(y, m, d, h, minute)
            raise Exception('date parse exception ' + pub_date.text.strip())
