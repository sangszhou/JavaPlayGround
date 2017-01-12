package scrooge_coin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

     *
     */
    public boolean isValidTx(Transaction tx) {
        for (Transaction.Input input : tx.getInputs()) {
            if (utxoPool.contains(new UTXO(input.prevTxHash, input.outputIndex)) == false) {
                return false;
            }
        }

        double inSum = 0;

        for (int i = 0; i < tx.getInputs().size(); i++) {
            Transaction.Input input = tx.getInput(i);
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            Transaction.Output txOutput = utxoPool.getTxOutput(utxo);
            inSum += txOutput.value;
            if(!Crypto.verifySignature(txOutput.address, tx.getRawDataToSign(i), input.signature)) {
                return false;
            }
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
        for (Transaction.Output output : tx.getOutputs()) {
            if (output.value < 0) return false;
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
        List<Transaction> transactions = new ArrayList<Transaction>();

        for (Transaction transaction : possibleTxs) {
            if (isValidTx(transaction)) {
                transactions.add(transaction);

                // do i need to delete all the utxo with signature prevTxHash?
                for(int i = 0; i < transaction.getInputs().size(); i ++) {
                    Transaction.Input input = transaction.getInput(i);
                    utxoPool.removeUTXO(new UTXO(input.prevTxHash, input.outputIndex));
                }

                for(int i = 0; i < transaction.getOutputs().size(); i ++) {
                    UTXO utxo = new UTXO(transaction.getHash(), i);
                    Transaction.Output output = transaction.getOutput(i);
                    utxoPool.addUTXO(utxo, output);
                }
            }
        }

        Transaction[] result = new Transaction[transactions.size()];
        for(int i = 0; i < result.length; i ++) {
            result[i] = transactions.get(i);
        }

        return result;
    }

}
