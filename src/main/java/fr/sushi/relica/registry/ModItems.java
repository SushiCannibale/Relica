package fr.sushi.relica.registry;

import fr.sushi.relica.Relica;
import fr.sushi.relica.item.ItemScroll;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Relica.MODID);

    public static final RegistryObject<Item> SCROLL = register("scroll", () -> new ItemScroll(new Item.Properties()));

//    public static final RegistryObject<Item> CREATIVE_SCROLL = register("creative_scroll", () ->
//            new ItemScroll(new Item.Properties()
//                    .rarity(Rarity.EPIC)));



    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> sup) {
        return ITEMS.register(name, sup);
    }
}
