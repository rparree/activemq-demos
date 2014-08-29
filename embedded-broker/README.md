# Embedded demos (and Persistence)

## EmbeddedDemo

Simple embedded example with a vm connector on thread sens messages, main thread consumes them.

## SpringDemo

This demo also demonstrates the use of a jdbc  journalled 

```bash
$ watch -d -c -n 5 'mysql activemq  --execute "select count(*) from ACTIVEMQ_MSGS"'
```

It will take ~30 seconds before the checkpoint