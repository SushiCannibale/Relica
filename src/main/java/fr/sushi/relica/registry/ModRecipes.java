package fr.sushi.relica.registry;

import fr.sushi.relica.Relica;
import fr.sushi.relica.recipe.InfusionRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModRecipes {
//    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Relica.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Relica.MODID);

    public static final RegistryObject<RecipeSerializer<?>> INFUSION_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("infusion",
            () -> InfusionRecipe.InfusionRecipeSerializer.INSTANCE);


//    public static final RegistryObject<InfusionRecipe.InfusionRecipeType> INFUSION_RECIPE_TYPE = registerRecipeType("infusion",
//            InfusionRecipe.InfusionRecipeType::new);

//    public static <T extends RecipeType<?>> RegistryObject<T> registerRecipeType(String name, Supplier<T> recipeTypeSupplier) {
//        return RECIPE_TYPES.register(name, recipeTypeSupplier);
//    }
}
