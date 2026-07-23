package model.common;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;

public class DataModel {
    public String getJSON() {
        try {
            StringBuilder json = new StringBuilder("{\n");
            Field[] fields = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Class<?> type = field.getType();
                field.setAccessible(true);
                json.append("    \"").append(field.getName()).append("\": ");

                if (field.get(this) == null) {
                    json.append("null");
                } else if (type == String.class) {
                    json.append("\"").append((String) field.get(this)).append("\"");
                } else if (type == int.class ) {
                    json.append(String.valueOf(field.getInt(this)));
                } else if (type == Integer.class) {
                    json.append(String.valueOf((Integer) field.get(this)));
                } else if (type == boolean.class) {
                    json.append(String.valueOf(field.getBoolean(this)));
                } else if (type == Boolean.class) {
                    json.append(String.valueOf((Boolean) field.get(this)));
                } else if (type == Timestamp.class) {
                    json.append(String.valueOf(((Timestamp) field.get(this)).getTime()));
                }

                if (i != fields.length-1) {
                    json.append(",\n");
                } else {
                    json.append("\n");
                }
                field.setAccessible(false);
            }
            json.append("}");
            return json.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getJSONArray(ArrayList<? extends DataModel> data){
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i<data.size(); i++){
            json.append(data.get(i).getJSON());
            if (i != data.size()) {
                json.append(",\n");
            } else {
                json.append("\n");
            }
        }
        json.append("]");
        return json.toString();
    }
}
