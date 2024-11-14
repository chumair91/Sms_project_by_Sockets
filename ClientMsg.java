import java.time.*;
import java.io.*;
public class ClientMsg implements Comparable <ClientMsg>{
    LocalDateTime date;
    String msg;
    Time t=new Time();
    public  ClientMsg( String msg,LocalDateTime date) {
       
        this.msg = msg;
        this.date = date;
    }
    
   public String formattedTime(){
    return t.returnTime(date);
    
   }
   


    public int compareTo(ClientMsg o){
       return this.date.compareTo(o.date);
    }
    
    @Override
    public String toString() {
        return " msg=" + msg +" time=" + formattedTime() ;
    }
    

}
