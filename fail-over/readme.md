## start the brokers

This demo uses two fuse instances sharing the same kahadb repo via
docker volumes.

Start the first broker and map to port 61616

```shell
docker run -d   -p 8181:8181 -p 8101:8101 \
    --name fuse1  -p 61616:61616 \
     -v /tmp/fuse/kahadb:/opt/jboss/jboss-fuse/kahadb \
     rparree/jboss-fuse-full-admin
```

Start the first broker and map to port 61617 

```shell
docker run -d   -p 8182:8181 -p 8102:8101 \
    --name fuse2  -p 61617:61616 \
     -v /tmp/fuse/kahadb:/opt/jboss/jboss-fuse/kahadb \
     rparree/jboss-fuse-full-admin
```