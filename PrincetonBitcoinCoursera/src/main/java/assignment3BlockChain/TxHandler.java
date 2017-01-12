package assignment3BlockChain;


import java.util.HashSet;
import java.util.Set;

public class TxHandler {

    UTXOPool utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        // IMPLEMENT THIS
        this.utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     * values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS

        // check `all outputs claimed by tx are in the current UTXO pool` [1]
        // 这一点非常难理解，input.signature 是 recipient 自己放上去的，而不是由上一个人发过来的
        // 好好思考下
        for (Transaction.Input input : tx.getInputs()) {
            if (utxoPool.contains(new UTXO(input.prevTxHash, input.outputIndex)) == false) {
                return false;
            }
        }

        // check `the signature on each input of tx are valid` [2]
        // we can get the public key from utxo pool
        // what's message? 应该就是 transaction id
        // how to get the public key ?
        double inSum = 0;

        for(int i = 0; i < tx.getInputs().size(); i ++) {
            Transaction.Input input = tx.getInput(i);
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            Transaction.Output txOutput = utxoPool.getTxOutput(utxo);
            inSum += txOutput.value;
            Crypto.verifySignature(txOutput.address, tx.getRawDataToSign(i), input.signature);
        }

        // check `no UTXO is claimed multiple times by tx` [3]
        // check there is no duplicated input from same tx
        // since Transaction has no compare method, we take the advantage of UTXO
        Set<UTXO> assembliedInputs = new HashSet<>();
        for (Transaction.Input input : tx.getInputs()) {
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            if (assembliedInputs.contains(utxo))
                return false;
            assembliedInputs.add(utxo);
        }

        // [4][5]
        double outSum = 0;
        for (Transaction.Output output: tx.getOutputs()) {
            if(output.value < 0) return false;
            outSum += output.value;
        }

        if (outSum > inSum) {
            return false;
        }

        return true;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
        return null;
    }

}
