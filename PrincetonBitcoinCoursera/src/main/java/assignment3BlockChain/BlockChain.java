//package assignment3BlockChain;
//// Block Chain should maintain only limited block nodes to satisfy the functions
//// You should not have all the blocks added to the block chain in memory
//// as it would cause a memory overflow.
//
//import java.util.Deque;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//
//public class BlockChain {
////    static class BlockNode {
////        List<BlockNode> nodes;
////        int maxHeight;
////        Map<ByteArrayWrapper, BlockNode> candidateBlocks;
////        TransactionPool transactionPool;
////    }
//    public static final int CUT_OFF_AGE = 10;
//
//    BlockChain parent;
//    Block head;
//    // unused transaction pool
//    TransactionPool confirmedTransactions;
//    TransactionPool newCandidateTransactions;
//    int height;
//
//    /**
//     * create an empty block chain with just a genesis block. Assume {@code genesisBlock} is a valid
//     * block
//     */
//    public BlockChain(Block genesisBlock) {
//        // IMPLEMENT THIS
//        head = genesisBlock;
//
//        height = 1;
//        confirmedTransactions = new TransactionPool();
//    }
//
//    /**
//     * Get the maximum height block
//     */
//    public Block getMaxHeightBlock() {
//        // IMPLEMENT THIS
//    }
//
//    /**
//     * Get the UTXOPool for mining a new block on top of max height block
//     *
//     * From this method, we can predict that UTXOPool is associated with BlockChain(Fork)
//     * what's the max height block anyway???
//     *
//     */
//    public UTXOPool getMaxHeightUTXOPool() {
//        // IMPLEMENT THIS
//        //how to convert block to utxoPool?
//        UTXOPool utxoPool = new UTXOPool();
//        Block block = getMaxHeightBlock();
//
//        for (Transaction transaction : block.getTransactions()) {
//            for(int i = 0; i < transaction.numOutputs(); i ++) {
//                Transaction.Output output = transaction.getOutput(i);
//                UTXO utxo = new UTXO(transaction.getHash(), i);
//                utxoPool.addUTXO(utxo, output);
//            }
//        }
//
//        return utxoPool;
//    }
//
//    /**
//     * Get the transaction pool to mine a new block
//     */
//    public TransactionPool getTransactionPool() {
//        // IMPLEMENT THIS
//        return this.transactionPool;
//    }
//
//    /**
//     * Add {@code block} to the block chain if it is valid. For validity, all transactions should be
//     * valid and block should be at {@code height > (maxHeight - CUT_OFF_AGE)}.
//     * <p>
//     * <p>
//     * For example, you can try creating a new block over the genesis block (block height 2) if the
//     * block chain height is {@code <=
//     * CUT_OFF_AGE + 1}. As soon as {@code height > CUT_OFF_AGE + 1}, you cannot create a new block
//     * at height 2.
//     *
//     * @return true if block is successfully added
//     *
//     * how  to get the height?
//     */
//    public boolean addBlock(Block block) {
//        // IMPLEMENT THIS
//
//    }
//
//    /**
//     * Add a transaction to the transaction pool
//     */
//    public void addTransaction(Transaction tx) {
//        // IMPLEMENT THIS
//        transactionPool.addTransaction(tx);
//    }
//}