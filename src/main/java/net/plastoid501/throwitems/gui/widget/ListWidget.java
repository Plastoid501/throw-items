package net.plastoid501.throwitems.gui.widget;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.plastoid501.throwitems.config.Configs;
import net.plastoid501.throwitems.config.ModConfig;
import net.plastoid501.throwitems.gui.ItemListScreen;
import net.plastoid501.throwitems.gui.ListScreen;
import net.plastoid501.throwitems.util.ItemUtil;
import net.plastoid501.throwitems.util.JsonUtil;
import net.plastoid501.throwitems.util.NbtUtil;

import java.util.List;

public class ListWidget extends ElementListWidget<ListWidget.Entry> {
    final ListScreen parent;
    private final MinecraftClient client;

    public ListWidget(ListScreen parent, MinecraftClient client, String listName) {
        //super(client, parent.width + 45, parent.height, 48, parent.height - 32, 23);
        super(client, parent.width + 45, parent.height - 80, 48, 23);
        this.parent = parent;
        this.client = client;
        this.initEntries(client, listName);
    }

    private void initEntries(MinecraftClient client, String listName) {
        ModConfig config = JsonUtil.readConfig();
        if (config != null) {
            String search = ListScreen.searchBox.getText();
            this.addEntry(new CategoryEntry(Text.literal("-- " + listName + " --"), client.textRenderer));
            for (ItemStack stack : Configs.throwItems.getStacks().get(listName)) {
                String itemId = Registries.ITEM.getId(stack.getItem()).toString();
                if (!search.isEmpty() && !itemId.contains(search)) {
                    continue;
                }
                this.addEntry(new ItemEntry(Text.literal(itemId), stack, client.textRenderer, config, listName));
            }
            this.addEntry(new CategoryEntry(Text.literal(""), client.textRenderer));
        }
    }

    public int getRowWidth() {
        return super.getRowWidth() + 250;
    }

    public static class CategoryEntry extends Entry {
        private final TextWidget categoryText;
        CategoryEntry(Text CategoryName, TextRenderer textRenderer) {
            this.categoryText = new TextWidget(CategoryName, textRenderer);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.categoryText);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.categoryText);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.categoryText.setPosition(x + 170, y + 5);
            this.categoryText.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        void update() {

        }
    }


    public class ItemEntry extends Entry {
        private final ItemStack stack;
        private final TextWidget itemText;
        private final String listName;
        private final ButtonWidget addButton;
        private final ButtonWidget removeButton;
        private final TextRenderer textRenderer;

        ItemEntry(Text itemId, ItemStack stack, TextRenderer textRenderer, ModConfig config, String listName) {
            this.textRenderer = textRenderer;
            this.stack = stack;
            this.itemText = new TextWidget(itemId, this.textRenderer);
            this.listName = listName;
            this.addButton = ButtonWidget.builder(Text.literal("Add"), button -> {
                if (!ItemUtil.contains(Configs.throwItems.getStacks().get(this.listName), stack)) {
                    if (ListWidget.this.client.world != null) {
                        NbtUtil.addItemStack(stack, ListWidget.this.client.world.getRegistryManager());
                        ItemListScreen.update();
                        this.update();
                    }
                }
            }).dimensions(0, 0, 50, 20).build();
            this.removeButton = ButtonWidget.builder(Text.literal("Remove"), button -> {
                int index = ItemUtil.indexOf(Configs.throwItems.getStacks().get(this.listName), stack);
                if (index != -1 && ListWidget.this.client.world != null) {
                    NbtUtil.removeItemStack(index, ListWidget.this.client.world.getRegistryManager());
                    ItemListScreen.update();
                    this.update();
                }
            }).dimensions(0, 0, 50, 20).build();
            this.addButton.active = !ItemUtil.contains(Configs.throwItems.getStacks().get(this.listName), stack);
            this.removeButton.active = ItemUtil.contains(Configs.throwItems.getStacks().get(this.listName), stack);

        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.itemText, this.addButton, this.removeButton);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.addButton, this.removeButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawItem(this.stack, x + 18, y + 2);
            if (x + 18 <= mouseX && mouseX <= x + 18 + 16 && y + 2 <= mouseY && mouseY <= y + 2 + 16) {
                context.drawItemTooltip(this.textRenderer, this.stack, mouseX, mouseY);
            }
            this.itemText.setPosition(x + 38, y + 5);
            this.itemText.render(context, mouseX, mouseY, tickDelta);
            this.addButton.setPosition(x + 332, y);
            this.addButton.render(context, mouseX, mouseY, tickDelta);
            this.removeButton.setPosition(x + 385, y);
            this.removeButton.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        void update() {
            if (ItemUtil.contains(Configs.throwItems.getStacks().get(this.listName), this.stack)) {
                this.addButton.active = false;
                this.removeButton.active = true;
            } else {
                this.addButton.active = true;
                this.removeButton.active = false;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<Entry> {
        public Entry() {
        }

        abstract void update();
    }
}
