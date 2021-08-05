package ru.riku.lightqiwi.Configurations;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.riku.lightqiwi.LightQiwi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MessagesConfiguration {

    private static MessagesConfiguration mcfg = new MessagesConfiguration();

    private File cfgFile;
    private FileConfiguration cfg;

    public void setUp() {
        if (cfgFile == null) {
            cfgFile = new File(LightQiwi.getInstance().getDataFolder(), "Messages.yml");
        }
        cfg = YamlConfiguration.loadConfiguration(cfgFile);
        if (!cfgFile.exists()) {
            try (InputStream in = LightQiwi.getInstance().getResource("Messages.yml")) {
                Files.copy(in, cfgFile.toPath());
                cfg = YamlConfiguration.loadConfiguration(cfgFile);
                Bukkit.getServer().getConsoleSender().sendMessage("[LightEconomy] Файл локализации успешно создан.");
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender().sendMessage("[LightEconomy] Произошла ошибка при создании файла локализации.");
            }
        }
    }

    public void reloadConfig() {
        cfgFile = new File(LightQiwi.getInstance().getDataFolder(), "Messages.yml");
        cfg = YamlConfiguration.loadConfiguration(cfgFile);
    }

    public FileConfiguration getConfig() {
        if (cfg == null) {
            reloadConfig();
        }
        return cfg;
    }

    public void saveConfig() {
        try {
            getConfig().save(cfgFile);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage("Ошибка в сохранении файла локализации: " + cfgFile);
        }
    }

    public static MessagesConfiguration getMessages() {
        return mcfg;
    }

}
