# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

import scrapy


class SinaItem(scrapy.Item):
    _id=scrapy.Field()
    title=scrapy.Field()
    pub_date=scrapy.Field()
