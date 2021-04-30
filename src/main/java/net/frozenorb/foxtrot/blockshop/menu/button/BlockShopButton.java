/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.blockshop.menu.button;

import com.google.common.collect.Lists;
import net.frozenorb.qlib.economy.FrozenEconomyHandler;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.util.ItemUtils;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BlockShopButton extends Button {

    private final Material material;
    private final byte data;
    private final int amount;
    private final double price;

    public BlockShopButton(Material material, byte data, int amount, double price){
        this.material = material;
        this.data = data;
        this.amount = amount;
        this.price = price;
    }

    public BlockShopButton(Material material, int amount, double price){
        this.material = material;
        this.data = 0;
        this.amount = amount;
        this.price = price;
    }

    @Override
    public String getName(Player player) {
        return BukkitChat.format("&b" + amount + "x " + ItemUtils.getName(new ItemStack(material)));
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = Lists.newArrayList();
        description.add("Your balance&7: &a$" + NumberFormat.getNumberInstance(Locale.US).format(FrozenEconomyHandler.getBalance(player.getUniqueId())));
        description.add("Price&7: &b" + NumberFormat.getNumberInstance(Locale.US).format(price));
        description.add("&r");
        description.add("&aClick to purchase.");
        int index = 0;
        for(String s : description){
            description.set(index++, BukkitChat.format(s));
        }
        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return material;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if(FrozenEconomyHandler.getBalance(player.getUniqueId()) >= price){
            ItemStack stack = new ItemStack(material);
            player.getInventory().addItem(stack);
            FrozenEconomyHandler.withdraw(player.getUniqueId(), price);
            player.sendMessage(BukkitChat.format("&aYou've purchased " + amount + "x " + ItemUtils.getName(stack) + "."));
        }else{
            player.sendMessage(BukkitChat.format("&cYou do not have enough money to purchase " + amount + "x " + ItemUtils.getName(new ItemStack(material)) + ". You need another &l$" + NumberFormat.getNumberInstance(Locale.US).format(price - FrozenEconomyHandler.getBalance(player.getUniqueId()) + "&r&c.")));
        }
    }
}
