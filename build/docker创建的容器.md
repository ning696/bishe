docker run -d --name my-redis -p 6379:6379 -e REDIS_PASSWORD=Zc112325! redis:latest redis-server --requirepass Zc112325!


docker run -d --name Myminio -p 9002:9000 -p 9003:9001 -e "MINIO_ROOT_USER=minioadmin" -e "MINIO_ROOT_PASSWORD=minioadmin" -v E:\dockervolumes\minio-data:/data minio/minio server /data --console-address :9001



 docker run -d --name mysql -p 3306:3306 -e TZ=Asia/Shanghai -e MYSQL_ROOT_PASSWORD=Zc112325! -e MYSQL_ROOT_HOST=% -v E:\dockervolumes\mysql\data:/var/lib/mysql mysql


docker run -d --name oj-rabbit-dev -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 rabbitmq:3.8.30-management