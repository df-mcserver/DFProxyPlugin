package uk.co.nikodem.dFProxyPlugin.Discord.Utils;

import java.awt.*;

public class HexColourHelper {
    public static Color hexToColor(String hex) {
        // Remove the '#' if present
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        // Parse the hex values for red, green, and blue
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        // Create a Color object
        return new Color(r, g, b);
    }
}
