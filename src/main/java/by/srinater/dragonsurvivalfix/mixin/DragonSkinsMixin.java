package by.srinater.dragonsurvivalfix.mixin;

import by.dragonsurvivalteam.dragonsurvival.client.skins.DragonSkins;
import by.srinater.dragonsurvivalfix.utils.DragonSkinsNewFields;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonSkins.class)
public class DragonSkinsMixin {
    @Inject(method = "init", at = @At("HEAD"), cancellable = true, remap = false)
    private static void init(CallbackInfo ci)
    {
        if (DragonSkinsNewFields.initialized) {
            ci.cancel();
            return;
        }
        DragonSkinsNewFields.initialized = true;
    }

}
