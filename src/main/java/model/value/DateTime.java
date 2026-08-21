package model.value;

import model.DataModel;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DateTime extends Timestamp implements DataModel {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm/dd.MM.yyyy");

    public DateTime(long time) {super(time);}
    public DateTime(Timestamp timestamp) {super(timestamp.getTime());}

    @Override
    public String toString(){
        return this.toLocalDateTime().format(dateTimeFormatter);
    }


    @Override
    public Map<String, String> toPlainTextData() {
        return Map.of(
                "datetime", toString()
        );
    }
}
