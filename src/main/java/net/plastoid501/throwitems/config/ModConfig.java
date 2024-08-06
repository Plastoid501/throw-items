package net.plastoid501.throwitems.config;

import net.plastoid501.throwitems.config.json.JHotkeyConfig;
import net.plastoid501.throwitems.config.json.JThrowItemConfig;

import java.util.Map;

public class ModConfig {
    private Map<String, JThrowItemConfig> ToggleHotkeys;
    private Map<String, JHotkeyConfig> Hotkeys;

    public ModConfig(Map<String, JThrowItemConfig> toggleHotkeys, Map<String, JHotkeyConfig> hotkeys) {
        this.ToggleHotkeys = toggleHotkeys;
        this.Hotkeys = hotkeys;
    }

    public Map<String, JThrowItemConfig> getToggleHotkeys() {
        return ToggleHotkeys;
    }

    public void setToggleHotkeys(Map<String, JThrowItemConfig> toggleHotkeys) {
        ToggleHotkeys = toggleHotkeys;
    }

    public Map<String, JHotkeyConfig> getHotkeys() {
        return Hotkeys;
    }

    public void setHotkeys(Map<String, JHotkeyConfig> hotkeys) {
        Hotkeys = hotkeys;
    }
}

