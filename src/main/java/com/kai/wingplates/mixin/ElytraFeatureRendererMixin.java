package com.kai.wingplates.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// Only render elytra when flying  and make it stay visible after landing for X milliseconds
@Mixin(ElytraFeatureRenderer.class)
public class ElytraFeatureRendererMixin {
	long lastFlyingTime;
	long sinceLastFlight;
	@SuppressWarnings("resource")
	@ModifyVariable(method = "render", at = @At("STORE"), ordinal = 0)
	private ItemStack renderElytra(ItemStack stack) {
		var player = MinecraftClient.getInstance().player;
		// Check if item is elytra and player "exists"
		if (!stack.isOf(Items.ELYTRA) || player == null)
			return stack;

		// Get the saved chestplate ItemStack (id) as nbt, check if it exists
		NbtCompound chestplateData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("wingplates");
		if (chestplateData.isEmpty())
			return stack;

		// Check if player has the "FallFlying" tag
		if (player.isFallFlying()) {
			lastFlyingTime = System.currentTimeMillis();
			return stack;
		}

		// Calculate difference between current time and lastFlyingTime in milliseconds
		sinceLastFlight = System.currentTimeMillis() - lastFlyingTime;

		// If the difference is bigger than X milliseconds stop rendering elytra
		if (sinceLastFlight >= 100)
			return ItemStack.EMPTY;

		// This is returned when player is not flying and difference is lower than X milliseconds
		return stack;
	}
}