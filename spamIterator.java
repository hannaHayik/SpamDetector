import java.util.Iterator;

public class spamIterator implements Iterator<Spam> {

	private int counter;
	private Spam[] arr;
	private int length;
	
	public spamIterator(Spam[] arr) {
		this.counter=0;
		this.arr=arr;
		this.length=arr.length;
	}

	@Override
	public boolean hasNext() {
		return this.counter<this.length-1;
	}

	@Override
	public Spam next() {
		Spam output=this.arr[counter];
		counter++;
		return output;
	}
}
