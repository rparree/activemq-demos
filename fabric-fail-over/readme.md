fa### Setup the fabric

Start fuse in docker:

```
docker run -Pd  \
       -p 2181:2181 \
       -p 8181:8181 \
       -p 8101:8101 \
       -p 18000-18005:18000-18005 \
       -p 19000-19005:19000-19005 \
       --name mq-fabric \
        rparree/jboss-fuse-full-admin
```

Create the Fabric

```
JBossFuse:karaf@root> fabric:create --zookeeper-password masterkey
```

create the containers

```
JBossFuse:karaf@root> container-create-child --jmx-user admin --jmx-password admin <root> east 2
JBossFuse:karaf@root> container-create-child --jmx-user admin --jmx-password admin <root> west 2
````

Optionally delete the broker on the root container

```
JBossFuse:karaf@root> config:delete org.fusesource.mq.fabric.server.<tab>
JBossFuse:karaf@root> shell:exec mv etc/org.fusesource.mq.fabric.server-default.cfg .
```

Create the east-side
```
JBossFuse:karaf@root> fabric:mq-create  --group amq-east \
                 --networks amq-west \
                 --networks-username admin --networks-password admin \
                 --ports "openwire=\${port:18000,18100}" \
                 --data /tmp/activemq/data/amq-east \
                 --assign-container east1,east2 demo-east-profile
```

Create the west-side
```
JBossFuse:karaf@root> fabric:mq-create  --group amq-west \
                 --networks amq-east \
                 --networks-username admin --networks-password admin \
                 --ports "openwire=\${port:19000,19100}" \
                 --data /tmp/activemq/data/amq-west \
                 --assign-container west1,west2 demo-west-profile
```
