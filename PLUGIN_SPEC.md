# Plugin message specification v0.0.0-INDEV

This plugin creates some custom commands via the plugin message system, similar to that of the BungeeCord plugin message spec.  
This makes it easier for backend servers to learn more about the clients connecting to them.  
The messages are sent via the `df:proxy` channel.

## Notable Implementations
- [DFLobbyServer](https://github.com/df-mcserver/DFLobbyServer/blob/master/src/main/java/uk/co/nikodem/Events/Plugins/MessageReceivers/)
- [DFSmpPlus](https://github.com/df-mcserver/DFSmpPlus/blob/master/src/main/java/uk/co/nikodem/dFSmpPlus/Messaging/Messages/)

## Connect
<details><summary>Click to expand</summary>

Similar to [BungeeCord's implementation](https://www.spigotmc.org/wiki/bukkit-bungee-plugin-messaging-channel/#connect), connects a player to the specified subserver, connected to the proxy.  
Returns a status update to allow servers to write custom logic

#### Arguments
1. Server name of the server to attempt to send the player too

#### Responses
Implementations should listen for the `Connect` message to get the response.
1. Whether or not the connection was successful (String, "true"/"false")
</details>

## IncompatibleClient
<details><summary>Click to expand</summary>
Returns whether or not the client is considered 'incompatible'. Used for player validation.

#### Arguments
None.

#### Responses
1. Whether or not the client is considered incompatible (String, "true"/"false")
</details>

## IsGeyser
<details><summary>Click to expand</summary>
Returns whether or not the client is playing via Geyser, on Bedrock. Used for player validation.
This is required to check if players are on Bedrock, as Geyser doesn't allow you to use the GeyserAPI on servers without the Geyser plugin being present, and Geyser cannot be present on both the proxy and backend server.

#### Arguments
None.

#### Responses
1. Whether or not the client is connected via Geyser (String, "true"/"false")
</details>

## RealProtocolVersion
<details><summary>Click to expand</summary>
Returns the real protocol version that the player is playing with. Used for player validation

#### Arguments
None.

#### Responses
1. The protocol version of the player (String)
</details>
