package ru.riku.lightqiwi.Configurations;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.riku.lightqiwi.LightQiwi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MainConfiguration {

    private static MainConfiguration mcfg = new MainConfiguration();

    private File cfgFile;
    private FileConfiguration cfg;

    public void setUp() {
        if (cfgFile == null) {
            cfgFile = new File(LightQiwi.getInstance().getDataFolder(), "QiwiConfiguration.yml");
        }
        cfg = YamlConfiguration.loadConfiguration(cfgFile);
        if (!cfgFile.exists()) {
            try (InputStream in = LightQiwi.getInstance().getResource("QiwiConfiguration.yml")) {
                Files.copy(in, cfgFile.toPath());
                cfg = YamlConfiguration.loadConfiguration(cfgFile);
                Bukkit.getServer().getConsoleSender().sendMessage("[LightEconomy] Файл конфигурации успешно создан.");
            } catch (IOException e) {
                Bukkit.getServer().getConsoleSender().sendMessage("[LightEconomy] Произошла ошибка при создании файла конфигурации.");
            }
        }
    }

    public void reloadConfig() {
        cfgFile = new File(LightQiwi.getInstance().getDataFolder(), "QiwiConfiguration.yml");
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
            Bukkit.getConsoleSender().sendMessage("Ошибка в сохранении файла конфигурации: " + cfgFile);
        }
    }

    public static MainConfiguration getMain() {
        return mcfg;
    }

}
