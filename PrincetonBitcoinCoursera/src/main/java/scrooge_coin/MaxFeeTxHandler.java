package scrooge_coin;

/**
 * Created by xinszhou on 12/01/2017.
 */
public class MaxFeeTxHandler {

    UTXOPool utxoPool;
    /**
     * Creates a public ledger whose current UTXOPool
     * (collection of unspent transaction outputs) is utxoPool.
     * This should  make a defensive copy of utxoPool by using
     * the UTXOPool (UTXOPool uPool) constructor.
     */
    public MaxFeeTxHandler(UTXOPool utxoPool) {
        this.utxoPool = utxoPool;
    }

    /**
     * Returns true if
     * (1) all outputs claimed by tx are in the current UTXO pool
     * (2) the signatures on each input of tx are valid
     * (3) no UTXO is claimed multiple times by tx
     * (4) all of tx’s output values are non-negative
     * (5) the sum of tx’s input values is greater than or equal
     * to the sum of its output values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        return false;
    }

    /**
     * Handles each epoch by receiving an unordered array of
     * proposed transactions, checking each transaction for
     * correctness, returning a mutually valid array of accepted
     * transactions, and updating the current UTXO pool as
     * appropriate.
     *
     * 思路: 背包问题？
     * 假设 input transactions 之间没有依赖关系，那就先判断
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        return null;
    }

}
