package net.plastoid501.throwitems.mixin;

import net.minecraft.client.Keyboard;
import net.plastoid501.throwitems.event.KeyBindHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At(value = "HEAD"))
    private void preOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        KeyBindHandler handler = KeyBindHandler.getInstance();
        handler.updateKeyboard(window, key, scancode, action, modifiers);
    }
}
