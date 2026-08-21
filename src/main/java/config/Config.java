package config;

import java.util.Map;

public abstract class Config {
    private static final Map<String, String> env = System.getenv();

    protected String get(String envArg, String defaultValue){
        return env.getOrDefault(envArg, defaultValue);
    }

    public abstract void load();
}
