package uk.co.nikodem.dFProxyPlugin.Config;

import java.util.ArrayList;
import java.util.List;

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

    public static class DiscordBot {
        public static class BridgedChannel {
            private String channel_id = "";
            private String mc_server_name = "";

            public String getChannelId() {
                return channel_id;
            }

            public String getRegisteredServerName() {
                return mc_server_name;
            }
        }

        private Boolean enabled = false;
        private String token = "";
        private String guild_id = "";
        private List<BridgedChannel> channels = new ArrayList<>();

        public Boolean isEnabled() {
            return enabled;
        }
        public String getToken() {
            return token;
        }
        public String getGuildId() {
            return guild_id;
        }
        public List<BridgedChannel> getBridgedChannels() {
            return channels;
        }
    }

    public ResourcePackHosting resource_pack_hosting = new ResourcePackHosting();
    public DiscordBot discord_bot = new DiscordBot();
    public Login login = new Login();
}
