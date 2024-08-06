package net.plastoid501.throwitems.config;

import net.plastoid501.throwitems.config.json.JHotkeyConfig;
import net.plastoid501.throwitems.util.KeyCodeUtil;

import java.util.HashSet;
import java.util.List;

public class HotkeyConfig extends JHotkeyConfig {
    private String id;
    private String narrator;

    public HotkeyConfig(String id, List<String> keys, String narrator) {
        super(keys);
        this.id = id;
        this.narrator = narrator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNarrator() {
        return narrator;
    }

    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    public boolean isPressed(boolean checkPostKey) {
        List<Integer> keyCodes = KeyCodeUtil.getCodeForKey(this.getKeys());
        if (KeyCodeUtil.matchKeyCodes(keyCodes)) {
            if (checkPostKey) {
                if (!new HashSet<>(KeyCodeUtil.getLastPressedKeys()).containsAll(keyCodes)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

}
