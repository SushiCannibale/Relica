package fr.sushi.relica.event;

import fr.sushi.relica.Relica;
import fr.sushi.relica.registry.ModEntities;
import fr.sushi.relica.renderer.AltarRenderer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Relica.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void registerBlockRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModEntities.ALTAR.get(), AltarRenderer::new);
    }
}
