import java.util.Iterator;

public class messageIterator implements Iterator<Message> {

	private int counter;
	private Message[] arr;
	private int arrLength;
	
	public messageIterator(Message[] arr1) {
		this.arr=arr1;
		this.counter=0;
		this.arrLength=arr1.length-1;
	}
	@Override
	public boolean hasNext() {
		return this.counter<this.arrLength;
	}

	@Override
	public Message next() {
		Message output=this.arr[counter];
		this.counter++;
		return output;
	}

}
