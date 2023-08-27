package fr.sushi.relica;

import fr.sushi.relica.event.ForgeEvents;
import fr.sushi.relica.event.ModEvents;
import fr.sushi.relica.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Relica.MODID)
public class Relica {
    public static final String MODID = "relica";
    public Relica() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);

        ModEntities.BLOCK_ENTITIES.register(modEventBus);

        ModBlocks.BLOCKS.register(modEventBus);

        ModMenus.MENU_TYPES.register(modEventBus);

//        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);

        modEventBus.register(ModCreativeTabs.class);

        modEventBus.register(ModEvents.class);
        MinecraftForge.EVENT_BUS.register(ForgeEvents.class);
    }
}
