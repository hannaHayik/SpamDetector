import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class Messages implements Iterable{
	private Message[] arr;
	private HashTable[] hashTables;
	private int numOfMessages;
	
	public Messages() {}
	
	public void generateMessages(String str) {
        this.messageCreator(this.pathToString(str));
	}
	
	private void messageCreator(String str) {
		StringTokenizer stock=new StringTokenizer(str,"#");  // Splitting the string according to # which defines the limits of every message.
	    this.arr=new Message[stock.countTokens()];
	    this.numOfMessages=stock.countTokens();
		String[] strArr;
		int i=0;
		while(stock.hasMoreTokens()) {
			strArr=this.msgInfo(stock.nextToken());    // using msgInfo function that returns an array of 3 : sender,receiver,subject.
			Message newMsg=new Message(strArr[0],strArr[1],strArr[2]);   // create new object for every message and inserting to the array. 
			this.arr[i]=newMsg;
			i++;
		}
	}
	
	private String[] msgInfo(String str) {
		String[] a1=new String[3];
		StringTokenizer stock=new StringTokenizer(str,":\r\n");    // using ":" can tell us where is the From: and To: and split them.
		stock.nextToken();                                        // popping the "From:" out of the stock.
		String tmp=stock.nextToken();
		a1[0]=tmp;                                                 // inserting the sender's name.
		stock.nextToken();                                       // the same with the "To:"
		tmp=stock.nextToken();
		a1[1]=tmp;
		a1[2]="";
		while(stock.hasMoreTokens())
			a1[2]=a1[2]+stock.nextToken()+"\n";     // the rest is considered the subject and may be splitted by new lines (\n).
		return a1;
	}

	@Override
	public Iterator iterator() {
	 messageIterator iter=new messageIterator(this.arr);
	 return iter;
	}
	
	public String findSpams(String str,BTree tree) {
		Spams spamsArr=new Spams();
		spamsArr.generateSpams(this.pathToString(str));   // generating Spam words to the object using the received string according to pathToString function.
		String output="";
		boolean flag=true;
		for(int i=0; i<this.arr.length; i++) {
			if(!this.ifFriends(this.arr[i].getFrom(), this.arr[i].getTo(), tree))  // if the receiver and sender are friends then no need to check the message.
				for(int j=0; j<spamsArr.getNumberOfSpams()&flag; j++) 
					if(checkForSpams(this.hashTables[i],spamsArr.getSpam(j))) {  // using checkForSpams function to check if the message is a spam.
						if(output!="")
							output=output+",";
						output=output+i;
						flag=false;                 // using flag because if the message has more than one spam word then it won't get printed more than once.
				}
			flag=true;
		}
		return output;
	}
	
	private boolean checkForSpams(HashTable hashtable,Spam spamWord) {
		double counter=0;
		int indicate=hashtable.hashFunction(spamWord.getWord());  //The hash function will give us the index of the list that it will chained into if the message has this spam.
		for(int i=0; i<hashtable.arr[indicate].array.length; i++) {  // navigating to that list and checking how many of it's elements equals the spam word.
			if(hashtable.arr[indicate].array[i]!=null) {
			if(hashtable.arr[indicate].array[i].getData().equals(spamWord.getWord())) 
				counter=counter+1;
			}
		}    // using Double type because we are dealing with thresholds and percentages and not round numbers.
		return ((counter/(double)hashtable.getWordCount())>=(double)(spamWord.getPercent()/100.0));  
	}
	
	private boolean ifFriends(String friend1, String friend2, BTree tree) {
		String test1=friend1+" "+"&"+" "+friend2;      // using the search function of the btree we could make sure if the two people are friends
		String test2=friend2+" "+"&"+" "+friend1;      // we check both options because we can't know the order in the Btree.
		return ((tree.root.search(tree.root, test1)!=null)||(tree.root.search(tree.root, test2)!=null));
	}
	
	public String pathToString(String str) {  // Converts the received path to a whole string but adds the new lines character to define the limits of every line.
		String output="";                     // when we asked on the forum about using Stream from the java.util package they said it was allowed,
		Path p1=Paths.get(str);               // we choose Stream because the Scanner had some problems receiving certain characters and the best way 
		Stream<String> obj = null;            // and the most elegant is to use Stream and turn it into an Array with adding the limits of every line.
		try {
			obj=Files.lines(p1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] outputArr=obj.toArray(String[]::new);
		for(int i=0; i<outputArr.length; i++) {
			output=output+outputArr[i];
			output=output+"\n";
		}
		return output;
	}
	
	private boolean isNumeric(String str)   // function to check if the receieved String is numeric.
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public void createHashTables(String size) {  // creates an array of HashTables in accordance to the messages array.
		if(!this.isNumeric(size))
			throw new RuntimeException();
		int size1=Integer.parseInt(size);   
		this.hashTables=new HashTable[this.arr.length];    
		for(int i=0; i<this.arr.length; i++) 
			this.hashTables[i]=new HashTable(size1);   // creating new objects for every cell.
		for(int j=0; j<this.arr.length; j++)
			this.fillTable(j);                  // filling every Hash Table according to the messages array.
	}
	
	private void fillTable(int index) {
		StringTokenizer stock=new StringTokenizer(this.arr[index].getSubject()," ");   // every two words are seperated by " " so we split them
		while(stock.hasMoreTokens()) {                                 // and insert them into the hashtable using the hashfunction.
			String tmp=stock.nextToken();
			this.hashTables[index].add(tmp,this.hashTables[index].hashFunction(tmp));
		}
		
	}
	

}
