package uk.co.nikodem.dFProxyPlugin.ResourcePack;

import team.unnamed.creative.BuiltResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.server.ResourcePackServer;
import team.unnamed.creative.server.handler.ResourcePackRequestHandler;
import uk.co.nikodem.dFProxyPlugin.DFProxyPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.concurrent.Executors;

public class PackHoster {
    private ResourcePackServer server;

    public PackHoster() {
        try {
            File packFile = Path.of(DFProxyPlugin.dataDirectory.toString(), DFProxyPlugin.config.resource_pack_hosting.getPath()).toFile();

            ResourcePackRequestHandler handler = (request, exchange) -> {
                BuiltResourcePack pack = BuiltResourcePack.of(
                        Writable.file(packFile),
                        getSHA1(packFile)
                );


                byte[] data = pack.data().toByteArray();
                exchange.getResponseHeaders().set("Content-Type", "application/zip");
                exchange.sendResponseHeaders(200, data.length);
                try (OutputStream responseStream = exchange.getResponseBody()) {
                    responseStream.write(data);
                }
            };

            this.server = ResourcePackServer.server()
                    .address(DFProxyPlugin.config.resource_pack_hosting.getAddress(), DFProxyPlugin.config.resource_pack_hosting.getPort())
                    .executor(Executors.newFixedThreadPool(2))
                    .handler(handler)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.server != null) {
            this.server.start();
        } else {
            DFProxyPlugin.logger.warn("Resource pack server failed to start!");
        }
    }

    public static String getSHA1(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            FileInputStream fis = new FileInputStream(file);
            byte[] byteArray = new byte[1024];
            int bytesCount;

            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
            fis.close();

            StringBuilder sb = new StringBuilder();
            for (byte b : digest.digest()) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void gracefullyShutdown() {
        this.server.stop(0);
    }
}
