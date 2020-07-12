# 项目介绍

在线账单，需要自己手动记录每一笔支出，提供统计图用于分析支出占比等，最终实现节流的目标。

# 前端

html页面直接挂在vue.js

报表使用的v-charts

# 后端

json-server实现的一个简单的rest接口

# 部署

前端挂在nginx上，后端通过json-server启动也挂在nginx上。

因为是windows部署，所以nginx和json-server都是通过winsw来实现服务化启动。

```

location /bill-web-app {
    alias D:/dev/applications/bill/bill-web-app/;
    index index.html;
}
location ^~ /bill-rest-app/ {
    proxy_pass  http://localhost:3000/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header REMOTE-HOST $remote_addr;
}
```

