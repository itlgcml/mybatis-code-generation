# support-generator

#### 项目介绍
基于mybatis-generator扩展

自定义插件\
提供
1. insert
2. insertBatch
3. getByKey
4. update
5. findEntitys
6. getEntitysCount

#### 使用说明

1. config.properties 中配置数据库连接
2. 在generatorConfiguration.xml中配置需要逆向生成的数据库表
3. 在generatorConfiguration.xml 中配置 mysql-connector-java-5.1.38.jar的绝对路径，有知道相对路径怎么配的告知一下
4. config.properties 中配置适合项目的包路径，方便直接复制

#### version 1.1
1. 支持model层的生成了，当前仅支持tb_开头的表名
2. 数据库jar包支持调用项目内文件了，不需要手动更改xml配置

#### 鸣谢

https://gitee.com/xxxcxy/support-generator

可以去点个赞

