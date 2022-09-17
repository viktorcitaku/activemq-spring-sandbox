# Spring Boot using Apache Camel to consume ActiveMQ messages

The goal of this project is to create idempotent consumers using the Redis Repository.

```
kubectl create deployment activemq-deployment -f deployment.yaml
kubectl expose deployment activemq-deployment --type=LoadBalancer --port=8161,61616,6379
```
