package fr.sushi.relica;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Relica.MODID)
public class Relica {
    public static final String MODID = "relica";

    public Relica() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        /*

          /data merge entity bbde30f4-58dc-466b-81ed-666b1eda57ac
          {Item:{
            id:"minecraft:filled_map", Count:1b, tag:{
                Decorations:
                    [{x:1193.0d, z:-2375.0d, id:"+", type:26b, rot:180.0d}]
                , map:1}
            }
          }

         */
    }
}
