package ru.riku.lightqiwi.Utils;

import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.CreateBillInfo;
import com.qiwi.billpayments.sdk.model.in.Customer;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import com.qiwi.billpayments.sdk.web.ApacheWebClient;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.riku.lightqiwi.Configurations.MainConfiguration;
import ru.riku.lightqiwi.Configurations.MessagesConfiguration;
import ru.riku.lightqiwi.LightQiwi;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.HashMap;
import java.util.UUID;

public class QiwiModule {

        public static BillPaymentClient client = BillPaymentClientFactory.createCustom(LightQiwi.getQiwi(), new ApacheWebClient(HttpClients.createDefault()));

        private static HashMap<UUID, String> clients = new HashMap<>();
        public static HashMap<UUID, String> getClients() {
            return clients;
        }

        public static void generateBill(Player p, int sum) {
            if (!getClients().containsKey(p.getUniqueId())) {
                CreateBillInfo billInfo = new CreateBillInfo(
                        UUID.randomUUID().toString(), new MoneyAmount(
                        BigDecimal.valueOf(sum),
                        Currency.getInstance("RUB")),
                        "Пополнение баланса на проекте LiwoCraft: Для игрока " + p.getName() + " на сумму  " + sum*MainConfiguration.getMain().getConfig().getInt("LightQiwi.Multiplication") + " игровых рублей.", ZonedDateTime.now().plusHours(1),
                        new Customer(
                                "example@mail.org",
                                UUID.randomUUID().toString(),
                                "79123456789"), "https://vk.com/fateabyss");
                try {
                    BillResponse response = client.createBill(billInfo);
                    clients.put(p.getUniqueId(), response.getBillId());
                    TextComponent msg = new TextComponent(MessagesConfiguration.getMessages().getConfig().getString("Messages.Json.Message").replace("&", "§"));
                    TextComponent action = new TextComponent(MessagesConfiguration.getMessages().getConfig().getString("Messages.Json.ActionMessage").replace("&", "§"));
                    action.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, response.getPayUrl()));
                    msg.addExtra(action);

                    p.spigot().sendMessage(msg);
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            } else {
                Messages.sendMessage(p, "Messages.Another.ActiveBill");
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1L, 1L);
            }
        }

        public static void onPaid(Player p) {
            BillResponse response = client.getBillInfo(getClients().get(p.getUniqueId()));
            int amount = response.getAmount().getValue().intValue() * MainConfiguration.getMain().getConfig().getInt("LightQiwi.Multiplication");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MainConfiguration.getMain().getConfig().getString("LightQiwi.Command").replace("%player%", p.getName()).replace("%amount%", "" + amount));
            Messages.sendMessage(p, "Messages.Status.Paid");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1L, 1L);
        }
        
        public static void checkBill(Player p) {
                BillResponse response = client.getBillInfo(getClients().get(p.getUniqueId()));
                switch (response.getStatus().getValue()) {
                    case PAID: {
                        int amount = response.getAmount().getValue().intValue() * MainConfiguration.getMain().getConfig().getInt("LightQiwi.Multiplication");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), MainConfiguration.getMain().getConfig().getString("LightQiwi.Command").replace("%player%", p.getName()).replace("%amount%", "" + amount));
                        Messages.sendMessage(p, "Messages.Status.Paid");
                        getClients().remove(p.getUniqueId());
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1L, 1L);
                        break;
                    }
                    case WAITING: {
                        Messages.sendMessage(p, "Messages.Status.Waiting");
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1L, 1L);
                        break;
                    }
                    case REJECTED: {
                        Messages.sendMessage(p, "Messages.Status.Rejected");
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1L, 1L);
                        break;
                    }
                    case EXPIRED: {
                        Messages.sendMessage(p, "Messages.Status.Expired");
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1L, 1L);
                        break;
                    }
                }
        }

}
