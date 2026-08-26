package web;

import model.DataModel;

import java.util.*;
import java.util.function.Function;

public class ViewRenderer {
    private static final String open = "{{ ";
    private static final String close = " }}";
    private String data;

    private ViewRenderer(String data) {
        this.data = data;
    }

    public static ViewRenderer fromString(String data) {
        return new ViewRenderer(data);
    }

    public static ViewRenderer fromResource(String resource) {
        return new ViewRenderer(ViewResource.loadHtml(resource).getData());
    }

    public ViewRenderer renderModel(DataModel model) {
        return renderMap(model.toPlainTextData(), true);
    }

    public ViewRenderer renderResource(ViewResource resource) {
        return renderMap(Map.of(resource.getName(), resource.getData()), false);
    }

    public ViewRenderer renderResource(String resource) {
        return renderResource(ViewResource.loadHtml(resource));
    }

    public ViewRenderer renderIfResource(String field, boolean flag, ViewResource trueResource, ViewResource falseResource) {
        if (flag) {
            if (trueResource == null) {
                return renderMap(Map.of(field, ""), false);
            } else {
                return renderMap(Map.of(field, trueResource.getData()), false);
            }
        } else {
            if (falseResource == null) {
                return renderMap(Map.of(field, ""), false);
            } else {
                return renderMap(Map.of(field, falseResource.getData()), false);
            }
        }
    }

    public ViewRenderer renderIfResource(String field, boolean flag, String trueResource, String falseResource) {
        return renderIfResource(field, flag, ViewResource.loadHtml(trueResource), ViewResource.loadHtml(falseResource));
    }

    public ViewRenderer renderIfString(String field, boolean flag, String trueValue, String falseValue) {
        if (flag) {
            return renderMap(Map.of(field, Objects.requireNonNullElse(trueValue, "")), true);
        } else {
            return renderMap(Map.of(field, Objects.requireNonNullElse(falseValue, "")), true);
        }
    }

    public ViewRenderer renderIfBase(boolean flag, ViewResource trueResource, ViewResource falseResource) {
        if (flag) {
            return renderBase(trueResource);
        } else {
            return renderBase(falseResource);
        }
    }

    public ViewRenderer renderIfBase(boolean flag, String trueResource, String falseResource) {
        return renderIfBase(flag, ViewResource.loadHtml(trueResource), ViewResource.loadHtml(falseResource));
    }

    public ViewRenderer renderBase(ViewResource resource) {
        return renderMap(Map.of("#", resource.getData()), false);
    }

    public ViewRenderer renderBase(String resource) {
        return renderBase(ViewResource.loadHtml(resource));
    }

    public ViewRenderer renderString(String field, String value, boolean encode) {
        return renderMap(Map.of(field, value), encode);
    }

    public ViewRenderer renderString(String field, String value) {
        return renderMap(Map.of(field, value), true);
    }

    public ViewRenderer renderInteger(String field, Integer value) {
        return renderMap(Map.of(field, Integer.toString(value)), true);
    }

    public ViewRenderer renderBoolean(String field, Boolean value) {
        return renderMap(Map.of(field, value?"Да":"Нет"), true);
    }

    public ViewRenderer renderMap(Map<String, String> model, boolean encode) {
        int openIndex = 0;
        if (data == null) return null;
        for (int index = open.length(); index < data.length(); index++){
            if (data.startsWith(open, index-open.length())) {
                openIndex = index-open.length();
            }
            if (data.startsWith(close, index-close.length())){
                String field = data.substring(openIndex+open.length(), index-close.length());
                if (model.containsKey(field)) {
                    String modelFieldData = model.get(field);
                    if (encode) {
                        modelFieldData = modelFieldData.replace("\"", "&quot;");
                        modelFieldData = modelFieldData.replace("<", "&lt;");
                        modelFieldData = modelFieldData.replace(">", "&gt;");
                        modelFieldData = modelFieldData.replace("\n", "<br>");
                    }
                    data = data.substring(0, openIndex) + modelFieldData + data.substring(index);
                    index = open.length();
                }
            }
        }
        return this;
    }

    public String get() {
        return data;
    }

    public ViewRenderer renderNav(boolean authenticated) {
        return renderIfResource(
                "nav",
                authenticated,
                "nav/nav-authenticated",
                "nav/nav-unauthenticated"
        );
    }

    public ViewRenderer renderTable(String name, List<String> columns, ArrayList<ArrayList<String>> rows) {
        StringBuilder head = new StringBuilder("<tr>");
        for (String column: columns) {
            head.append("<th>%s</th>".formatted(column));
        }
        head.append("</tr>");

        StringBuilder body = new StringBuilder();
        for (List<String> row: rows) {
            body.append("<tr>");
            for (String field: row) {
                body.append("<th>%s</th>".formatted(field));
            }
            body.append("</tr>");
        }
        renderString("name", name, false);
        renderString("head", head.toString(), false);
        renderString("body", body.toString(), false);
        return this;
    }

    public ViewRenderer renderTable(String name, String[] columns, ArrayList<? extends DataModel> dataList) {
        ArrayList<ArrayList<String>> rows = new ArrayList<>();
        for (DataModel data: dataList) {
            Map<String, String> plainData = data.toPlainTextData();
            ArrayList<String> row = new ArrayList<>();
            for (String column: columns) {
                row.add(plainData.getOrDefault(column, ""));
            }
            rows.add(row);
        }
        return renderTable(name, Arrays.asList(columns), rows);
    }

    public ViewRenderer renderResourceModelList(String field, Function<DataModel, String> modelRenderer, ArrayList<? extends DataModel> models) {
        StringBuilder stringBuilder = new StringBuilder();
        models.forEach((DataModel model) -> stringBuilder.append(modelRenderer.apply(model)));
        return renderString(field, stringBuilder.toString(), false);
    }
}
