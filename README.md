# DFProxyPlugin
A hacky velocity plugin for barebones proxy wide server management and custom plugin messages

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
- ViaVersion (Velocity) 5.5.2-SNAPSHOT+
    - Getting accurate protocol versions (to get around the fact that velocity will only report 1.13 or higher w/ modern forwarding)