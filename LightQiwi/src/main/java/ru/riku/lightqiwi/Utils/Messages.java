package ru.riku.lightqiwi.Utils;

import com.destroystokyo.paper.Title;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.riku.lightqiwi.Configurations.MessagesConfiguration;
import ru.riku.lightqiwi.LightQiwi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Messages {

    public static String color(String mes) {
        return ChatColor.translateAlternateColorCodes('&', mes);
    }

    public static List<String> color(List<String> mes) {
        List<String> list = new ArrayList<>();
        for (String s : mes) {
            list.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return list;
    }

    public static String getMessage(String path) {
        return color(MessagesConfiguration.getMessages().getConfig().getString(path));
    }

    public static void sendReplacedMessage(Player p, String message) {
        String[] messages = message.split("#");

        switch (messages[0].toLowerCase()) {
            case "actionbar": {
                p.sendActionBar(color(messages[1]));
                break;
            }
            case "title": {
                String[] title = messages[1].split("%n");
                if (title.length == 1) {
                    p.sendTitle(
                            Title.builder()
                                    .title(title[0] != null ? color(title[0]) : "")
                                    .build());
                    break;
                }
                p.sendTitle(
                        Title.builder()
                                .title(title[0] != null ? color(title[0]) : "")
                                .subtitle(title[1] != null ? color(title[1]) : "")
                                .build());
                break;
            }
            default: {
                p.sendMessage(color(messages[0]));
                break;
            }
        }
    }

    public static void sendMessage(Player p, String path) {
        String[] messages = MessagesConfiguration.getMessages().getConfig().getString(path).split("#");

        switch (messages[0].toLowerCase()) {
            case "actionbar": {
                p.sendActionBar(color(messages[1]));
                break;
            }
            case "title": {
                String[] title = messages[1].split("%n");
                if (title.length == 1) {
                    p.sendTitle(
                            Title.builder()
                                    .title(title[0] != null ? color(title[0]) : "")
                                    .build());
                    break;
                }
                p.sendTitle(
                        Title.builder()
                                .title(title[0] != null ? color(title[0]) : "")
                                .subtitle(title[1] != null ? color(title[1]) : "")
                                .build());
                break;
            }
            default: {
                p.sendMessage(color(messages[0]));
                break;
            }
        }
    }

    public static void sendRandomMessage(Player p, String config, String path) {
        List<String> msg = MessagesConfiguration.getMessages().getConfig().getStringList(path);
        int random = new Random().nextInt(msg.size());

        String[] messages = msg.get(random).split("#");

        switch (messages[0].toLowerCase()) {
            case "actionbar": {
                p.sendActionBar(color(messages[1]));
                break;
            }
            case "title": {
                String[] title = messages[1].split("%n");
                if (title.length == 1) {
                    p.sendTitle(
                            Title.builder()
                                    .title(title[0] != null ? color(title[0]) : "")
                                    .build());
                    break;
                }
                p.sendTitle(
                        Title.builder()
                                .title(title[0] != null ? color(title[0]) : "")
                                .subtitle(title[1] != null ? color(title[1]) : "")
                                .build());
                break;
            }
            default: {
                p.sendMessage(color(messages[0]));
                break;
            }
        }



    }


}
