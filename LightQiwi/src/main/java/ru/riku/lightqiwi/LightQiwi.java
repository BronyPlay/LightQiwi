package ru.riku.lightqiwi;

import org.bukkit.plugin.java.JavaPlugin;
import ru.riku.lightqiwi.Configurations.MainConfiguration;
import ru.riku.lightqiwi.Configurations.MessagesConfiguration;

public final class LightQiwi extends JavaPlugin {

    private static LightQiwi instance;
    public static LightQiwi getInstance() {
        return instance;
    }

    private static String qiwiToken;
    public static String getQiwi() {
        return qiwiToken;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        instance = this;
        MainConfiguration.getMain().setUp();
        MessagesConfiguration.getMessages().setUp();
        reloadToken();
        getCommand("lightqiwi").setExecutor(new QiwiCommands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void reloadToken() {
        qiwiToken = MainConfiguration.getMain().getConfig().getString("LightQiwi.Token");
    }
}
