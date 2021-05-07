import java.util.Iterator;
import java.util.StringTokenizer;

public class Spams implements Iterable<Spam>{

	private Spam[] arr;
	
	public Spams() {}
	
	public void generateSpams(String str) {
		StringTokenizer stock=new StringTokenizer(str,"\n");   // Using StringTokenizer to split the string into tokens
		this.arr=new Spam[stock.countTokens()];
		for(int i=0; i<=stock.countTokens(); i++) {
			StringTokenizer stock2=new StringTokenizer(stock.nextToken()," ");   // using another one to split every word from it's threshold.
			String tmp=stock2.nextToken();
			int t=Integer.parseInt(stock2.nextToken());    
			Spam newSpam=new Spam(tmp,t);           // Creating a new object for every word and inserting it into the array of spams.
			this.arr[i]=newSpam;
		}
	}
	
	public Spam getSpam(int index) {
		return this.arr[index];
	}
	
	public int getNumberOfSpams() {
		return this.arr.length;
	}
	
	@Override
	public Iterator iterator() {
		spamIterator iter=new spamIterator(this.arr);
		return iter;
	}

}
