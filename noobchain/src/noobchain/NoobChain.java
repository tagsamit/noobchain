package noobchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.GsonBuilder;

public class NoobChain {
	
	public static ArrayList<Block> blockChain = new ArrayList<Block>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //list of all unspent transactions. 
	public static int difficulty = 5;
	public static Wallet walletA;
	public static Wallet walletB;
	public static float minimumTransaction;
	
	public static void main(String arg[])
	{
		//Setup Bouncey castle as a Security Provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		//create new wallets
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		
		//Test public and private keys
		System.out.println("Private and public keys:");
		System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
		System.out.println(StringUtil.getStringFromKey(walletA.publicKey));

		
		//Create a test transaction from WalletA to walletB 
		Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
		transaction.generateSignature(walletA.privateKey);
		//Verify the signature works and verify it from the public key
		System.out.println("Is signature verified");
		System.out.println(transaction.verifySignature());
		/*blockChain.add(new Block("Hi im the first block", "0"));
		System.out.println("Trying to Mine block 1..");
		blockChain.get(0).mineBlock(difficulty);
		
		blockChain.add( new Block("Yo im the second block",blockChain.get(blockChain.size()-1).hash));
		System.out.println("Trying to Mine block 2..");
		blockChain.get(1).mineBlock(difficulty);
		
		blockChain.add(new Block("Hey im the third block",blockChain.get(blockChain.size()-1).hash));
		System.out.println("Trying to Mine block 3..");
		blockChain.get(2).mineBlock(difficulty);
		
		System.out.println("Is the Chain valid::"+isChainValid());
				
		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);		
		System.out.println("The Block chain_______");
		System.out.println(blockchainJson);*/
	}	
	
	public static Boolean isChainValid() {
		
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		for (int i=1; i < blockChain.size(); i++) {
			currentBlock = blockChain.get(i);
			previousBlock = blockChain.get(i-1);
			
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ) {
				System.out.println("Current Has is not equal");
				return false;
			}
			
			if (!currentBlock.previousHash.equals(previousBlock.hash)) {
				System.out.println("Previous Has is not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
		}
		
		return true;
		
	}
	
}
