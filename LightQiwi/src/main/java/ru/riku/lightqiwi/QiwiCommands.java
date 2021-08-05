package ru.riku.lightqiwi;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.riku.lightqiwi.Configurations.MainConfiguration;
import ru.riku.lightqiwi.Configurations.MessagesConfiguration;
import ru.riku.lightqiwi.Utils.Messages;
import ru.riku.lightqiwi.Utils.QiwiModule;

public class QiwiCommands implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                for (String s : MessagesConfiguration.getMessages().getConfig().getStringList("Messages.Help")) {
                    p.sendMessage(Messages.color(s));
                }
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "reload": {
                    if (p.hasPermission("lightqiwi.reload")) {
                        MainConfiguration.getMain().reloadConfig();
                        MessagesConfiguration.getMessages().reloadConfig();
                        LightQiwi.reloadToken();
                        Messages.sendMessage(p, "Messages.Another.Reloaded");
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1L, 1L);
                        break;
                    } else {
                        Messages.sendMessage(p, "Messages.Another.NoPerm");
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1L, 1L);
                        break;
                    }
                }
                case "generate": {
                    p.sendMessage(LightQiwi.getQiwi());
                    QiwiModule.generateBill(p, Integer.parseInt(args[1]));
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1L, 1L);
                    break;
                }
                case "check": {
                    if (QiwiModule.getClients().containsKey(p.getUniqueId())) {
                        QiwiModule.checkBill(p);
                    } else {
                        Messages.sendMessage(p, "Messages.Another.NoBill");
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1L, 1L);
                    }
                    break;
                }
                case "reject": {
                    if (QiwiModule.getClients().containsKey(p.getUniqueId())) {
                        QiwiModule.getClients().remove(p.getUniqueId());
                        Messages.sendMessage(p, "Messages.Another.BillRejected");
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1L, 1L);
                    } else {
                        Messages.sendMessage(p, "Messages.Another.NoBill");
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1L, 1L);
                    }
                    break;
                }
            }
        } else {
            sender.sendMessage("Не для консоли.");
        }
        return false;
    }
}
