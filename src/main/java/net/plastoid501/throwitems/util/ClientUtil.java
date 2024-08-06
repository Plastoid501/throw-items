package net.plastoid501.throwitems.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.GameMode;
import net.plastoid501.throwitems.config.Configs;
import net.plastoid501.throwitems.gui.ConfigScreen;
import net.plastoid501.throwitems.gui.ItemListScreen;
import net.plastoid501.throwitems.gui.ListScreen;

public class ClientUtil {
    public static void throwItems(MinecraftClient client) {
        if (!Configs.throwItems.isEnable()) {
            return;
        }

        if (client.player == null || client.interactionManager == null) {
            return;
        }

        if (!isSurvival(client)) {
            return;
        }

        Screen screen = client.currentScreen;
        ScreenHandler handler = client.player.currentScreenHandler;
        if ((screen == null && handler instanceof PlayerScreenHandler) ||
                screen instanceof BookEditScreen ||
                screen instanceof BookScreen ||
                screen instanceof LecternScreen ||
                screen instanceof SignEditScreen ||
                screen instanceof ChatScreen ||
                screen instanceof ConfigScreen ||
                screen instanceof ItemListScreen ||
                screen instanceof ListScreen
        ) {
            DefaultedList<ItemStack> inventory = client.player.getInventory().main;
            for (int slot = 0; slot < inventory.size(); slot++) {
                if (ItemUtil.contains(Configs.throwItems.getSelectedStacks(), inventory.get(slot))) {
                    if (0 <= slot && slot < 9) {
                        client.interactionManager.clickSlot(handler.syncId, slot + 36, 1, SlotActionType.THROW, client.player);
                    } else {
                        client.interactionManager.clickSlot(handler.syncId, slot, 1, SlotActionType.THROW, client.player);
                    }
                }
            }
        } else if (screen != null && !(handler instanceof PlayerScreenHandler && !(screen instanceof InventoryScreen))) {
            for (Slot slot : handler.slots) {
                if (ItemUtil.contains(Configs.throwItems.getSelectedStacks(), slot.getStack())) {
                    ItemUtil.throwItems(screen, slot.id);
                }
            }
        }
    }

    public static boolean isSurvival(MinecraftClient client) {
        if (client.player != null && client.getNetworkHandler() != null) {
            PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getGameProfile().getId());
            return playerListEntry != null && playerListEntry.getGameMode() == GameMode.SURVIVAL;
        }
        return false;
    }
}
