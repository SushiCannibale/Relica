package fr.sushi.relica.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import fr.sushi.relica.Relica;
import fr.sushi.relica.block.entity.AltarBlockEntity;
import fr.sushi.relica.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.checkerframework.framework.qual.Unused;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InfusionRecipe implements Recipe<AltarBlockEntity> {
    private final ResourceLocation id;
    /* Ingredients list from JSON / network */
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int magic;
    private final int processTime;

    public InfusionRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, int magic, int processTime) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.magic = magic;
        this.processTime = processTime;
    }

    public int getProcessTime() {
        return this.processTime;
    }

    public int getMagicNeeded() {
        return this.magic;
    }

    @Override
    public boolean matches(AltarBlockEntity pBlockEntity, Level pLevel) {
        for (int i = 0; i < this.ingredients.size(); i++) {
            if (!this.ingredients.get(i).test(pBlockEntity.getItem(i)))
                return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(AltarBlockEntity pBlockEntity, RegistryAccess pRegistryAccess) {
//        ItemStackHandler container = pBlockEntity.getInventory();
//        for (int i = 0; i < container.getSlots(); i++) {
//            // TODO : shrink all the resources itemstacks
//        }

        return this.result.copy();
    }

    /* Not used */
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    /* Book display only */
    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return InfusionRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return InfusionRecipeType.INSTANCE;
    }

    public static class InfusionRecipeType implements RecipeType<InfusionRecipe> {
        public static final InfusionRecipeType INSTANCE = new InfusionRecipeType();
    }

    public static class InfusionRecipeSerializer implements RecipeSerializer<InfusionRecipe> {
        public static final InfusionRecipeSerializer INSTANCE = new InfusionRecipeSerializer();

        private static final ResourceLocation NAME = new ResourceLocation(Relica.MODID, "infusion");
        public static final int MAX_WIDTH = 3;
        public static final int MAX_HEIGHT = 3;

        @Override
        public InfusionRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Map<String, Ingredient> keys = keysFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "key"));
            String[] pattern = patternFromJson(GsonHelper.getAsJsonArray(pSerializedRecipe, "pattern"));
            NonNullList<Ingredient> inputs = dissolvePattern(pattern, keys, pattern[0].length(), pattern.length);
//            ItemStack result = itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
            Ingredient result = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
            int magic = GsonHelper.getAsInt(pSerializedRecipe, "magic");
            int processTime = GsonHelper.getAsInt(pSerializedRecipe, "process_time");

            boolean flag = Arrays.stream(result.getItems()).anyMatch(s -> s.getItem() instanceof BlockItem);

            return new InfusionRecipe(pRecipeId, inputs, result.getItems()[0], magic, processTime);
        }

        @Override
        public @Nullable InfusionRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int size = pBuffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
            for (int i = 0; i < size; i++) {
                ingredients.add(i, Ingredient.fromNetwork(pBuffer));
            }

            ItemStack result = pBuffer.readItem();
            int magic = pBuffer.readVarInt();
            int processTime = pBuffer.readVarInt();

            return new InfusionRecipe(pRecipeId, ingredients, result, magic, processTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, InfusionRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.ingredients.size());
            for (Ingredient ingredient : pRecipe.ingredients)
                ingredient.toNetwork(pBuffer);

            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.magic);
            pBuffer.writeVarInt(pRecipe.processTime);
        }


        /* -> #: minecraft:iron_ingot */
        private static Map<String, Ingredient> keysFromJson(JsonObject jsonArray) {
            Map<String, Ingredient> map = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : jsonArray.entrySet()) {
                String key = entry.getKey();
                if (key.length() != 1)
                    throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");

                if (" ".equals(key))
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

                map.put(key, Ingredient.fromJson(entry.getValue()));
            }

            map.put(" ", Ingredient.EMPTY);
            return map;
        }

        /* -> ["###", "LLL", ..]  */
        private static String[] patternFromJson(JsonArray pPatternArray) {
            String[] astring = new String[pPatternArray.size()];
            if (astring.length > MAX_HEIGHT) {
                throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
            } else if (astring.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            } else {
                for(int i = 0; i < astring.length; ++i) {
                    String s = GsonHelper.convertToString(pPatternArray.get(i), "pattern[" + i + "]");
                    if (s.length() > MAX_WIDTH) {
                        throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                    }

                    if (i > 0 && astring[0].length() != s.length()) {
                        throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                    }

                    astring[i] = s;
                }

                return astring;
            }
        }

        private static NonNullList<Ingredient> dissolvePattern(String[] pPattern, Map<String, Ingredient> pKeys, int pPatternWidth, int pPatternHeight) {
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(pPatternWidth * pPatternHeight, Ingredient.EMPTY);
            Set<String> set = Sets.newHashSet(pKeys.keySet());
            set.remove(" ");

            for(int i = 0; i < pPattern.length; ++i) {
                for(int j = 0; j < pPattern[i].length(); ++j) {
                    String s = pPattern[i].substring(j, j + 1);
                    Ingredient ingredient = pKeys.get(s);
                    if (ingredient == null) {
                        throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                    }

                    set.remove(s);
                    nonnulllist.set(j + pPatternWidth * i, ingredient);
                }
            }

            if (!set.isEmpty()) {
                throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
            } else {
                return nonnulllist;
            }
        }

        private static ItemStack itemStackFromJson(JsonObject pStackObject) {
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(pStackObject, true, true);
        }
    }
}
