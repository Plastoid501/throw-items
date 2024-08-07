package net.plastoid501.throwitems.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.plastoid501.throwitems.gui.widget.ConfigWidget;

import java.util.List;

public class ConfigScreen extends Screen {
    private ConfigWidget configList;
    private final Screen parent;

    public String keyBinding;
    public List<Integer> keys;
    //public boolean isActiveValue;


    public ConfigScreen(Screen parent) {
        super(Text.of("Throw Items"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.configList = new ConfigWidget(this, this.client);
        this.addSelectableChild(this.configList);
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
        }).dimensions(this.width / 2 - 100, this.height - 27, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.configList.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 0xFFFFFF);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keys != null) {
            if (!this.keys.contains(keyCode)) {
                this.keys.add(keyCode);
            }
            this.configList.update();
            return false;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (keys != null) {
            if (!this.keys.contains(button)) {
                this.keys.add(button);
            }
            this.configList.update();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);

    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

}
