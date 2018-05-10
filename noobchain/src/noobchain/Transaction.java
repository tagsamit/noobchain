package noobchain;

import java.util.ArrayList;
import org.bouncycastle.*;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Transaction {

	public String transactionId;//hash of the transaction
	public PublicKey senderKey;
	public PublicKey reciepientKey;
	public float value;
	public byte[] signature;
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput> ();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput> ();
	
	private static int sequence = 0; //start of transaction and transaction count
	
	// Constructor: 
		public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
			this.senderKey = from;
			this.reciepientKey = to;
			this.value = value;
			this.inputs = inputs;
		}
		
		// This Calculates the transaction hash (which will be used as its Id)
		private String calculateHash() {
			sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
			return StringUtil.applySha256(
					StringUtil.getStringFromKey(senderKey) +
					StringUtil.getStringFromKey(reciepientKey) +
					Float.toString(value) + sequence
					);
		}
		
		public void generateSignature(PrivateKey privatekey) {
			String data = StringUtil.getStringFromKey(reciepientKey) + StringUtil.getStringFromKey(senderKey);
			this.signature = StringUtil.applyECDSASig(privatekey, data);
		}
		
		public boolean verifySignature() {
			String data = StringUtil.getStringFromKey(reciepientKey) + StringUtil.getStringFromKey(senderKey);
			return StringUtil.verifyECDSASig(senderKey, data, signature);
		}
		
		public boolean processTransaction() {
			
			if (!verifySignature()) {
				System.out.println("Transaction signature failes to verify");
				return false;
			}
			
			//gather transaction inputs, make sure they are unspent
			for (TransactionInput i: inputs) {
				i.UTXO = NoobChain.UTXOs.get(i.transactionOutputId);
			}
			
			//check if transaxtion is valid
			if(getInputValues() < NoobChain.minimumTransaction) {
				System.out.println("Transaction value too small" + getInputValues());
				return false;
			}
			
			
			return true;
		}
		
		//return sum of inputs
		public float getInputValues() {
			float total = 0;
			for(TransactionInput i: inputs) {
				if(i.UTXO == null) continue;
				total =+ i.UTXO.value;
			}
			
			return total;
		}
		
		//return sum of outputs
		public float getOutputValues() {
			float total = 0;
			for(TransactionOutput o: output) {
				total += o.value;
			}
			
			return total;
		}
		
}
