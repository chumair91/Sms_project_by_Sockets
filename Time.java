import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Time {
    public String returnTime(LocalDateTime time){
        DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return time.format(format);
    }
}
