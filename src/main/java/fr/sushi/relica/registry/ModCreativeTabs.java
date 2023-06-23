package fr.sushi.relica.registry;


import fr.sushi.relica.Relica;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Relica.MODID)
public class ModCreativeTabs {
//    public static final CreativeModeTab TAB = CreativeModeTab.builder(0);

    public static CreativeModeTab TAB;

    @SubscribeEvent
    public static void register(CreativeModeTabEvent.Register event) {
        TAB = event.registerCreativeModeTab(new ResourceLocation(Relica.MODID, "relica_tab"), builder -> {
            builder.icon(() -> new ItemStack(Items.BLACK_CANDLE));
            builder.title(Component.translatable("creativemodetab.relica_tab"));
            builder.build();
        });
    }

    @SubscribeEvent
    public static void addItems(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == TAB) {
            /* Also register BlockItems */
            ModItems.ITEMS.getEntries().forEach(obj -> event.accept(obj.get()));
        }
    }
}
