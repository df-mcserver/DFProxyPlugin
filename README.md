# DFProxyPlugin
A hacky velocity plugin for barebones proxy wide server management and custom plugin messages  

This plugin also contains a basic resource pack hoster, for hosting one resource pack, and a discord bot, ran by the plugin, both of which are disabled by default and are completely optional.

> [!WARNING]
> NOT INTENDED FOR PERSONAL USE  
> This plugin is built for a very specific use case, and you may need to modify the source code in order to make this plugin work for you.  
> Despite that, many of the other projects in this organisation are designed to have this plugin available behind a proxy.

This plugin creates some custom commands via the plugin message system, similar to that of the BungeeCord plugin message spec.  
Read the specification [here](./PLUGIN_SPEC.md)

## Dependencies
- Geyser (Velocity) 2.9.0-SNAPSHOT+
    - detecting whether or not players that join are Bedrock or not, and making Bedrock-exclusive QoL Features
    - must be present in the plugins
- ViaVersion (Velocity) 5.6.1+
    - Getting accurate protocol versions (to get around the fact that velocity will only report 1.13 or higher w/ modern forwarding)