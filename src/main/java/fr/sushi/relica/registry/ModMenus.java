package fr.sushi.relica.registry;

import fr.sushi.relica.Relica;
import fr.sushi.relica.client.menu.AltarMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Relica.MODID);

    public static final RegistryObject<MenuType<AltarMenu>> ALTAR_MENU = register("altar_menu", () -> new MenuType<>(AltarMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static <M extends AbstractContainerMenu> RegistryObject<MenuType<M>> register(String name, Supplier<MenuType<M>> sup) {
        return MENU_TYPES.register(name, sup);
    }
}
