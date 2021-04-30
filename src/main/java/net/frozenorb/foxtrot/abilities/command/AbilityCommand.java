/*
 * Made by KermanIsPretty
 */

package net.frozenorb.foxtrot.abilities.command;

import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.util.CC;
import net.frozenorb.foxtrot.util.ItemBuilder;
import net.frozenorb.qlib.command.Command;
import net.frozenorb.qlib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AbilityCommand {

    String prefix = "&b&lSkyHCF&7 ┃ ";

    @Command(names = {"ability give"}, permission = "skyhcf.ability")
    public static void giveability(Player sender, @Param(name = "player", defaultValue = "self") Player target, @Param(name = "item") String item, @Param(name = "amount") int amount){
        if (item.equalsIgnoreCase("switcher")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.SNOW_BALL).name("&b&lSwitcher").lore(Arrays.asList(CC.Color("&7Switch locations with any"), CC.Color("&7enemy within 7 blocks of you"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + String.valueOf(amount) + " switchers!");
            return;
        }
        if (item.equalsIgnoreCase("paralyser")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.BLAZE_ROD).name("&b&lParalyser").lore(Arrays.asList(CC.Color("&7Hit a player 3 times in a row with this item"), CC.Color("&7to restrict them from building for 15 seconds!"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + String.valueOf(amount) + " paralysers!");
            return;
        }
        if (item.equalsIgnoreCase("fakepearl")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.ENDER_PEARL).name("&b&lFake Pearl").lore(Arrays.asList(CC.Color("&7Right click this pearl"), CC.Color("&7to shoot a pearl to trick your enemies!"))).enchant(Enchantment.DURABILITY).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + String.valueOf(amount) + " fake pearls!");
        }
        if (item.equalsIgnoreCase("ragesoup")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.MUSHROOM_SOUP).name("&b&lRage Soup").lore(Arrays.asList(CC.Color("&7Right click this item to recieve"), CC.Color("&7Strength 2 and Resistance 3 for 10 seconds!"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + String.valueOf(amount) + " rage soup!");
        }
        if (item.equalsIgnoreCase("ninjastar")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.NETHER_STAR).name("&b&lNinja Star").lore(Arrays.asList(CC.Color("&7Right click this item to"), CC.Color("&7teleport to the player that last hit you!"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + amount + " ninja star!");
        }
        if (item.equalsIgnoreCase("potchecker")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.STICK).name("&b&lPot Checker").lore(Arrays.asList(CC.Color("&7Hit a player with this item to"), CC.Color("&7check how many potions they have left!"))).enchant(Enchantment.DURABILITY).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + amount + " pot checkers!");
        }
        if (item.equalsIgnoreCase("lifesaver")){
            target.getInventory().addItem(new ItemStack[] { new ItemBuilder(Material.WATCH).name("&b&lLife Saver").lore(Arrays.asList(CC.Color("&7You will have 30 seconds to go down to 3 hearts"), CC.Color("&7during this cooldown if you go down to 2 hearts you will be healed fully!"))).amount(amount).build()});
            sender.sendMessage(ChatColor.GREEN + "You have given " + target.getName() + " " + amount + " life savers!");
        }
    }

    @Command(names = {"ability reset"}, permission = "skyhcf.ability")
    public static void resetability(Player sender, @Param(name = "player", defaultValue = "self") Player target, @Param(name = "item") String item){
        if (item.equalsIgnoreCase("switcher")){
            if (Foxtrot.getInstance().getSwitcher().onCooldown(target)){
                Foxtrot.getInstance().getSwitcher().cooldownRemove(target);
                sender.sendMessage(ChatColor.GREEN + "You have removed the switcher cooldown from " + target.getName());
                return;
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + " is not on switcher cooldown!");
            }
        }
    }

    @Command(names = {"ability list"}, permission = "skyhcf.ability")
    public static void abilitylist(Player sender){
        sender.sendMessage((CC.Color("&7&m---------------------------")));
        sender.sendMessage((CC.Color("&6&lAbility List")));
        sender.sendMessage((CC.Color("&7- &b&lSwitcher")));
        sender.sendMessage((CC.Color("&7- &b&lParalyser")));
        sender.sendMessage((CC.Color("&7- &b&lFakePearl")));
        sender.sendMessage((CC.Color("&7- &b&lRageSoup")));
        sender.sendMessage((CC.Color("&7- &b&lNinjaStar")));
        sender.sendMessage((CC.Color("&7- &b&lPotChecker")));
        sender.sendMessage((CC.Color("&7- &b&lLifeSaver")));
        sender.sendMessage((CC.Color("&7&m---------------------------")));
    }
}
