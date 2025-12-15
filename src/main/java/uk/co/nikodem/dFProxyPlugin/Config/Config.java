package uk.co.nikodem.dFProxyPlugin.Config;

public class Config {
    public static class ResourcePackHosting {
        private Boolean enabled = false;
        private String address = "127.0.0.1";
        private Integer port = 7270;
        private String path = "packs/pack.zip";

        public Boolean isEnabled() {
            return enabled;
        }
        public String getAddress() {
            return address;
        }
        public String getPath() {
            return path;
        }
        public Integer getPort() {
            return port;
        }
    }

    public static class Login {
        private Boolean login_message = true;

        public Boolean isLoginMessageEnabled() {
            return login_message;
        }
    }

    public ResourcePackHosting resource_pack_hosting = new ResourcePackHosting();
    public Login login = new Login();
}
