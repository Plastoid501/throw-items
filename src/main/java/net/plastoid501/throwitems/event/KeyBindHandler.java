package net.plastoid501.throwitems.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.plastoid501.throwitems.config.Configs;
import net.plastoid501.throwitems.config.ModConfig;
import net.plastoid501.throwitems.config.json.JThrowItemConfig;
import net.plastoid501.throwitems.gui.ConfigScreen;
import net.plastoid501.throwitems.util.JsonUtil;
import net.plastoid501.throwitems.util.KeyCodeUtil;
import net.plastoid501.throwitems.util.NbtUtil;
import net.plastoid501.throwitems.util.SlotUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindHandler {
    private MinecraftClient client;
    private static final KeyBindHandler INSTANCE = new KeyBindHandler();

    public KeyBindHandler() {
        this.client = MinecraftClient.getInstance();
    }

    public static KeyBindHandler getInstance() {
        return INSTANCE;
    }

    public void handleInputEvents() {
        if (this.client.player == null || this.client.world == null) {
            return;
        }

        Screen screen = this.client.currentScreen;

        if (!(screen == null || screen instanceof InventoryScreen)) {
            return;
        }

        ModConfig config = Configs.config;
        if (config == null) {
            return;
        }

        if (screen == null) {
            if (Configs.openGUI.isPressed(false)) {
                JsonUtil.updateConfigs();
                this.client.setScreen(new ConfigScreen(screen));
            } else if (Configs.throwItems.isPressed(true)) {
                this.UpdateAndSendMessage(Configs.throwItems.getId(), Configs.throwItems.getJsonConfig());
            }
        } else {
            if (Configs.storeItem.isPressed(true)) {
                Slot slot = SlotUtil.getMouseSlot(screen);
                if (slot == null || slot.getStack().isEmpty()) {
                    return;
                }
                NbtUtil.addItemStack(slot.getStack());
            }
        }
    }

    public void UpdateAndSendMessage(String name, JThrowItemConfig config) {
        JsonUtil.updateThrowItemConfig(name, new JThrowItemConfig(!config.isEnable(), config.getKeys(), config.getSelected()));
        this.sendMessage(name, config.isEnable());
    }

    public void sendMessage(String name, boolean enable) {
        this.client.player.sendMessage(
                Text.of(name + ": ").copy()
                        .append(Text.of(String.valueOf(enable)).copy()
                                .formatted(enable ? Formatting.GREEN : Formatting.RED)),
                true);
    }

    public void update() {
        if (this.client == null || this.client.inGameHud == null) {
            return;
        }
        if (KeyCodeUtil.updateKeyStatus()) {
            this.handleInputEvents();
        }
    }

    public void updateKeyboard(long window, int key, int scancode, int action, int modifiers) {
        if (this.client == null || this.client.inGameHud == null) {
            return;
        }

        if (action == GLFW.GLFW_RELEASE) {
            KeyCodeUtil.remove(key);
        } else if (action == GLFW.GLFW_PRESS) {
            KeyCodeUtil.add(key);
        }

        this.handleInputEvents();
    }

    public void updateMouse(long window, int button, int action, int mods) {
        if (this.client == null || this.client.player == null || this.client.world == null) {
            return;
        }

        if (action == GLFW.GLFW_RELEASE) {
            KeyCodeUtil.remove(button);
        } else if (action == GLFW.GLFW_PRESS) {
            KeyCodeUtil.add(button);
        }

        this.handleInputEvents();
    }


}
