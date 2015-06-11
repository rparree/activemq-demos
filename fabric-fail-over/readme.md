### Setup the fabric

```
JBossFuse:karaf@root> fabric:create --zookeeper-password masterkey --global-resolver localip --wait-for-provisioning
```

```create the containers
JBossFuse:karaf@root> container-create-child --jmx-user admin --jmx-password admin root East 2
JBossFuse:karaf@root> container-create-child --jmx-user admin --jmx-password admin root West 2
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
                 --assign-container East1,East2 demo-east-profile
```

Create the west-side
```
JBossFuse:karaf@root> fabric:mq-create  --group amq-west \
                 --networks amq-est \
                 --networks-username admin --networks-password admin \
                 --ports "openwire=\${port:19000,19100}" \
                 --data /tmp/activemq/data/amq-west \
                 --assign-container West1,West2 demo-west-profile
```