package net.plastoid501.throwitems.config.json;

import java.util.List;

public class JHotkeyConfig {
    private List<String> keys;

    public JHotkeyConfig(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys){
        this.keys = keys;
    }
}
