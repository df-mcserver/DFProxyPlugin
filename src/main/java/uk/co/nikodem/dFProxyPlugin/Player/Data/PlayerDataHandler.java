package uk.co.nikodem.dFProxyPlugin.Player.Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.velocitypowered.api.proxy.Player;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformation;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.ParsedPlatformInformationAdapter;
import uk.co.nikodem.dFProxyPlugin.Player.Platform.Versions.BedrockPlatformInformation;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;

public class PlayerDataHandler {
    public static String folderName = "players";
    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(ParsedPlatformInformation.class, new ParsedPlatformInformationAdapter())
            .create();

    public static PlayerData onJoin(Player plr, ParsedPlatformInformation info) {
        PlayerData data = retrievePlayerData(plr);

        updatePlayerData(data, info);
        writePlayerDataToPlayerFile(plr, data);

        return data;
    }

    public static PlayerData retrievePlayerData(UUID uuid) {
        PlayerData data = gson.fromJson(readPlayerFileAsString(uuid), PlayerData.class);
        if (data == null) data = new PlayerData();
        return data;
    }

    public static void updatePlayerData(PlayerData data, ParsedPlatformInformation info) {
        if (info instanceof BedrockPlatformInformation) {
            data.lastWasOnBedrock = true;
            data.hasPlayedOnBedrock = true;
            data.bedrockJoinCount += 1;
        }

        data.lastPlatform = info;
        data.joinCount += 1;
        data.lastJoinDate = new Date().getTime();
    }

    public static PlayerData retrievePlayerData(Player plr) {
        return retrievePlayerData(plr.getUniqueId());
    }

    @Nullable
    public static String readPlayerFileAsString(UUID uuid) {
        File playerFile = getPlayerFile(uuid);
        if (!playerFile.exists()) {
            try {
                createPlayerFile(uuid);
            } catch (IOException ignored) {

            }
        }

        if (!playerFile.canRead()) {
            System.out.println("Cannot read file "+playerFile.getAbsolutePath()+"!");
            return null;
        }

        try (FileReader fileReader = new FileReader(playerFile)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                return bufferedReader.readLine(); // data should only be on the first line
            } catch (IOException e) {
                System.out.println("Failed to parse file "+playerFile.getAbsolutePath()+"!");
            }
        } catch (IOException e) {
            System.out.println("Failed to read file "+playerFile.getAbsolutePath()+"!");
        }

        return null;
    }

    @Nullable
    public static String readPlayerFileAsString(Player plr) {
        return readPlayerFileAsString(plr.getUniqueId());
    }

    public static void writePlayerDataToPlayerFile(UUID uuid, PlayerData playerData) {
        writeStringToPlayerFile(uuid, gson.toJson(playerData));
    }

    public static void writePlayerDataToPlayerFile(Player plr, PlayerData playerData) {
        writePlayerDataToPlayerFile(plr.getUniqueId(), playerData);
    }

    public static void writeStringToPlayerFile(UUID uuid, String content) {
        File playerFile = getPlayerFile(uuid);
        if (!playerFile.exists()) {
            try {
                createPlayerFile(uuid);
            } catch (IOException ignored) {

            }
        }

        if (!playerFile.canRead()) {
            System.out.println("Cannot read file "+playerFile.getAbsolutePath()+"!");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(playerFile)) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(content);
                bufferedWriter.flush();
            } catch (IOException e) {
                System.out.println("Failed to write to file "+playerFile.getAbsolutePath()+"!");
            }
        } catch (IOException e) {
            System.out.println("Failed to read file "+playerFile.getAbsolutePath()+"!");
        }
    }

    public static void writeStringToPlayerFile(Player plr, String content) {
        writeStringToPlayerFile(plr.getUniqueId(), content);
    }


    public static boolean createPlayerFile(UUID uuid) throws IOException {
        File file = getPlayerFile(uuid);
        createPlayersDirectory();
        return file.createNewFile();
    }

    public static boolean createPlayerFile(Player plr) throws IOException {
        return createPlayerFile(plr.getUniqueId());
    }

    public static File getPlayerFile(UUID uuid) {
        return Path.of(getPlayersDirectory().toURI().getPath(), "/"+uuid.toString()+".json").toFile();
    }

    public static File getPlayerFile(Player plr) {
        return getPlayerFile(plr.getUniqueId());
    }

    public static File getPlayersDirectory() {
        return Path.of(DFProxyPlugin.dataDirectory.toUri().getPath(), "/"+folderName).toFile();
    }

    public static boolean createPlayersDirectory() {
        return getPlayersDirectory().mkdirs();
    }
}
