package net.plastoid501.throwitems.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.world.ClientWorld;
import net.plastoid501.throwitems.event.KeyBindHandler;
import net.plastoid501.throwitems.util.ClientUtil;
import net.plastoid501.throwitems.util.NbtUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void preTick(CallbackInfo ci) {
        ClientUtil.throwItems((MinecraftClient) (Object) this);
    }

    @Inject(method = "tick", at = @At(value = "RETURN"))
    private void postTick(CallbackInfo ci) {
        KeyBindHandler handler = KeyBindHandler.getInstance();
        handler.update();
    }

    @Inject(method = "joinWorld", at = @At(value = "RETURN"))
    private void postJoinWorld(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci) {
        if (world == null) {
            return;
        }
        NbtUtil.updateNbt(world.getRegistryManager());
    }
}
