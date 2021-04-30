package net.frozenorb.foxtrot.settings.menu;

import com.google.common.collect.Maps;
import net.frozenorb.foxtrot.settings.Setting;
import net.frozenorb.qlib.menu.Button;
import net.frozenorb.qlib.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Map;

public class SettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Options";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        // I'm going to manually set the positions for now as we only have three options to make it pretty,
        // but once we add more we should probably use a for loop to add the buttons.
        buttons.put(0, Setting.FOUND_DIAMONDS.toButton());
        buttons.put(2, Setting.DEATH_MESSAGES.toButton());
        buttons.put(4, Setting.PUBLIC_CHAT.toButton());
        buttons.put(6, Setting.TAB_LIST.toButton());
        buttons.put(8, Setting.SCOREBOARD_CLAIM.toButton());
        return buttons;
    }

}
