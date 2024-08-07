package net.plastoid501.throwitems.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.plastoid501.throwitems.mixin.IHandledScreenMixin;

import java.util.Collection;
import java.util.Objects;

public class ItemUtil {
    public static void moveItems(Screen screen, int fromSlot, int toSlot){
        HandledScreen<? extends ScreenHandler> handler = (HandledScreen<? extends ScreenHandler>) screen;
        ((IHandledScreenMixin) handler).throw_items$onMouseClick(handler.getScreenHandler().getSlot(fromSlot), fromSlot, 0, SlotActionType.PICKUP);
        ((IHandledScreenMixin) handler).throw_items$onMouseClick(handler.getScreenHandler().getSlot(toSlot), toSlot, 0, SlotActionType.PICKUP);
    }

    public static void quickMoveItems(Screen screen, int fromSlot) {
        HandledScreen<? extends ScreenHandler> handler = (HandledScreen<? extends ScreenHandler>) screen;
        ((IHandledScreenMixin) handler).throw_items$onMouseClick(handler.getScreenHandler().getSlot(fromSlot), fromSlot, 0, SlotActionType.QUICK_MOVE);
    }

    public static void throwItems(Screen screen, int fromSlot) {
        HandledScreen<? extends ScreenHandler> handler = (HandledScreen<? extends ScreenHandler>) screen;
        ((IHandledScreenMixin) handler).throw_items$onMouseClick(handler.getScreenHandler().getSlot(fromSlot), fromSlot, 1, SlotActionType.THROW);
    }

    public static void pickupItems(Screen screen, int fromSlot){
        HandledScreen<? extends ScreenHandler> handler = (HandledScreen<? extends ScreenHandler>) screen;
        ((IHandledScreenMixin) handler).throw_items$onMouseClick(handler.getScreenHandler().getSlot(fromSlot), fromSlot, 0, SlotActionType.PICKUP);
    }

    public static void rightPickupItems(Screen screen, int fromSlot) {
        HandledScreen<? extends ScreenHandler> handler = (HandledScreen<? extends ScreenHandler>) screen;
        ((IHandledScreenMixin) handler).throw_items$onMouseClick(handler.getScreenHandler().getSlot(fromSlot), fromSlot, 1, SlotActionType.PICKUP);
    }

    public static boolean fixedCanCombine(ItemStack stack, ItemStack otherStack) {
        if (!stack.isOf(otherStack.getItem())) {
            return false;
        } else {
            if (stack.isEmpty() && otherStack.isEmpty()) {
                return true;
            }

            ComponentMap stackNbt = stack.getComponents();
            ComponentMap otherStackNbt = otherStack.getComponents();
            return Objects.equals(stackNbt, otherStackNbt) || ((stackNbt == null || stackNbt.isEmpty()) && (otherStackNbt == null || otherStackNbt.isEmpty()));
        }
    }

    public static boolean contains(Collection<? extends ItemStack> stacks, ItemStack stack) {
        for (ItemStack stack2 : stacks) {
            if (fixedCanCombine(stack, stack2)) {
                return true;
            }
        }

        return false;
    }

    public static int indexOf(Collection<? extends ItemStack> stacks, ItemStack stack) {
        int index = 0;
        for (ItemStack stack2 : stacks) {
            if (fixedCanCombine(stack, stack2)) {
                return index;
            }
            index++;
        }

        return -1;
    }

}
