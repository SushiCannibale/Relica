package fr.sushi.relica.registry;

import fr.sushi.relica.Relica;
import fr.sushi.relica.recipe.InfusionRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Relica.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Relica.MODID);


    public static final RegistryObject<RecipeType<InfusionRecipe>> INFUSION_RECIPE_TYPE = registerRecipeType("infusion");

    public static final RegistryObject<RecipeSerializer<?>> INFUSION_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("infusion",
            InfusionRecipe.InfusionRecipeSerializer::new);

    public static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerRecipeType(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<T>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }
}
