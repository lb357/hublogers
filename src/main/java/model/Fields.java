package model;

import java.util.HashMap;

public class Fields {
    private String[] data;
    private final HashMap<Integer, String> fieldMap = new HashMap<>();
    private final HashMap<String, Integer> indexMap = new HashMap<>();

    public Fields(String... data) {
        this.data = data;
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                if (!indexMap.containsKey(data[i])) {
                    fieldMap.put(i, data[i]);
                    indexMap.put(data[i], i);
                } else {
                    throw new IllegalArgumentException(
                            "Поле (%s на %d) таблицы должно быть уникальным".formatted(data[i], i)
                    );
                }
            } else {
                throw new IllegalArgumentException("Поле (на %d) таблицы должно быть не null".formatted(i));
            }
        }
    }

    public String[] getData() {
        return data;
    }

    public int getIndex(String field) {
        if (indexMap.containsKey(field)) {
            return indexMap.get(field);
        } else {
            throw new IllegalArgumentException("Таблица не содержит поле: %s".formatted(field));
        }
    }

    public String getIndexMap(int index) {
        if (fieldMap.containsKey(index)) {
            return fieldMap.get(index);
        } else {
            throw new IllegalArgumentException("Таблица не содержит индекс: %d".formatted(index));
        }
    }
}
