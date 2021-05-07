import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class BTree {
    	private int t;
    	protected Node root;
    	
    	class Node{
    		private int numOfKeys;
    		public String[] keys;
    		private Node[] children;
    		private int numOfChildren;
    		private boolean leaf;
    		private Node parent;
    		
    		public Node(int t) {
    			this.numOfKeys=0;
    			this.keys=new String[(2*t)-1];
    			this.children=new Node[t*2];
    			this.numOfChildren=0;	
    		}
    		
    		public boolean getLeaf() {
    			return this.leaf;
    		}
    		
    		public void setLeaf(boolean flag) {
    			this.leaf=flag;
    		}
    		
    		public int getNumOfKeys() {
    			int count=0;
    			for(int i=0; i<(2*t)-1; i++)
    				if(this.keys[i]!=null)
    					count++;
    			this.numOfKeys=count;
    			return count;	
    		}
    		
    		public int getNumOfChildren() {
    			int count=0;
    			for(int i=0; i<this.children.length; i++)
    				if(this.children[i]!=null)
    					count++;
    			this.numOfChildren=count;
    			return count;
    		}
    		
    		public String search(Node x,String str) {  // Regular BTree search, using comparisons to make its way through the tree and find the wanted key
    	    	 int i=0;                              // if the key doesn't exist it returns null.
    	    	 int tmp=x.getNumOfKeys();
    	    	 while((i<tmp)&&(str.compareTo(x.keys[i])>0))
    	    		 i++;
    	    	 if((i<tmp)&&(str.compareTo(x.keys[i])==0))
    	    		 return x.keys[i];
    	    	 else {
    	    		 if(x.getLeaf()==true)
    	    			 return null;
    	    		 else
    	    			 return search(x.children[i],str);
    	    	 }
    	     }
    	}
      	private static boolean isNumeric(String str)  
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
    	
    public BTree(String tVal) {
    	 if(tVal==null)
    		 throw new RuntimeException();
    	 if(!this.isNumeric(tVal))
    		 throw new RuntimeException();
    	 this.t=Integer.parseInt(tVal);
    	 Node x=new Node(this.t);
 		x.setLeaf(true);
 		this.root=x;
     }
    public int getNodeHeight(Node x) {    // gets the height of the Node.
		int c=0;
		while(x.parent!=null) {
			x=x.parent;
			c++;
		}
		return c;
			
	}
    
    public int getHeight() {        // gets the height of the tree.
    	if(this.root.getLeaf())
    		return 1;
    	Node tmp=this.root;
    	int c=1;
    	while(tmp.getLeaf()==false) {
    		tmp=tmp.children[0];
    		c++;
    	}
    	return c;
    }
    
    public void createFullTree(String str){   // Adding an array of elements to the tree.
    	String[] arr=this.pathToString(str);
    	for(int i=0; i<arr.length; i++)
    		this.insert(this, arr[i]);
    }
    
	public String[] pathToString(String str) { // a modified version of the pathToString function in Messages's class.
		String output="";                      // like we stated before we used Stream because others ways couldn't decode certain characters like Scanner and '.'
		Path p1=Paths.get(str);                // on the forum we got an approval to use Stream but we used it just to receive the lines and turned it into an array.
		Stream<String> obj = null;
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
		return outputArr;
	}
	
	public void splitChild(Node x, int i) {   // Normal child splitting function, Note:we saw that restricting this function to 15 lines would make it 
		Node y=x.children[i];                 // very uncomfortable to understand, we explained every loop and what it does and we think that it's easier
		Node z=new Node(x.children.length/2); // to understand this function as a whole block.
		z.parent=x;
		z.setLeaf(y.getLeaf());
		int t=this.t;                      // Initializing variables and fields.
		for(int j=0; j<t-1; j++)
			z.keys[j]=y.keys[j+t];         // copying keys to the new Node.
		if(y.getLeaf()==false) {
			for(int j=0; j<t; j++) {          // if the Node isn't leaf then we copy the children too and change the parent field too.
				z.children[j]=y.children[j+t];
			    y.children[j+t].parent=z;
			}
			for(int j=2*t-1; j>=t; j--)     // manually "deleting" the children we copied before.
				y.children[j]=null;
		}
		int tmp=x.getNumOfKeys()+1;
		for(int j=tmp; j>i; j--)
			x.children[j]=x.children[j-1];   // making a place for Z to be a new child to X.
		x.children[i+1]=z;
		tmp=x.getNumOfKeys();
		for(int j=tmp; j>i; j--) 
			x.keys[j]=x.keys[j-1];        // making a place for the middle key of y so we move it to X.
		x.keys[i]=y.keys[t-1];
		for(int j=(2*t)-2; j>=t-1; j--)   // deleting the keys we copied from y.
			y.keys[j]=null;
	}
	
	public void insert(BTree tree, String str) {  // Normal insert like we learned in class.
		Node r=tree.root;
		if(r.getNumOfKeys()==(2*t)-1) {
			Node s=new Node(t);
		    tree.root=s;
		    s.setLeaf(false);
		    s.children[0]=r;
		    r.parent=s;
		    splitChild(s,0);
		    insertNonFull(s,str);
		}
		else 
			insertNonFull(r,str);
		}
	
	
	public void insertNonFull(Node x, String str) {  // regular InsertNonFull like we have seen in class.
		int i=x.getNumOfKeys();
		if(x.getLeaf()) {
			while((i>0)&&(str.compareTo(x.keys[i-1])<0)) {
				x.keys[i]=x.keys[i-1];
				i--;
			}
		x.keys[i]=str;	
		}
		else {
			while((i>0)&&(str.compareTo(x.keys[i-1])<0))
				i--;
		if(x.children[i].getNumOfKeys()==(2*t)-1) {
			splitChild(x,i);
			if(str.compareTo(x.keys[i])>0)
				i++;
		}
		insertNonFull(x.children[i],str);
		}
	}
	
	public String initLvlScan() {   // prepares the needed variables and Queues to do the BFS scan, we will be using two queues and the fields Node.parent,
		Node x=this.root;           // also the getNodeHeight function to compare each node so we can print the correct characters.
		int t=this.t;
		nodeQueue nodeTor=new nodeQueue(this.getHeight()*2*t);
		stringQueue stringTor=new stringQueue(this.getHeight()*2*t);
		nodeTor.insert(x);
		for(int i=0; i<x.getNumOfKeys(); i++)
			stringTor.insert(x.keys[i]);
		return this.levelScan(t, x, nodeTor, stringTor);
	}
	
	public String levelScan(int t,Node x, nodeQueue nodeTor, stringQueue stringTor) {
		//Inserting every node to the nodeQueue and it's keys to the stringQueue and adding them to the output string after that we add the Node's children 
		// and we dequeue him out of the Queue and we keep going till we reach empty queues meaning we passed by every node in the tree.
		// special characters as wanted in the assignment were added according to comparisons of the parent field and the height.
		String output="";      
	while(!nodeTor.isEmpty()) {
		while(!stringTor.isEmpty()) {
			output=output+stringTor.remove();
			if(stringTor.len!=0)
			output=output+",";
		}
		Node cmp=nodeTor.peek();
		for(int i=0; i<cmp.getNumOfChildren(); i++)
			nodeTor.insert(cmp.children[i]);
		nodeTor.remove();
		if(!nodeTor.isEmpty()) {
		output=this.bfsHelper(nodeTor, output, cmp);
		cmp=nodeTor.peek();
		for(int i=0; i<cmp.getNumOfKeys(); i++)
			stringTor.insert(cmp.keys[i]);
		}
	}
	return output;
	}
	
	public String bfsHelper(nodeQueue nodeTor,String output,Node cmp) {  // The comparison function to add the special characters.
		if(cmp.parent==nodeTor.peek().parent)
			output=output+"|";
		else {
		if(this.getNodeHeight(cmp)!=this.getNodeHeight(nodeTor.peek()))
			output=output+"#";
		}
		if((this.getNodeHeight(cmp)==this.getNodeHeight(nodeTor.peek()))&&(cmp.parent!=nodeTor.peek().parent))
			output=output+"^";
		return output;
	}
	
	public String toString() {
		return this.initLvlScan();
	}
	
	private class nodeQueue
	{
	    protected Node[] Queue;
	    protected int front, rear, size, len;
	 
	    /* Constructor */
	    public nodeQueue(int n) 
	    {
	        size = n;
	        len = 0;
	        Queue = new Node[size];
	        front = -1;
	        rear = -1;
	    }    
	    /*  Function to check if queue is empty */
	    public boolean isEmpty() 
	    {
	        return front == -1;
	    }     
 
	    /*  Function to check the front element of the queue */
	    public Node peek() 
	    {
	        if (isEmpty())
	           throw new RuntimeException("Underflow Exception");
	        return Queue[front];
	    }    
	    /*  Function to insert an element to the queue */
	    public void insert(Node i) 
	    {
	        if (rear == -1) 
	        {
	            front = 0;
	            rear = 0;
	            Queue[rear] = i;
	        }
	        else if (rear + 1 >= size)
	            throw new IndexOutOfBoundsException("Overflow Exception");
	        else if ( rear + 1 < size)
	            Queue[++rear] = i;    
	        len++ ;    
	    }    
	    /*  Function to remove front element from the queue */
	    public Node remove() 
	    {
	        if (isEmpty())
	           throw new RuntimeException("Underflow Exception");
	        else 
	        {
	            len-- ;
	            Node ele = Queue[front];
	            if ( front == rear) 
	            {
	                front = -1;
	                rear = -1;
	            }
	            else
	                front++;                
	            return ele;
	        }        
	    }
	}
	
	private class stringQueue
	{
	    protected  String Queue[] ;
	    protected int front, rear, size, len;
	 
	    /* Constructor */
	    public stringQueue(int n) 
	    {
	        size = n;
	        len = 0;
	        Queue = new String[size];
	        front = -1;
	        rear = -1;
	    }    
	    /*  Function to check if queue is empty */
	    public boolean isEmpty() 
	    {
	        return front == -1;
	    }    
     
	    /*  Function to check the front element of the queue */
	    public String peek() 
	    {
	        if (isEmpty())
	           throw new RuntimeException("Underflow Exception");
	        return Queue[front];
	    }    
	    /*  Function to insert an element to the queue */
	    public void insert(String i) 
	    {
	        if (rear == -1) 
	        {
	            front = 0;
	            rear = 0;
	            Queue[rear] = i;
	        }
	        else if (rear + 1 >= size)
	            throw new IndexOutOfBoundsException("Overflow Exception");
	        else if ( rear + 1 < size)
	            Queue[++rear] = i;    
	        len++ ;    
	    }    
	    /*  Function to remove front element from the queue */
	    public String remove() 
	    {
	        if (isEmpty())
	           throw new RuntimeException("Underflow Exception");
	        else 
	        {
	            len-- ;
	            String ele = Queue[front];
	            if ( front == rear) 
	            {
	                front = -1;
	                rear = -1;
	            }
	            else
	                front++;                
	            return ele;
	        }        
	    }
	}
}


