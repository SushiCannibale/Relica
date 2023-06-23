package fr.sushi.relica.registry;

import fr.sushi.relica.Relica;
import fr.sushi.relica.entity.tileentity.BlockEntityAltar;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Relica.MODID);

    public static final RegistryObject<BlockEntityType<BlockEntityAltar>> ALTAR = register("altar_entity",
            () -> BlockEntityType.Builder.of(BlockEntityAltar::new, ModBlocks.ALTAR.get()).build(null));

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, Supplier<? extends BlockEntityType<T>> sup) {
        return BLOCK_ENTITIES.register(name, sup);
    }
}
