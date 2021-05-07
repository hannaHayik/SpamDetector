
public class HashListElement {   //HashListElement as asked, "Link" class for the HashList class.

	private String data;
	private HashListElement next;
	
	public HashListElement(String data) {   // normal constructor.
		this.data=data;
		this.next=null;
	}
	// Gets and Sets which we will use.
	public HashListElement getNext() {
		return this.next;
	}
	public void setNext(HashListElement node) {  
		this.next=node;
	}
	
	public String getData() {
		return this.data;
	}
	
	public void setData(String data) {
		this.data=data;
	}
}
