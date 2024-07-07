package by.srinater.dragonsurvivalfix.mixin;

import by.dragonsurvivalteam.dragonsurvival.client.handlers.DragonSkinReloadHandle;
import by.dragonsurvivalteam.dragonsurvival.client.skins.DragonSkins;
import by.srinater.dragonsurvivalfix.utils.DragonSkinsNewFields;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DragonSkinReloadHandle.class)
public class DragonSkinReloadHandleMixin {
    /**
     * @author
     * @reason
     */

    @Overwrite(remap = false)
    public static void onReloadEvent(AddReloadListenerEvent reloadEvent)
    {
        DragonSkinsNewFields.initialized = false;
        DragonSkins.init();
    }
}
