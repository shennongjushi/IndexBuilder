# IndexBuilder
## Functionality
```
1. It's a index builder pipeline
2. It consums the ad data from rabbitMQ where the Web Crawler sends to.
```
## Steps
### 1. Start rabbitMQ
Check the queue status on http://localhost:15672
```
rabbitmq-server
```
### 2. Start memcached server
```
memcached -d -p 11211
```
### 3. Setup Mysql(Mac Environment)
```
Start MySQL Server, setup the password
Use MySQL Workbench to create the database and ad table.
For example, database:demo, table: ad
```

### 3. Run WebCrawer to get ads data
```
Download another repo "WebCrawler"(https://github.com/shennongjushi/WebCrawler)
Add some query to the Queue: queue_feeds (copy query from rawQuery3.txt)
WebCrawer will send the ad data to the Queue: q_product
```

### 4. Run IndexBuilder
```
It will consum the ad datas from Queue: q_product, do the "buildInvertIndex" and "buildForwardIndex".
In the end, you should see some rows are inserted into the table "ad"
```

