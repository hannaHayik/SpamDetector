public class HashList {                 // Class that implements a list using arrays.
       protected HashListElement[] array;    // array of "Hash List Elements" which equals Nodes/Links.
       protected HashListElement data;
       public HashList(int size){
              array = new HashListElement[size];
       }
   
       public void Add(HashListElement data){      // if we find a null place we add the item and break, else we add another place using UpdateSize function.
              for(int i=0;i<array.length;i++){
                     if(array[i] == null){
                           array[i] = data;
                           break;
                     }
              }
              array=UpdateSize(array,array.length);
              array[array.length-1]=data;         // and adding the item we wanted to add in the first place.
       }

       public HashListElement[] UpdateSize(HashListElement[] rArr,int length){   // copying the array into a bigger one.
    	   HashListElement[] arr=new HashListElement[rArr.length+1];
    	   for(int i=0; i<rArr.length; i++)
    		   arr[i]=rArr[i];
    	   return arr;
       }
       public void display(){
              for(int i=0;i<array.length;i++){
                     if(array[i] == null)break;
                     System.out.println(array[i]);
              }
       }
}
