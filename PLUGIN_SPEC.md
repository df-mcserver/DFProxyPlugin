# Plugin message specification v0.0.1-INDEV

This plugin creates some custom commands via the plugin message system, similar to that of the BungeeCord plugin message spec.  
This makes it easier for backend servers to learn more about the clients connecting to them.  
The messages are sent via the `df:proxy` channel.

## Notable Implementations
- [DFLobbyServer](https://github.com/df-mcserver/DFLobbyServer/blob/master/src/main/java/uk/co/nikodem/Events/Plugins/MessageReceivers/)
- [DFSmpPlus](https://github.com/df-mcserver/DFSmpPlus/blob/master/src/main/java/uk/co/nikodem/dFSmpPlus/Messaging/Messages/)

## Connect
<details><summary>Click to expand</summary>

Similar to [BungeeCord's implementation](https://www.spigotmc.org/wiki/bukkit-bungee-plugin-messaging-channel/#connect), connects a player to the specified subserver, connected to the proxy.  
Returns a status update to allow servers to write custom logic for when a connect fails. Same name as the BungeeCord implementation, to make it easier to switch between the two.

Backend -> Proxy -> Backend

#### Arguments
1. Server name of the server to attempt to send the player too

#### Responses
1. Whether or not the connection was successful (String, "true"/"false")
</details>

## IncompatibleClient
<details><summary>Click to expand</summary>
Returns whether or not the client is considered 'incompatible'. Used for player validation.

Backend -> Proxy -> Backend

#### Arguments
None.

#### Responses
1. Whether or not the client is considered incompatible (String, "true"/"false")
</details>

## IsGeyser
<details><summary>Click to expand</summary>
Returns whether or not the client is playing via Geyser, on Bedrock. Used for player validation.
This is required to check if players are on Bedrock, as Geyser doesn't allow you to use the GeyserAPI on servers without the Geyser plugin being present, and Geyser cannot be present on both the proxy and backend server.

Backend -> Proxy -> Backend

#### Arguments
None.

#### Responses
1. Whether or not the client is connected via Geyser (String, "true"/"false")
</details>

## RealProtocolVersion
<details><summary>Click to expand</summary>
Returns the real protocol version that the player is playing with. Used for player validation

Backend -> Proxy -> Backend

#### Arguments
None.

#### Responses
1. The protocol version of the player (String)
</details>

## DiscordLoggingBridged
<details><summary>Click to expand</summary>
Returns whether or not the requesting backend server has an enabled bridged channel in this proxy plugin's discord bot.
If the proxy server receives this request, the automatic chat logging is disabled, assuming the backend will use DiscordLogPlayerMessage to forward player messages.
Will return "false" regardless if the discord bot is disabled.

Backend -> Proxy -> Backend

#### Arguments
None.

#### Responses
1. Whether or not proxy plugin's discord bot is enabled (String, "true"/"false")
</details>

## DiscordLogStandardMessage
<details><summary>Click to expand</summary>
Allows to forward a player (death) message to the proxy's discord bot. Make sure that the discord channel bridge is set up in the config, or else this request will be ignored. This request will be ignored if the DiscordLoggingBridged message was not previously sent.

Backend -> Proxy (-> Backend)

#### Arguments
1. The player message (String)

#### Responses
If the server sending this message is not registered, the exact message will be sent back to the backend server.
</details>

## DiscordLogEmbedMessage
<details><summary>Click to expand</summary>
Allows to forward a message within an embed to the proxy's discord bot. Make sure that the discord channel bridge is set up in the config, or else this request will be ignored. This request will be ignored if the DiscordLoggingBridged message was not previously sent.

Backend -> Proxy (-> Backend)

#### Arguments
1. The 6 char hex colour of the embed
2. The message

#### Responses
If the server sending this message is not registered, the exact message will be sent back to the backend server.
</details>

## DiscordLogPlayerEmbedMessage
<details><summary>Click to expand</summary>
Allows to forward a player message within an embed to the proxy's discord bot. Make sure that the discord channel bridge is set up in the config, or else this request will be ignored. This request will be ignored if the DiscordLoggingBridged message was not previously sent.

Backend -> Proxy (-> Backend)

#### Arguments
1. The 6 char hex colour of the embed
2. The message

#### Responses
If the server sending this message is not registered, the exact message will be sent back to the backend server.
</details>