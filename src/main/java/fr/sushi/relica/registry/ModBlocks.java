package fr.sushi.relica.registry;

import fr.sushi.relica.Relica;
import fr.sushi.relica.block.AltarBlock;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Relica.MODID);

    public static final RegistryObject<AltarBlock> ALTAR = register("altar", () -> new AltarBlock(BlockBehaviour.Properties.of(Material.STONE)
            .strength(5.0f)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
            .noOcclusion()));

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> sup) {
        RegistryObject<T> block = BLOCKS.register(name, sup);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}
