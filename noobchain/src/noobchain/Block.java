package noobchain;

import java.util.Date;

public class Block {
	
	public String hash;
	public String previousHash;
	private String data; //our data will be a simple message.
	private long timeStamp; //as number of milliseconds since 1/1/1970.
	private int nonce;

	//Block Constructor.
	public Block(String data,String previousHash ) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();//make sure this is done after all the other values are set
	}
	
	//This is going to calculate hash of the data
	public String calculateHash(){
		String calculatedHash = StringUtil.applySha256(previousHash +
				Long.toString(timeStamp)+
				Integer.toString(nonce)+
				data);
		return calculatedHash;
	}
	
	
	public void mineBlock(int difficulty) {
		String ctarget = new String (new char[difficulty]);
		String target = ctarget.replace('\0', '0');
		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
			System.out.println("New Calculated Hash ::: "+hash);
			System.out.println("Target :::"+target);
		}
		System.out.println("Block Mined!!! : " + hash);
	}
	
}
