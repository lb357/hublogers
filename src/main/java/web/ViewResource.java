package web;

import util.ResourceReader;

public class ViewResource {
    private final String name;
    private final String data;
    private final String type;

    public ViewResource(String name, String data, String type) {
        this.name = name;
        this.data = data;
        this.type = type;
    }

    public static ViewResource loadCss(String resource) {
        return new ViewResource(resource, ResourceReader.readResource("view/"+resource+".css"), "css");
    }

    public static ViewResource loadHtml(String resource) {
        return new ViewResource(resource, ResourceReader.readResource("view/"+resource+".html"), "html");
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getType() {
        return type;
    }
}
