package com.github.naterepos.forgebookshelf.utility;

import com.github.naterepos.forgebookshelf.Text;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Optional;

public class ItemUtilities {

    public static ItemStack withLore(ItemStack stack, List<String> lore) {
        CompoundNBT compound = new CompoundNBT();
        ListNBT loreNBT = new ListNBT();

        for (String line : lore) {
            loreNBT.add(Text.nbt(line));
        }

        compound.put("lore", loreNBT);
        CompoundNBT tag = stack.getOrCreateTag();
        tag.put("display", compound);
        stack.setTag(tag);

        return stack;
    }

    public static ItemStack withCustomTag(ItemStack stack, String tag, String value) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putString(tag, value);
        stack.setTag(nbt);
        return stack;
    }

    public static Optional<String> getTag(ItemStack stack, String customTag) {
        CompoundNBT tag = stack.getOrCreateTag();
        if(tag.contains(customTag)) {
            return Optional.of(tag.getString(customTag));
        }
        return Optional.empty();
    }

    public static Item getItem(String id) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
    }

    public static @NonNull ItemStack deserialize(String data) {
        try {
            return ItemStack.read(JsonToNBT.getTagFromJson(data));
        } catch (CommandSyntaxException ignored) {}
        return ItemStack.EMPTY;
    }

    public static String serialize(ItemStack stack) {
       return stack.serializeNBT().toString();
    }
}
