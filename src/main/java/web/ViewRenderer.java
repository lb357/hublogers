package web;

import config.AdminConfig;
import model.DataModel;
import util.ResourceReader;

import java.util.Map;

public class ViewRenderer {
    private static final String open = "{{ ";
    private static final String close = " }}";

    public static String renderModel(String templateHTML, DataModel model) {
        return renderModel(templateHTML, model.toPlainTextData());
    }

    public static String renderModel(String templateHTML, String field, String value) {
        return renderModel(templateHTML, Map.of(field, value));
    }

    public static String renderModel(String templateHTML, String field, Integer value) {
        return renderModel(templateHTML, Map.of(field, Integer.toString(value)));
    }

    public static String renderModel(String templateHTML, String field, Boolean value) {
        return renderModel(templateHTML, Map.of(field, value?"Да":"Нет"));
    }

    public static String renderModel(String templateHTML, Map<String, String> model) {
        int openIndex = 0;
        if (templateHTML == null) return null;
        for (int index = open.length(); index < templateHTML.length(); index++){
            if (templateHTML.startsWith(open, index-open.length())) {
                openIndex = index-open.length();
            }
            if (templateHTML.startsWith(close, index-close.length())){
                String field = templateHTML.substring(openIndex+open.length(), index-close.length());
                if (model.containsKey(field)) templateHTML = templateHTML.substring(0, openIndex) + model.get(field) + templateHTML.substring(index);
                index = open.length();
            }
        }
        return templateHTML;
    }

    public static String loadViewResource(String resource) {
        return ResourceReader.readResource("view/"+resource);
    }
}
