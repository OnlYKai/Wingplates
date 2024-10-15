package com.kai.wingplates;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

import java.util.Map;

import static java.util.Map.entry;

public class WingplatesClient implements ClientModInitializer {

	public static final Map<String, Float> TRIM_MATERIALS = Map.of(
			"minecraft:amethyst", 0.00001f,
			"minecraft:copper", 0.00002f,
			"minecraft:diamond", 0.00003f,
			"minecraft:emerald", 0.00004f,
			"minecraft:gold", 0.00005f,
			"minecraft:iron", 0.00006f,
			"minecraft:lapis", 0.00007f,
			"minecraft:netherite", 0.00008f,
			"minecraft:quartz", 0.00009f,
			"minecraft:redstone", 0.00010f);

	public static final Map<String, Float> TRIM_PATTERNS = Map.ofEntries(
			entry("minecraft:bolt", 0.001f),
			entry("minecraft:coast", 0.002f),
			entry("minecraft:dune", 0.003f),
			entry("minecraft:eye", 0.004f),
			entry("minecraft:flow", 0.005f),
			entry("minecraft:host", 0.006f),
			entry("minecraft:raiser", 0.007f),
			entry("minecraft:rib", 0.008f),
			entry("minecraft:sentry", 0.009f),
			entry("minecraft:shaper", 0.010f),
			entry("minecraft:silence", 0.011f),
			entry("minecraft:snout", 0.012f),
			entry("minecraft:spire", 0.013f),
			entry("minecraft:tide", 0.014f),
			entry("minecraft:vex", 0.015f),
			entry("minecraft:ward", 0.016f),
			entry("minecraft:wayfinder", 0.017f),
			entry("minecraft:wild", 0.018f));

	public static final Map<String, Float> MATERIALS = Map.of(
			"minecraft:leather_chestplate",0.1f,
			"minecraft:chainmail_chestplate", 0.2f,
			"minecraft:iron_chestplate", 0.3f,
			"minecraft:golden_chestplate", 0.4f,
			"minecraft:diamond_chestplate", 0.5f,
			"minecraft:netherite_chestplate", 0.6f);

	public static float getTrimMaterialValue(String trimMaterialData) {
		return TRIM_MATERIALS.getOrDefault(trimMaterialData, 0.0f);
	}

	public static float getTrimPatternValue(String trimPatternData) {
		return TRIM_PATTERNS.getOrDefault(trimPatternData, 0.0f);
	}

	public static float getMaterialValue(String chestplateData) {
		return MATERIALS.getOrDefault(chestplateData, 0.0f);
	}

	@Override
	public void onInitializeClient() {
		WingplatesConfig.loadConfig();
		WingplatesConfig.configCommand();

		ModelPredicateProviderRegistry.register(Items.ELYTRA, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			//System.out.println("is running elytra");
			// Get chestplate id stored in custom_data, check if it exists, turn it into a String
			NbtElement chestplateCheckData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("wingplates").get("id");
			if (chestplateCheckData == null)
				return 0.0f;
			String chestplateData = chestplateCheckData.toString().replaceAll("\"","");

			// Get trim data, check if it exists, turn it into a String (or return chestplate id alone if no trim)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return getMaterialValue(chestplateData);
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// Check returned float value
			//float total = Math.round((getMaterialValue(chestplateData) + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
			//System.out.println("Val: " + total);

			// return chestplate id and trim data
			return Math.round((getMaterialValue(chestplateData) + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		// Netherite chestplate with Wingplates support
		ModelPredicateProviderRegistry.register(Items.NETHERITE_CHESTPLATE, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get chestplate id stored in custom_data, check if it exists
			NbtCompound chestplateCheckData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("wingplates");
			if (chestplateCheckData.isEmpty()) {
				// Get trim data, check if it exists, turn it into a String (or return custom data doesn't exist)
				ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
				if (trimData == null)
					return 0.0f;
				String trimPatternData = trimData.getPattern().getIdAsString();
				String trimMaterialData = trimData.getMaterial().getIdAsString();

				// return 0.2f to indicate no custom_data exists, and trim data
				return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
			}

			// Get trim data, check if it exists, turn it into a String (or return custom data exists)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				// return 0.1f to indicate custom_data exists
				return 0.1f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.1f to indicate custom_data exists, and trim data
			return Math.round((0.1f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		// Diamond chestplate with Wingplates support
		ModelPredicateProviderRegistry.register(Items.DIAMOND_CHESTPLATE, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get chestplate id stored in custom_data, check if it exists
			NbtCompound chestplateCheckData = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getCompound("wingplates");
			if (chestplateCheckData.isEmpty()) {
				// Get trim data, check if it exists, turn it into a String (or return custom data doesn't exist)
				ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
				if (trimData == null)
					return 0.0f;
				String trimPatternData = trimData.getPattern().getIdAsString();
				String trimMaterialData = trimData.getMaterial().getIdAsString();

				// return 0.2f to indicate no custom_data exists, and trim data
				return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
			}

			// Get trim data, check if it exists, turn it into a String (or return custom data exists)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				// return 0.1f to indicate custom_data exists
				return 0.1f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.1f to indicate custom_data exists, and trim data
			return Math.round((0.1f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.GOLDEN_CHESTPLATE, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.IRON_CHESTPLATE, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.CHAINMAIL_CHESTPLATE, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.LEATHER_CHESTPLATE, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.NETHERITE_HELMET, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.NETHERITE_LEGGINGS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.NETHERITE_BOOTS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.DIAMOND_HELMET, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.DIAMOND_LEGGINGS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.DIAMOND_BOOTS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.GOLDEN_HELMET, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.GOLDEN_LEGGINGS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.GOLDEN_BOOTS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.IRON_HELMET, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.IRON_LEGGINGS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.IRON_BOOTS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.CHAINMAIL_HELMET, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.CHAINMAIL_LEGGINGS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.CHAINMAIL_BOOTS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.LEATHER_HELMET, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.LEATHER_LEGGINGS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});

		ModelPredicateProviderRegistry.register(Items.LEATHER_BOOTS, Identifier.of("wingplates"), (stack, world, ent, i) -> {
			// Get trim data, check if it exists, turn it into a String (or return default)
			ArmorTrim trimData = stack.get(DataComponentTypes.TRIM);
			if (trimData == null)
				return 0.0f;
			String trimPatternData = trimData.getPattern().getIdAsString();
			String trimMaterialData = trimData.getMaterial().getIdAsString();

			// return 0.2f and trim data
			return Math.round((0.2f + getTrimPatternValue(trimPatternData) + getTrimMaterialValue(trimMaterialData)) * 100000f) / 100000f;
		});
	}
}