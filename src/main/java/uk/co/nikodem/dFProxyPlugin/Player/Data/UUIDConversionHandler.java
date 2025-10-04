package uk.co.nikodem.dFProxyPlugin.Player.Data;

import com.google.gson.Gson;
import com.velocitypowered.api.proxy.Player;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

public class UUIDConversionHandler {
    public static UUIDConversion conversion = retrieveConversion();

    public static UUIDConversion retrieveConversion() {
        Gson gson = new Gson();
        UUIDConversion data = gson.fromJson(readUUIDFileAsString(), UUIDConversion.class);
        if (data == null) data = new UUIDConversion();
        return data;
    }

    public static void addConversion(String username, String uuid) {
        if (!conversion.map.containsKey(username)) conversion.map.put(username, uuid);
        writeConversionsToUUIDFile();
    }

    public static void addConversion(Player plr) {
        addConversion(plr.getUsername(), plr.getUniqueId().toString());
    }

    public static void addConversion(String username, UUID uuid) {
        addConversion(username, uuid.toString());
    }

    @Nullable
    public static String convertUsernameToUUID(String username) {
        return conversion.map.get(username);
    }

    @Nullable
    public static String readUUIDFileAsString() {
        File uuidFile = getUUIDFile();
        if (!uuidFile.exists()) {
            try {
                createUUIDFile();
            } catch (IOException ignored) {

            }
        }

        if (!uuidFile.canRead()) {
            System.out.println("Cannot read file "+uuidFile.getAbsolutePath()+"!");
            return null;
        }

        try (FileReader fileReader = new FileReader(uuidFile)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                return bufferedReader.readLine(); // data should only be on the first line
            } catch (IOException e) {
                System.out.println("Failed to parse file "+uuidFile.getAbsolutePath()+"!");
            }
        } catch (IOException e) {
            System.out.println("Failed to read file "+uuidFile.getAbsolutePath()+"!");
        }

        return null;
    }

    public static void writeConversionsToUUIDFile() {
        writeStringToUUIDFile(new Gson().toJson(conversion));
    }

    public static void writeStringToUUIDFile(String content) {
        File uuidFile = getUUIDFile();
        if (!uuidFile.exists()) {
            try {
                createUUIDFile();
            } catch (IOException ignored) {

            }
        }

        if (!uuidFile.canRead()) {
            System.out.println("Cannot read file "+uuidFile.getAbsolutePath()+"!");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(uuidFile)) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(content);
                bufferedWriter.flush();
            } catch (IOException e) {
                System.out.println("Failed to write to file "+uuidFile.getAbsolutePath()+"!");
            }
        } catch (IOException e) {
            System.out.println("Failed to read file "+uuidFile.getAbsolutePath()+"!");
        }
    }

    public static boolean createUUIDFile() throws IOException {
        File file = getUUIDFile();
        return file.createNewFile();
    }

    public static File getUUIDFile() {
        return Path.of(DFProxyPlugin.dataDirectory.toUri().getPath(), "/uuids.json").toFile();
    }
}
