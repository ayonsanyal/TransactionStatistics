A small rest based java api which records the statistics on transactions for every 60 seconds.
This is a spring boot based application developed on java 8 and spring.
This API expose  two endpoints :

1. POST /transactions to post new transactions.

2.GET /statistics  to retireve the statistics.

To Run this application ,please follow the following steps:

1. Go to the root folder of this project and trigger mvn spring-boot:run command.

2.On successfull completion,all test will run follwed by build success and then the server will be up and running on 8080 port.

3.Once the server comes up,the restful endpoints can be executed either by rest client or by httpie or curl by giving inputs.

4.One sample json file is provided in the root folder for testing purpose.
