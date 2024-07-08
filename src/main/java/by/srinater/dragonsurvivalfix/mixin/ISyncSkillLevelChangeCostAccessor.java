package by.srinater.dragonsurvivalfix.mixin;

import by.dragonsurvivalteam.dragonsurvival.network.magic.SyncSkillLevelChangeCost;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SyncSkillLevelChangeCost.class)
public interface ISyncSkillLevelChangeCostAccessor {
    @Accessor(remap = false)
    String getSkill();

    @Accessor(remap = false)
    int getLevelChange();
}
