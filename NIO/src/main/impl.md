1:

Java channel 是全双工的，但是为什么 BlockingChannel 有两个 channel 呢？

2:

配置与作用

```
channel.configureBlocking(true);
channel.socket().setSoTimeout(readTimeoutMs);
channel.socket().setTcpNoDelay(true);
channel.socket().connect(new InetSocketAddress(host, port), connectTimeout);
```
