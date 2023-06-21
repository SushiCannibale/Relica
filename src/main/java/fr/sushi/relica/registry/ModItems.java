package fr.sushi.relica.registry;

import fr.sushi.relica.Relica;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Relica.MODID);

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<T> sup) {
        return ITEMS.register(name, sup);
    }
}
