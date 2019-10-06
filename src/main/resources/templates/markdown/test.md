
# ElasticsearchSink Sink

## 1 语法

CREATE TABLE tableName(
	    colName colType,
	    ...
	 )WITH(
	    type = elasticsearch,
		address = address,
		indexName = indexName
		batchSize = size,
	    parallelism = parllNum
	 );


## 2 表结构定义
 
|参数名称|含义|
|----|---|
| tableName | 在sql中使用的名称;即注册到flink-table-env上的名称|
| colName | 字段名|
| colType | 字段类型|



## 3 参数
 
|参数名称|含义|是否必填|默认值|
|----|---|---|---|
|type | 输出表类型，elasticsearch | 是||
|address | es服务端点，127.0.0.1:9300 | 是||
|indexName | 索引名称 | 是||
|batchSize | 输出批次大小 | 是||
|parallelism | 并行度设置|否|1|


## 4 样例

CREATE TABLE sink(
		userId VARCHAR,
		cnt INT
	)WITH(
		type = elasticsearch,
		address = 127.0.0.1:9300,
		indexName = elasticsearchSink
		batchSize = 2,
		parallelism = 1
	);

