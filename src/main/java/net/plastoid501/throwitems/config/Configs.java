package net.plastoid501.throwitems.config;

import net.minecraft.item.ItemStack;
import net.plastoid501.throwitems.config.json.JHotkeyConfig;
import net.plastoid501.throwitems.config.json.JThrowItemConfig;

import java.util.*;

public class Configs {
    private static final List<String> toggleNames = new ArrayList<>();
    private static final List<String> hotkeyNames = new ArrayList<>();
    public static Map<String, ThrowItemConfig> toggleHotkeys = new LinkedHashMap<>();
    public static Map<String, JThrowItemConfig> jToggleHotkeys = new LinkedHashMap<>();
    public static Map<String, HotkeyConfig> hotkeys = new LinkedHashMap<>();
    public static Map<String, JHotkeyConfig> jHotkeys = new LinkedHashMap<>();

    public static ThrowItemConfig throwItems = new ThrowItemConfig("throwItems", true, new ArrayList<>(), "whitelist", new LinkedHashMap<>(), "If true, this mod is enable.");

    public static HotkeyConfig openGUI = new HotkeyConfig("openGUI", new ArrayList<>(), "If set hotkey, open config gui.");
    public static HotkeyConfig storeItem = new HotkeyConfig("storeItem", new ArrayList<>(), "If set hotkey, store item to selected list when Inventory screen.");

    public static ModConfig config;

    static {
        Map<String, List<ItemStack>> stacks = new LinkedHashMap<>();
        stacks.put("whitelist", new ArrayList<>());
        stacks.put("blacklist", new ArrayList<>());
        throwItems.setStacks(stacks);

        toggleNames.add("throwItems");

        hotkeyNames.add("openGUI");
        hotkeyNames.add("storeItem");

        //toggle
        toggleHotkeys.put("throwItems", new ThrowItemConfig("throwItems", false, new ArrayList<>(), "whitelist", stacks, throwItems.getNarrator()));

        toggleHotkeys.forEach((key, value) -> jToggleHotkeys.put(key, new JThrowItemConfig(value.isEnable(), value.getKeys(), value.getSelected())));


        //hotkey
        hotkeys.put("openGUI", openGUI);
        hotkeys.put("storeItem", storeItem);

        hotkeys.forEach((key, value) -> jHotkeys.put(key, new JHotkeyConfig(value.getKeys())));


        config = new ModConfig(jToggleHotkeys, jHotkeys);

    }

    public static List<String> getToggleNames() {
        return toggleNames;
    }

    public static List<String> getHotkeyNames() {
        return hotkeyNames;
    }

    public static Map<String, ThrowItemConfig> getToggleHotkeys(){
        return toggleHotkeys;
    }

    public static Map<String, JThrowItemConfig> getJToggleHotkeys() {
        return jToggleHotkeys;
    }

    public static Map<String, HotkeyConfig> getHotkeys(){
        return hotkeys;
    }

    public static Map<String, JHotkeyConfig> getJHotkeys() {
        return jHotkeys;
    }

}
