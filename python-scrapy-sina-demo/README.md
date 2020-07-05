# Introduction

this is a demo application for scrapy that crawls news on the [http://news.sina.com.cn](http://news.sina.com.cn) and stores the crawled news datas into mongoDB.

# Developing Environment

1. python environment: anaconda

> Anaconda is the standard platform for Python data science, leading in open source innovation for machine learning. Develop, manage, collaborate, and govern at scale with our enterprise platform. 
>
> **It contains python**, so we don't need to download python alonely.

> download: [https://www.anaconda.com/distribution/](https://www.anaconda.com/distribution/)

2. IDE: pycharm

> The Python & Django IDE with intelligent code completion, on-the-fly error checking, quick-fixes, and much more... 

> download: [https://www.jetbrains.com/pycharm/download/#section=windows](https://www.jetbrains.com/pycharm/download/#section=windows)

3. crawler framework: scrapy

> An open source and collaborative framework for extracting the data you need from websites. 
>
> In a fast, simple, yet extensible way.

> download: pip install scrapy

4. database: MongoDB

> MongoDB is a general purpose, document-based, distributed database built for modern application developers and for the cloud era. 

> download: [https://www.mongodb.com/download-center/community?jmp=docs](https://www.mongodb.com/download-center/community?jmp=docs)

5. mongoDB driver for python: pymongo

>  PyMongo is a Python distribution containing tools for working with MongoDB, and is the recommended way to work with MongoDB from Python 

> download: pip install pymongo

6. html parser for python: BeautifulSoup

>  Beautiful Soup: a library designed for screen-scraping HTML and XML. 

> download: pip install BeautifulSoup(contains in anaconda)

# Developing Processes

1. create the project

> use the scrapy command to create a new scrapy project's file structure.

> scrapy startproject sina

2. crawling first page

> name the spider with 'sina_news' and define the start_urls, and run the application with scrapy command.
>
> parse the response with beautifulSoup in the parse function and find the detail pages' url by regex

> scrapy crawl sina_news

3. crawling the details page

> encapsulate the url above to a request and  send it to scrapy with a new parse function that can parse the detail pages' response. In the same way, we use beautifulSoup to parse the resposne.

3. define item and store the item to json file

> define the item class and encapsulate the data crawled into an item class and use cmd params to store the crawled data into a json file

> scrapy crawl sina_news -o result.json -t json

5. store the item data into mongoDB

pipelines.py is to process item datas. with this file you can do something like store the item datas to database and so on. So you need to change this file in order to store item datas into mongoDB

settings.py is a config file for this application and you need to define the item_pipelines in this file for adding the pipeline process above. again, you can define the constants like dbhost,dbport,dbname in this file

> scrapy crawl sina_news

6. continue crawling and make the crawling to be a job

> we can get the detail pages' url in the first page, and also we can get the other detail pages' url from per detail page. Then, the spider is becoming bigger and bigger.
>
> Sometimes we want to stop the application and we want to restart it from the stopped location later, so we use job to do this.

> scrapy crawl sina_news -s JOBDIR=job