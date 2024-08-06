package net.plastoid501.throwitems.gui.widget;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.plastoid501.throwitems.config.Configs;
import net.plastoid501.throwitems.config.HotkeyConfig;
import net.plastoid501.throwitems.config.ModConfig;
import net.plastoid501.throwitems.config.ThrowItemConfig;
import net.plastoid501.throwitems.config.json.JThrowItemConfig;
import net.plastoid501.throwitems.gui.ConfigScreen;
import net.plastoid501.throwitems.gui.ListScreen;
import net.plastoid501.throwitems.mixin.IScreenMixin;
import net.plastoid501.throwitems.util.JsonUtil;
import net.plastoid501.throwitems.util.KeyCodeUtil;
import net.plastoid501.throwitems.util.NbtUtil;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ConfigWidget extends ElementListWidget<ConfigWidget.Entry> {
    final ConfigScreen parent;
    private final MinecraftClient client;
    private final ModConfig CONFIG = Configs.config;

    public ConfigWidget(ConfigScreen parent, MinecraftClient client) {
        super(client, parent.width + 155, parent.height, 20, parent.height - 32, 23);
        //super(client, parent.width + 155, parent.height - 52, 20, 23);
        this.parent = parent;
        this.client = client;
        this.initEntries(client);
    }

    private void initEntries(MinecraftClient client) {
        if (CONFIG != null) {
            this.addEntry(new CategoryEntry(Text.of("-- Throw Item --"), client.textRenderer));
            Configs.getToggleHotkeys().keySet().forEach((key) -> this.addEntry(new ThrowItemEntry(key, this.client.textRenderer, CONFIG)));
            this.addEntry(new CategoryEntry(Text.of(""), client.textRenderer));
            this.addEntry(new CategoryEntry(Text.of("-- List --"), client.textRenderer));
            //Configs.getToggleHotkeys().keySet().forEach((key) -> this.addEntry(new ThrowItemEntry(key, this.client.textRenderer, CONFIG)));
            Configs.throwItems.getStacks().keySet().forEach((key) -> this.addEntry(new ListEntry(key, this.client.textRenderer, CONFIG, this.client, this.parent)));
            this.addEntry(new AddListEntry(this.client.textRenderer));
            this.addEntry(new CategoryEntry(Text.of(""), client.textRenderer));
            this.addEntry(new CategoryEntry(Text.of("-- Hotkey --"), client.textRenderer));
            Configs.getHotkeys().keySet().forEach((key) -> this.addEntry(new HotkeyEntry(key, this.client.textRenderer, CONFIG)));
            this.addEntry(new CategoryEntry(Text.of(""), client.textRenderer));
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 85;
    }

    public int getRowWidth() {
        return super.getRowWidth() + 150;
    }

    public void update() {
        this.updateChildren();
    }

    public void updateChildren() {
        this.children().forEach(Entry::update);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client == null || this.client.player == null || this.client.world == null) {
            this.setRenderBackground(true);
        } else {
            this.setRenderBackground(false);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    public class CategoryEntry extends Entry {
        private final TextWidget text;
        private final int textWidth;
        CategoryEntry(Text CategoryName, TextRenderer textRenderer) {
            this.text = new TextWidget(CategoryName, textRenderer);
            this.textWidth = this.text.getWidth();
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.text);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.text);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.text.setPosition(ConfigWidget.this.client.currentScreen.width / 2 - this.textWidth / 2, y + 5);
            this.text.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        void update() {
        }

    }

    public class ThrowItemEntry extends Entry {
        private final ThrowItemConfig defaultConfig;
        private boolean enable;
        private List<String> keys;
        private int selected;
        private List<String> list;
        private final TextWidget text;
        private final ButtonWidget enableButton;
        private final ButtonWidget toggleButton;
        private final ButtonWidget editButton;
        private final ButtonWidget resetButton;

        ThrowItemEntry(String key, TextRenderer textRenderer, ModConfig config) {
            this.defaultConfig = Configs.getToggleHotkeys().get(key);
            this.enable = Configs.throwItems.isEnable();
            this.keys = Configs.throwItems.getKeys();
            this.list = Configs.throwItems.getStacks().keySet().stream().toList();
            this.selected = this.list.indexOf(Configs.throwItems.getSelected());
            this.text = new TextWidget(Text.literal(key), textRenderer);
            this.enableButton = ButtonWidget.builder(Text.literal(String.valueOf(this.enable)).setStyle(Style.EMPTY.withColor(this.enable ? Color.GREEN.getRGB() : Color.red.getRGB())), button -> {
                this.enable = !this.enable;
                JsonUtil.updateThrowItemConfig(key, new JThrowItemConfig(this.enable, this.keys, this.list.get(this.selected)));
                this.update();
            }).size(60, 20).build();
            this.toggleButton = ButtonWidget.builder(Text.literal(this.list.get(this.selected)), button -> {
                this.selected = this.selected + 1 >= this.list.size() ? 0 : this.selected + 1;
                JsonUtil.updateThrowItemConfig(key, new JThrowItemConfig(this.enable, this.keys, this.list.get(this.selected)));
                this.update();
            }).size(60, 20).build();
            this.editButton = ButtonWidget.builder(this.getKeyBindText(this.keys), button -> {
                ConfigWidget.this.parent.keyBinding = key;
                ConfigWidget.this.parent.keys = new ArrayList<>();
                this.update();
            }).size(160, 20).build();
            this.resetButton = ButtonWidget.builder(Text.literal("RESET"), button -> {
                this.enable = this.defaultConfig.isEnable();
                this.keys = this.defaultConfig.getKeys();
                this.selected = 0;
                JsonUtil.updateThrowItemConfig(key, new JThrowItemConfig(this.enable, this.keys, this.list.get(this.selected)));
                this.update();
            }).size(40, 20).build();
            this.resetButton.active = this.defaultConfig.isEnable() != this.enable || !Objects.equals(this.defaultConfig.getKeys(), this.keys);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.text, this.enableButton, this.toggleButton, this.editButton, this.resetButton);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.enableButton, this.toggleButton, this.editButton, this.resetButton);
        }

        private Text getKeyBindText(List<String> keys) {
            StringBuilder text = new StringBuilder();
            if (keys.isEmpty()) {
                if (!this.text.getMessage().getString().equals(ConfigWidget.this.parent.keyBinding)) {
                    text.append("NONE");
                    return Text.literal(text.toString());
                }
            } else {
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    text.append(key);
                    if (iterator.hasNext()) {
                        text.append(" + ");
                    }
                }
            }
            if (this.text.getMessage().getString().equals(ConfigWidget.this.parent.keyBinding)) {
                text.insert(0, "> ").append(" <");
            }
            return Text.literal(text.toString());

        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.text.setPosition(x - 122, y + 5);
            this.text.render(context, mouseX, mouseY, tickDelta);
            this.enableButton.setPosition(x + 27, y);
            this.enableButton.render(context, mouseX, mouseY, tickDelta);
            this.toggleButton.setPosition(x + 90, y);
            this.toggleButton.render(context, mouseX, mouseY, tickDelta);
            this.editButton.setPosition(x + 153, y);
            this.editButton.render(context, mouseX, mouseY, tickDelta);
            this.resetButton.setPosition(x + 316, y);
            this.resetButton.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        void update() {
            this.enableButton.setMessage(Text.literal(String.valueOf(this.enable)).setStyle(Style.EMPTY.withColor(this.enable ? Color.GREEN.getRGB() : Color.red.getRGB())));
            this.toggleButton.setMessage(Text.literal(this.list.get(this.selected)));
            if (this.text.getMessage().getString().equals(ConfigWidget.this.parent.keyBinding)) {
                this.keys = null;
                List<Integer> keyCodes = ConfigWidget.this.parent.keys;
                if (!keyCodes.isEmpty() && this.isEnd(keyCodes.get(keyCodes.size() - 1))) {
                    keyCodes.remove(keyCodes.size() - 1);
                    this.keys = KeyCodeUtil.getKeyForCode(keyCodes);
                    JsonUtil.updateThrowItemConfig(this.text.getMessage().getString(), new JThrowItemConfig(this.enable, this.keys, this.list.get(this.selected)));
                    this.editButton.setFocused(false);
                    ConfigWidget.this.parent.keyBinding = null;
                    this.editButton.setMessage(this.getKeyBindText(this.keys));
                    ConfigWidget.this.parent.keys = null;
                } else {
                    this.editButton.setMessage(this.getKeyBindText(KeyCodeUtil.getKeyForCode(keyCodes)));
                }
            } else {
                this.editButton.setMessage(this.getKeyBindText(this.keys));
            }
            this.resetButton.active = this.defaultConfig.isEnable() != this.enable || !Objects.equals(this.defaultConfig.getKeys(), this.keys);
        }

        private boolean isEnd(int keyCode) {
            return keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_MOUSE_BUTTON_1 || keyCode == GLFW.GLFW_MOUSE_BUTTON_2;
        }

    }

    public class HotkeyEntry extends Entry {
        private final HotkeyConfig defaultConfig;
        private List<String> keys;
        private final TextWidget text;
        private final ButtonWidget editButton;
        private final ButtonWidget resetButton;

        HotkeyEntry(String key, TextRenderer textRenderer, ModConfig config) {
            this.defaultConfig = Configs.getHotkeys().get(key);
            this.keys = config.getHotkeys().get(key).getKeys();
            this.text = new TextWidget(Text.literal(key), textRenderer);
            this.editButton = ButtonWidget.builder(this.getKeyBindText(this.keys), button -> {
                ConfigWidget.this.parent.keyBinding = key;
                ConfigWidget.this.parent.keys = new ArrayList<>();
                this.update();
            }).size(160, 20).build();
            this.resetButton = ButtonWidget.builder(Text.literal("RESET"), button -> {
                this.keys = this.defaultConfig.getKeys();
                JsonUtil.updateHotkeyConfig(key, this.keys);
                this.update();
            }).size(40, 20).build();
            this.resetButton.active = !Objects.equals(this.defaultConfig.getKeys(), this.keys);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.text, this.editButton, this.resetButton);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.editButton, this.resetButton);
        }


        private Text getKeyBindText(List<String> keys) {
            StringBuilder text = new StringBuilder();
            if (keys.isEmpty()) {
                if (!this.text.getMessage().getString().equals(ConfigWidget.this.parent.keyBinding)) {
                    text.append("NONE");
                    return Text.literal(text.toString());
                }
            } else {
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    text.append(key);
                    if (iterator.hasNext()) {
                        text.append(" + ");
                    }
                }
            }
            if (this.text.getMessage().getString().equals(ConfigWidget.this.parent.keyBinding)) {
                text.insert(0, "> ").append(" <");
            }
            return Text.literal(text.toString());

        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.text.setPosition(x - 122, y + 5);
            this.text.render(context, mouseX, mouseY, tickDelta);
            this.editButton.setPosition(x + 153, y);
            this.editButton.render(context, mouseX, mouseY, tickDelta);
            this.resetButton.setPosition(x + 316, y);
            this.resetButton.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        protected void update() {
            if (this.text.getMessage().getString().equals(ConfigWidget.this.parent.keyBinding)) {
                this.keys = null;
                List<Integer> keyCodes = ConfigWidget.this.parent.keys;
                if (!keyCodes.isEmpty() && this.isEnd(keyCodes.get(keyCodes.size() - 1))) {
                    keyCodes.remove(keyCodes.size() - 1);
                    this.keys = KeyCodeUtil.getKeyForCode(keyCodes);
                    JsonUtil.updateHotkeyConfig(this.text.getMessage().getString(), this.keys);
                    this.editButton.setFocused(false);
                    ConfigWidget.this.parent.keyBinding = null;
                    this.editButton.setMessage(this.getKeyBindText(this.keys));
                    ConfigWidget.this.parent.keys = null;
                } else {
                    this.editButton.setMessage(this.getKeyBindText(KeyCodeUtil.getKeyForCode(keyCodes)));
                }
            } else {
                this.editButton.setMessage(this.getKeyBindText(this.keys));
            }
            this.resetButton.active = !Objects.equals(this.defaultConfig.getKeys(), this.keys);
        }

        private boolean isEnd(int keyCode) {
            return keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_MOUSE_BUTTON_1 || keyCode == GLFW.GLFW_MOUSE_BUTTON_2;
        }
    }

    public class ListEntry extends Entry {
        private final TextWidget listText;
        private final ButtonWidget detailButton;
        private final ButtonWidget removeButton;

        ListEntry(String key, TextRenderer textRenderer, ModConfig config, MinecraftClient client, Screen parent) {
            this.listText = new TextWidget(Text.literal(key), textRenderer);
            this.detailButton = ButtonWidget.builder(Text.literal("Details"), button -> {
                if (client != null) {
                    JsonUtil.updateConfigs();
                    client.setScreen(new ListScreen(parent, key));
                }
            }).size(60, 20).build();
            this.removeButton = ButtonWidget.builder(Text.literal("Remove"), button -> {
                if (NbtUtil.removeNbtList(key)) {
                    ((IScreenMixin) parent).throw_items$clearAndInit();
                }
            }).size(60, 20).build();

        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.listText, this.detailButton, this.removeButton);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.detailButton, this.removeButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.listText.setPosition(x - 122, y + 5);
            this.listText.render(context, mouseX, mouseY, tickDelta);
            this.detailButton.setPosition(x + 230, y);
            this.detailButton.render(context, mouseX, mouseY, tickDelta);
            this.removeButton.setPosition(x + 293, y);
            this.removeButton.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        void update() {
        }
    }

    public class AddListEntry extends Entry {
        private final TextFieldWidget listText;
        private final ButtonWidget addButton;

        AddListEntry(TextRenderer textRenderer) {
            this.listText = new TextFieldWidget(textRenderer, 0, 0, 160, 16, Text.literal(""));
            this.listText.setText("");
            this.addButton = ButtonWidget.builder(Text.literal("+ Add"), button -> {
                String name = this.listText.getText();
                if (name.isEmpty() || name.isBlank()) {
                    return;
                }
                if (NbtUtil.addNewNbtList(name)) {
                    ((IScreenMixin) parent).throw_items$clearAndInit();
                }
            }).dimensions(0, 0, 60, 20).build();

        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.listText, this.addButton);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.listText, this.addButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.listText.setPosition(x + 130, y + 2);
            this.listText.render(context, mouseX, mouseY, tickDelta);
            this.addButton.setPosition(x + 293, y);
            this.addButton.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        void update() {
        }
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<Entry> {
        public Entry() {
        }

        abstract void update();
    }
}
