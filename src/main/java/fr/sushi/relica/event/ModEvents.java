package fr.sushi.relica.event;

import fr.sushi.relica.Relica;
import fr.sushi.relica.block.entity.MachineBlockEntity;
import fr.sushi.relica.client.renderer.AbstractMachineRenderer;
import fr.sushi.relica.client.renderer.AltarRenderer;
import fr.sushi.relica.client.screen.AltarScreen;
import fr.sushi.relica.registry.ModEntities;
import fr.sushi.relica.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Relica.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void registerBlockRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModEntities.ALTAR.get(), AltarRenderer::new);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(ModMenus.ALTAR_MENU.get(), AltarScreen::new));
    }

//    @SubscribeEvent
//    public static void onTextureStitch(TextureStitchEvent event) {
//        TextureAtlas atlas = event.getAtlas();
//        if (atlas.location().equals(TextureAtlas.LOCATION_BLOCKS))
//            event.getAtlas().
//    }
}