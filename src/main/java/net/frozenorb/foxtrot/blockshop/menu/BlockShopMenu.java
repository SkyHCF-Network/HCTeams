/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.blockshop.menu;

import com.google.common.collect.Maps;
import net.frozenorb.foxtrot.blockshop.menu.button.BlockShopButton;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import net.skyhcf.atmosphere.shared.chat.BukkitChat;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class BlockShopMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Block Shop";
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return (9 * 5);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(10, new BlockShopButton(Material.STONE, 64, 500));
        buttons.put(11, new BlockShopButton(Material.GRASS, 64, 500));
        buttons.put(12, new BlockShopButton(Material.DIRT, 64, 500));
        buttons.put(13, new BlockShopButton(Material.COBBLESTONE, 64, 500));
        buttons.put(14, new BlockShopButton(Material.WOOD, 64, 500));
        buttons.put(15, new BlockShopButton(Material.WOOD, (byte) 1,64, 500));
        buttons.put(16, new BlockShopButton(Material.WOOD, (byte) 2,64, 500));
        buttons.put(19, new BlockShopButton(Material.WOOD, (byte) 3, 64, 500));
        buttons.put(20, new BlockShopButton(Material.WOOD, (byte) 4, 64, 500));
        buttons.put(22, new BlockShopButton(Material.WOOD, (byte) 5, 64, 500));
        buttons.put(23, new BlockShopButton(Material.SAND, 64, 500));
        buttons.put(24, new BlockShopButton(Material.SAND, (byte) 1,64, 500));
        buttons.put(25, new BlockShopButton(Material.GRAVEL, 64, 500));
        buttons.put(28, new BlockShopButton(Material.LOG, 64, 500));
        buttons.put(29, new BlockShopButton(Material.LOG, (byte) 1, 64, 500));
        buttons.put(30, new BlockShopButton(Material.LOG, (byte) 2, 64, 500));
        buttons.put(31, new BlockShopButton(Material.LOG, (byte) 3, 64, 500));
        buttons.put(32, new BlockShopButton(Material.SPONGE, 64, 500));
        buttons.put(33, new BlockShopButton(Material.GLASS, 64, 500));
        buttons.put(34, new BlockShopButton(Material.WOOL, 64, 500));
        return buttons;
    }

    @Override
    public void onOpen(Player player) {
        player.closeInventory();
        player.sendMessage(BukkitChat.format("&cBlock Shop is currently disabled."));
    }
}
