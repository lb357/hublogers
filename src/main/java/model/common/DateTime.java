package model.common;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class DateTime extends Timestamp {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm/dd.MM.yyyy");

    public DateTime(long time) {super(time);}
    public DateTime(Timestamp timestamp) {super(timestamp.getTime());}

    @Override
    public String toString(){
        return this.toLocalDateTime().format(dateTimeFormatter);
    }
}
