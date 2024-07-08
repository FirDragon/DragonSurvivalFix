package by.srinater.dragonsurvivalfix.config;

import by.srinater.dragonsurvivalfix.DragonSurvivalFixMod;
import by.srinater.dragonsurvivalfix.config.attributes.ConfigComment;
import by.srinater.dragonsurvivalfix.config.attributes.ConfigItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = DragonSurvivalFixMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigLoader {
    public static ForgeConfigSpec CLIENT_SPEC;
    public static ForgeConfigSpec SERVER_SPEC;
    public static Map<Field, ForgeConfigSpec.ConfigValue<?>> CLIENT_CONFIG_VALUES = new HashMap<>();
    public static Map<Field, ForgeConfigSpec.ConfigValue<?>> SERVER_CONFIG_VALUES = new HashMap<>();
    public static void Initialize()
    {
        if (FMLEnvironment.dist.isClient()) {
            ForgeConfigSpec.Builder clientConfig = new ForgeConfigSpec.Builder();
            LoadConfig(clientConfig, Dist.CLIENT);
            CLIENT_SPEC = clientConfig.build();
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
        }
        ForgeConfigSpec.Builder serverConfig = new ForgeConfigSpec.Builder();
        LoadConfig(serverConfig, Dist.DEDICATED_SERVER);
        SERVER_SPEC = serverConfig.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    }
    protected static void LoadConfig(ForgeConfigSpec.Builder builder, Dist dist)
    {
        Map<Field, ForgeConfigSpec.ConfigValue<?>> configValues;
        if (dist.isClient())
            configValues = CLIENT_CONFIG_VALUES;
        else
            configValues = SERVER_CONFIG_VALUES;
        for (Field field : Config.class.getFields())
        {
            ConfigItem item = null;
            ConfigComment comment = null;
            for (Annotation annotation : field.getDeclaredAnnotations()){
                if (annotation instanceof ConfigItem configItem)
                    item = configItem;
                if (annotation instanceof ConfigComment configComment)
                    comment = configComment;
            }
            if (item == null || !Arrays.asList(item.dist()).contains(dist))
                continue;
            if (!Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) || !field.canAccess(null))
            {
                throw new ClassCastException(String.format("The member attribute of configuration item %s is incorrect", field.getName()));
            }
            String commentComponent = "";
            if (comment != null)
                commentComponent = comment.value();

            try {
                Object defaultValue = field.get(null);
                ForgeConfigSpec.ConfigValue<?> configValue = null;
                if (defaultValue instanceof Integer $integer){
                    configValue = builder.comment(commentComponent).define(List.of(new String[]{item.region(), item.name()}), $integer);
                } else if (defaultValue instanceof Boolean $boolean) {
                    configValue = builder.comment(commentComponent).define(List.of(new String[]{item.region(), item.name()}), $boolean);
                } else if (defaultValue instanceof String $string) {
                    configValue = builder.comment(commentComponent).define(List.of(new String[]{item.region(), item.name()}), $string);
                } else if (defaultValue instanceof Double $double) {
                    configValue = builder.comment(commentComponent).define(List.of(new String[]{item.region(), item.name()}), $double);
                }
                if (configValue != null)
                    configValues.put(field, configValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
        builder.build();
    }
    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event)
    {
        ApplyConfig(event.getConfig());
    }
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event)
    {
        ApplyConfig(event.getConfig());
    }
    protected static void ApplyConfig(ModConfig modConfig)
    {
        Map<Field, ForgeConfigSpec.ConfigValue<?>> configValues;
        if (modConfig.getSpec() == CLIENT_SPEC)
            configValues = CLIENT_CONFIG_VALUES;
        else
            configValues = SERVER_CONFIG_VALUES;

        for (var configItem : configValues.entrySet())
        {
            try {
                configItem.getKey().set(null, configItem.getValue().get());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
