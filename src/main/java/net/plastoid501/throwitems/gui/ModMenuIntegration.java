package net.plastoid501.throwitems.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.plastoid501.throwitems.util.JsonUtil;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        JsonUtil.updateConfigs();
        return ConfigScreen::new;
    }
}
