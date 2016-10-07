package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HashTable {

	//public static Node[] hashTable;
	public static List<Node> hashTable = new ArrayList<>();
	
	public List<Node> getHashTable() {
		return hashTable;
	}

	public void setHashTable(List<Node> hashTable) {
		this.hashTable = hashTable;
	}
	
	public void addHashTable(Node node) {
		this.hashTable.add(node);
	}

	private static class Node{
		private String key;
		private Integer val;
		private Node next;
		private Node prev;
		private List<Integer> posList = new ArrayList<>();
		
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public Integer getVal() {
			return val;
		}
		public void setVal(Integer val) {
			this.val = val;
		}
		public void incrementVal(Integer step) {
			this.val += step;
		}
		
		public Node getNext() {
			return next;
		}
		public void setNext(Node next) {
			this.next = next;
		}
		public Node getPrev() {
			return prev;
		}
		public void setPrev(Node prev) {
			this.prev = prev;
		}
		public List getPosList() {
			return posList;
		}
		public void addPos(Integer pos) {
			this.posList.add(pos);
		}
		
		public Node(String key, Integer val, Node next, Node prev, List<Integer> posList) {
			super();
			this.key = key;
			this.val = val;
			this.next = next;
			this.prev = prev;
			this.posList = posList;
		}
	}
	
	public static int findPow(Integer length){
		int p = 0;
		while(length>=2){
			p+=1;
			length /= 2;			
		}
		
		if(length > 1)
			p+=1;
		
		return p;
	}
	
	public static int getTableLength(Integer length){
		int power = findPow(length);
		int[] primes = {53,53,53,53,53,53,97,193,389,769,1543,3079,6151,12289,24593,49157,98317,196613,393241,786433,1572869,
	              3145739,6291469,12582917,25165843,50331653,100663319,201326611,402653189,805306457,1610612741};
		
		if(power > 31)
			return primes[30];
		
		return primes[power - 1];		
	}
	
	public static double generateHashVal(String word){
		double hash = 7;
		word=word.replaceAll(".!()?-", "").toLowerCase();
		for (int i = 0; i < word.length(); i++) {
		    hash = hash*31 + word.charAt(i);
		}
		
		return hash;
	}
	
	public static Boolean increase(String key, Integer pos, int hashKey){
		Node res = find(key,hashKey);
		if(res != null){
			res.incrementVal(1);
			res.addPos(pos);
			return true;
		}
		return false;
	}
	
	public static Node find(String word, int hashKey){
		//Node currNode = hashTable[hashKey];
		System.out.println(word + hashKey);
		Node currNode = hashTable.get(hashKey);
		while(currNode.getVal() != -1){
			if(currNode.getKey().equalsIgnoreCase(word))
				return currNode;
			currNode = currNode.getNext();
		}
		return null;
	}
	
	public static Boolean insert(String word, int pos){
		double hashVal = generateHashVal(word);
		
		int hashKey =(int) (hashVal % hashTable.size());
		System.out.println(hashKey + " "+ hashTable.size());
		
		if(increase(word,pos,hashKey))
			return true;
		else{
			List<Integer> posList = new ArrayList<>();
			posList.add(pos);
			Node newWord = new Node(word, 1, null, null, posList);
			/*hashTable[hashKey].setPrev(newWord);
			newWord.setNext(hashTable[hashKey]);
			hashTable[hashKey] = newWord;*/
			hashTable.get(hashKey).setPrev(newWord);
			newWord.setNext(hashTable.get(hashKey));
			hashTable.set(hashKey, newWord);
			return true;
		}
	}
	
	public static Boolean delete(String key){
		double hashVal = generateHashVal(key);
		int hashKey = (int) (hashVal % hashTable.size());
		Node wordNode = find(key,hashKey);
		
		if(wordNode != null){
			System.out.println("Removing "+wordNode.getKey()+" from positions "+wordNode.getPosList().toString());
			if(wordNode.getPrev() != null)
				wordNode.getPrev().setNext(wordNode.getNext());
			wordNode.getNext().setPrev(wordNode.getPrev());
			wordNode.setVal(-1);
			return true;
		}
		else
			return false;
		
	}
	
	public static void listAllKeys(){
		PrintWriter writer;
		String text ="";
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
			writer.println("Current state of hash table");
			for(int i=0; i<hashTable.size();i++){
				//Node currNode = hashTable[i];
				Node currNode = hashTable.get(i);
				while(currNode.getVal() != -1){
					text += "key: "+currNode.getKey()+", value: "+currNode.getVal()+", positions: "+currNode.getPosList().toString();
					currNode = currNode.getNext();
				}
				System.out.println("Current bucket: " + i + ", Entries: " + text);
				writer.println("Current bucket: " + i + ", Entries: " + text);
				text = "";
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args){
		//System.out.println(generateHashVal("but"));
		//HashTable hash = new HashTable();
				
		File file = new File("input.txt");
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String temp = null;
		    String lines = null;
		    
		    while ((temp = reader.readLine()) != null) {
		        lines += temp;
		    }
		    
		    String[] text = lines.split("\\s+");
		    int tableLength = getTableLength(text.length);
		    
		    //List<Node> hashTable = new ArrayList<>(tableLength);
		    
		    for(int i=0; i< tableLength; i++){
		    	List<Integer> pos = new ArrayList();
		    	pos.add(-1);
		    	//hashTable[i] = new Node(null, -1, null, null, pos);
		    	hashTable.add(i, new Node(null, -1, null, null, pos)) ;
		    }
		    
		    for(int i=1; i< text.length; i++){
		    	insert(text[i-1],i);
		    }
		    
		    System.out.println("After initialization");
		    listAllKeys();
		    
		    String toBeRemoved = "encounter";
		    System.out.println("After deletion");
		    if(delete(toBeRemoved)){
		    	System.out.println("All instances of '" + toBeRemoved + "' were removed successfully!");
		    	listAllKeys();
		    }
		    else{
		    	System.out.println("No instance found for " + toBeRemoved);
		    	listAllKeys();
		    }
		    
		    toBeRemoved = "with";
		    System.out.println("After deletion");
		    if(delete(toBeRemoved)){
		    	System.out.println("All instances of '" + toBeRemoved + "' were removed successfully!");
		    	listAllKeys();
		    }
		    else{
		    	System.out.println("No instance found for " + toBeRemoved);
		    	listAllKeys();
		    }
		    	
		    

		    
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}
	}
}

