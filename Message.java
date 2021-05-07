
public class Message {
	private String sender;
	private String receiver;
	private String subject;
	
	public Message(String sender, String receiver, String subject) {
		this.sender=sender;
		this.receiver=receiver;
		this.subject=subject;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public String getFrom() {
		return this.sender;
	}
	
	public String getTo() {
		return this.receiver;
	}
}
