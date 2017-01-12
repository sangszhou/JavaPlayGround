**Very important:**

input of Transaction has a field called signature, this field is not come from above, rather, it is set by the people who start this transaction himself.


1: UTXO 是以前交易的结果，相当于已经存在的 block chain, 用它来 verify 到来的 transaction 是否合理

1.1: UTXO 是会变化的。如果确认一笔 transaction 有效，那么这笔 tx 将会加入到 UTXOPool 中，用来继续 verify 后续的请求

2: bitcoin 中的某个 output 只会被使用一次，但是这个 output 所在的 tx 会出现多次，出现的次数小于等于他在另外的 tx 中的 input

3: 如何验证某个 tx 是正确的？签名是怎么 work 的？是否需要追溯到创世块？
    从后面的介绍可以看出，没这个必要

4: Transaction 中有两个方法，分别是 getRawDataToSign 和 getRawTx, 其中 getRawDataToSign 包含一个参数 index, 
这个 index 是某个交易的 input index, 除此之外，他还会计算 all output 的 byte, 目前不知道它的目的是什么。
getRawTx 的意义则是所有 input 和 output 的集合，用来计算此次 TX 的 id


有几点需要注意:

1. 