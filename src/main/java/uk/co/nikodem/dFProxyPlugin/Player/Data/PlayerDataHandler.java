package uk.co.nikodem.dFProxyPlugin.Player.Data;

import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import java.io.*;
import java.nio.file.Path;

public class PlayerDataHandler {
    public static String fileName = "players.toml";

    public static File getPlayersFile() {
        return Path.of(DFProxyPlugin.dataDirectory.toUri().getPath(), "/"+fileName).toFile();
    }

    public static void initialisePlayersFile() {
        File file = getPlayersFile();

        try {
             file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTestValue() {
        if (getPlayersFile().exists()) {

        } else {
            System.out.println("File does not exist!");
        }
    }
}
