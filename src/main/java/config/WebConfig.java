package config;

import util.AdminOutput;

public class WebConfig extends Config {
    private boolean enabled;
    private int port;
    private String hostName;

    public void load() {
        this.enabled = get("webserver-enabled", "TRUE").toLowerCase().trim().equals("true");
        try {
            this.port = Integer.parseInt(get("webserver-port", "80"));
        } catch (NumberFormatException e) {
            AdminOutput.error(e);
            this.port = 80;
        }

        this.hostName = get("webserver-hostname", "localhost");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getPort() {
        return port;
    }

    public String getHostName() {
        return hostName;
    }
}
