package net.plastoid501.throwitems.util;

import net.fabricmc.loader.api.FabricLoader;
import net.plastoid501.throwitems.ThrowItems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static void generateClientModFolder() {
        Path path = getConfigPath().resolve(ThrowItems.MOD_ID);

        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch(IOException e) {
                ThrowItems.LOGGER.error(e.getMessage());
            }
        }

        NbtUtil.generateDefaultNbt();
    }




}
