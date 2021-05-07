
public class HashTable {

	protected HashList[] arr;
	private int size;
	private int wordCount;
	
	public HashTable(int size) {       // An array of HashLists, which implements a Hash Table using Chaining with every cell as LinkedList. 
		this.arr=new HashList[size];
		this.size=size;
		this.init(size);
		this.wordCount=0;
	}
	
	private void init(int size) {         // Initializing every cell(Hash list).
		for(int i=0; i<size; i++) {
			this.arr[i]=new HashList(size);
		}
	}
	
	public int hashFunction(String str) {   // Hash function which uses the values of every character in the received string to turn into an int according to 
			int output=0;                   // the ASCII table. after that we use modolo function to make sure the number we return is within limits of the array.
			for(int i=0; i<str.length(); i++)
				output=output+str.charAt(i);
			return output%this.size;
	}
	
	public void add(String str,int index) {            // adding a new element in the i'th cell (i'th list) and updating wordCount.
		HashListElement tmp=new HashListElement(str);
		this.arr[index].Add(tmp);
		this.wordCount=this.wordCount+1;
	}
	
	public int getWordCount() {       // a function and a field that counts how many words there are in the HashTable because we need to know later
		return this.wordCount;        // to compare to the threshold of the spam words(better than counting in a FOR loop costing O(n)).
	}
		
		
}
