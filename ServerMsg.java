import java.time.*;
import java.io.*;
import java.util.*;
public class ServerMsg implements Comparable <ServerMsg>{
    LocalDateTime date;
    String msg;
    Time t=new Time();
    public  ServerMsg( String msg,LocalDateTime date) {
       
        this.msg = msg;
        this.date = date;
    }
    
   public String formattedTime(){
    return t.returnTime(date);
    
   }
   
   
   @Override
   public int compareTo(ServerMsg o) {
       return this.date.compareTo(o.date);
       
   }
    
    
    @Override
    public String toString() {
       
        return " msg=" + msg +" time=" + formattedTime() ;
     
    }

  

    
    

}

