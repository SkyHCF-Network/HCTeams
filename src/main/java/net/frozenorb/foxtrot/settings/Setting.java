package net.frozenorb.foxtrot.settings;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.settings.menu.button.SettingButton;
import net.frozenorb.foxtrot.tab.TabListMode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collection;

@AllArgsConstructor
public enum  Setting {

    PUBLIC_CHAT(
            ChatColor.RED + "Public Chat",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "public chat messages?"
            ),
            Material.SIGN,
            ChatColor.YELLOW + "Show public chat",
            ChatColor.YELLOW + "Hide public chat",
            true
    ) {

        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId());

            Foxtrot.getInstance().getToggleGlobalChatMap().setGlobalChatToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") + ChatColor.YELLOW + " to see global chat messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId());
        }

    },
    FOUND_DIAMONDS(
            ChatColor.RED + "Found Diamonds",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "found-diamonds messages?"
            ),
            Material.DIAMOND,
            ChatColor.YELLOW + "Show messages",
            ChatColor.YELLOW + "Hide messages",
            true
    ) {

        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getToggleFoundDiamondsMap().isFoundDiamondToggled(player.getUniqueId());

            Foxtrot.getInstance().getToggleFoundDiamondsMap().setFoundDiamondToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") + ChatColor.YELLOW + " to see found diamond messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getToggleFoundDiamondsMap().isFoundDiamondToggled(player.getUniqueId());
        }

    },
    TAB_LIST(
            ChatColor.RED + "Tab List Info",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "extra info on your",
                    ChatColor.BLUE + "tab list?"
            ),
            Material.ENCHANTED_BOOK,
            ChatColor.YELLOW + "Show info on tab",
            ChatColor.YELLOW + "Show default tab",
            true
    ) {

        @Override
        public void toggle(Player player) {
            TabListMode mode = SettingButton.next(Foxtrot.getInstance().getTabListModeMap().getTabListMode(player.getUniqueId()));

            Foxtrot.getInstance().getTabListModeMap().setTabListMode(player.getUniqueId(), mode);
            player.sendMessage(ChatColor.YELLOW + "You've set your tab list mode to " + ChatColor.LIGHT_PURPLE + mode.getName() + ChatColor.YELLOW + ".");
        }

        @Override
        public boolean isEnabled(Player player) {
            return true;
        }

    },
    DEATH_MESSAGES(
            ChatColor.RED + "Death Messages",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
                    ChatColor.BLUE + "death messages?"
            ),
            Material.SKULL_ITEM,
            ChatColor.YELLOW + "Show messages",
            ChatColor.YELLOW + "Hide messages",
            true
    ) {
        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getToggleDeathMessageMap().areDeathMessagesEnabled(player.getUniqueId());

            Foxtrot.getInstance().getToggleDeathMessageMap().setDeathMessagesEnabled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") + ChatColor.YELLOW + " to see death messages.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getToggleDeathMessageMap().areDeathMessagesEnabled(player.getUniqueId());
        }
    },
    SCOREBOARD_CLAIM(
            ChatColor.RED + "Claim on Scoreboard",
            ImmutableList.of(
                    ChatColor.BLUE + "Do you want to see",
            ChatColor.BLUE + "your current claim on the scoreboard?"
    ),
    Material.MAP,
    ChatColor.YELLOW + "Show scoreboard claim",
    ChatColor.YELLOW + "Hide scoreboard claim",
            true
            ) {

        @Override
        public void toggle(Player player) {
            boolean value = !Foxtrot.getInstance().getToggleGlobalChatMap().isGlobalChatToggled(player.getUniqueId());

            Foxtrot.getInstance().getToggleGlobalChatMap().setGlobalChatToggled(player.getUniqueId(), value);
            player.sendMessage(ChatColor.YELLOW + "You are now " + (value ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") + ChatColor.YELLOW + " to see your current claim on the scoreboard.");
        }

        @Override
        public boolean isEnabled(Player player) {
            return Foxtrot.getInstance().getClaimOptionMap().isScoreboardClaimEnabled(player.getUniqueId());
        }

    };

    @Getter private String name;
    @Getter private Collection<String> description;
    @Getter private Material icon;
    @Getter private String enabledText;
    @Getter private String disabledText;
    private boolean defaultValue;

    // Using @Getter means the method would be 'isDefaultValue',
    // which doesn't correctly represent this variable.
    public boolean getDefaultValue() {
        return (defaultValue);
    }

    public SettingButton toButton() {
        return new SettingButton(this);
    }

    public abstract void toggle(Player player);

    public abstract boolean isEnabled(Player player);

}
