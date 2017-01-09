1: Logger 不 work, log 的默认输出是哪里？

2: ServerBootstrap 和 Bootstrap 的异同点是什么

3: server 的 handler 和 childHandler 分别是干什么的？

handler 是为 bossGroup 工作的，而 childHandler 应该是分配给每一个连接的，工作在 workerGroup 上