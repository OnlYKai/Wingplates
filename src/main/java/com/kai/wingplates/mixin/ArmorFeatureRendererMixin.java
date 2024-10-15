package com.kai.wingplates.mixin;

import com.kai.wingplates.WingplatesConfig;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

// Render Chestplate when armored elytra is worn
@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
	@SuppressWarnings("resource")
	@ModifyVariable(method = "renderArmor", at = @At("STORE"), ordinal = 0)
	private ItemStack renderChestplate(ItemStack stack) {
		var player = MinecraftClient.getInstance().player;
		// Check if item is elytra and player "exists"
		if (!stack.isOf(Items.ELYTRA) || player == null)
			return stack;



		// Apply config
		if (WingplatesConfig.chestplateVisibility.equals("never")) {
			return stack;
		}

		if (WingplatesConfig.chestplateVisibility.equals("onGround"))
			if (player.isFallFlying())
				return stack;

		if (WingplatesConfig.chestplateVisibility.equals("inFlight"))
			if (!player.isFallFlying())
				return stack;



		// Get the saved chestplate ItemStack (id) as nbt, check if it exists
		NbtCompound chestplateData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("wingplates");
		if (chestplateData.isEmpty())
			return stack;

		// Add "components" to Nbt structure
		chestplateData.put("components", new NbtCompound());

		// Check if enchantment glint should be applied (enchantment_glint_override and existing enchants)
		Boolean glintOverride = stack.get(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE); // returns true,false or null
		if (glintOverride == null) {
			Set<Object2IntMap.Entry<RegistryEntry<Enchantment>>> enchantments = stack.getEnchantments().getEnchantmentEntries();
			if (!enchantments.isEmpty())
				chestplateData.getCompound("components").putBoolean("minecraft:enchantment_glint_override", true);
		}
		else
			chestplateData.getCompound("components").putBoolean("minecraft:enchantment_glint_override", glintOverride);

		// Check for ArmorTrim
		ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
		if (trimData != null) {
			chestplateData.getCompound("components").put("minecraft:trim", new NbtCompound());
			chestplateData.getCompound("components").getCompound("minecraft:trim").putString("material", trimData.getMaterial().getIdAsString());
			chestplateData.getCompound("components").getCompound("minecraft:trim").putString("pattern", trimData.getPattern().getIdAsString());
		}

		// Check final Nbt construct
		//System.out.println("Nbt: " + chestplateData);

		// Convert the Nbt data to an ItemStack, return it (or the default stack if it fails to convert)
		var armorItem = ItemStack.fromNbt(player.getRegistryManager(), chestplateData);
		return armorItem.orElse(stack);
	}
}