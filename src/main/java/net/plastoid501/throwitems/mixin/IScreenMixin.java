package net.plastoid501.throwitems.mixin;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface IScreenMixin {
    @Invoker("clearAndInit")
    void throw_items$clearAndInit();
}
