package com.kai.wingplates;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class WingplatesConfig {

	public static String chestplateVisibility = "always";
	public static String elytraVisibility = "inFlight";
	public static boolean applyToNormalElytra = false;

	public static void loadConfig() {
		// Load config
		try {
			List<String> lines = Files.readAllLines(Paths.get("config/Wingplates.toml"));
			for (String line : lines) {
				if (line.startsWith("chestplateVisibility"))
					chestplateVisibility = line.split("=")[1].strip();
				if (line.startsWith("elytraVisibility"))
					elytraVisibility = line.split("=")[1].strip();
				if (line.startsWith("applyToNormalElytra"))
					applyToNormalElytra = Boolean.parseBoolean(line.split("=")[1].strip());
			}
		}
		catch (IOException e) {
			System.out.println("[Wingplates] Couldn't load config! Error: " + e.getMessage());
		}
		saveConfig();
	}

	public static void configCommand() {
		// Config command
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("Wingplates")
				.executes(context -> {
					context.getSource().sendFeedback(Text.of("\n[Wingplates] Chestplate settings:\n          Visibility: " + chestplateVisibility + " (Default: always)\n[Wingplates] Elytra settings:\n          Visibility: " + elytraVisibility + " (Default: inFlight)\n          Apply to normal elytra: " + (applyToNormalElytra ? "Enabled" : "Disabled") + " (Default: Disabled)\n"));
					return 1;
				})
				.then(ClientCommandManager.literal("ChestplateVisibility")
						.executes(context -> {
							context.getSource().sendFeedback(Text.of("\n[Wingplates] Chestplate settings:\n          Visibility: " + chestplateVisibility + " (Default: always)\n"));
							return 1;
						})
						.then(ClientCommandManager.literal("always")
								.executes(context -> {
									chestplateVisibility = "always";
									saveConfig();
									context.getSource().sendFeedback(Text.of("[Wingplates] Chestplate visibility set to: " + chestplateVisibility));
									return 1;
								})
						)
						.then(ClientCommandManager.literal("inFlight")
								.executes(context -> {
									chestplateVisibility = "inFlight";
									saveConfig();
									context.getSource().sendFeedback(Text.of("[Wingplates] Chestplate visibility set to: " + chestplateVisibility));
									return 1;
								})
						)
						.then(ClientCommandManager.literal("onGround")
								.executes(context -> {
									chestplateVisibility = "onGround";
									saveConfig();
									context.getSource().sendFeedback(Text.of("[Wingplates] Chestplate visibility set to: " + chestplateVisibility));
									return 1;
								})
						)
						.then(ClientCommandManager.literal("never")
								.executes(context -> {
									chestplateVisibility = "never";
									saveConfig();
									context.getSource().sendFeedback(Text.of("[Wingplates] Chestplate visibility set to: " + chestplateVisibility));
									return 1;
								})
						)
				)
				.then(ClientCommandManager.literal("ElytraVisibility")
						.executes(context -> {
							context.getSource().sendFeedback(Text.of("\n[Wingplates] Elytra settings:\n          Visibility: " + elytraVisibility + " (Default: inFlight)\n          Apply to normal elytra: " + (applyToNormalElytra ? "Enabled" : "Disabled") + " (Default: Disabled)\n"));
							return 1;
						})
						.then(ClientCommandManager.literal("always")
								.executes(context -> {
									elytraVisibility = "always";
									saveConfig();
									context.getSource().sendFeedback(Text.of("[Wingplates] Elytra visibility set to: " + elytraVisibility));
									return 1;
								})
						)
						.then(ClientCommandManager.literal("inFlight")
								.executes(context -> {
									elytraVisibility = "inFlight";
									saveConfig();
									context.getSource().sendFeedback(Text.of("[Wingplates] Elytra visibility set to: " + elytraVisibility));
									return 1;
								})
						)
						.then(ClientCommandManager.literal("onGround")
								.executes(context -> {
									elytraVisibility = "onGround";
									saveConfig();
									context.getSource().sendFeedback(Text.of("[Wingplates] Elytra visibility set to: " + elytraVisibility));
									return 1;
								})
						)
						.then(ClientCommandManager.literal("never")
								.executes(context -> {
									elytraVisibility = "never";
									saveConfig();
									context.getSource().sendFeedback(Text.of("[Wingplates] Elytra visibility set to: " + elytraVisibility));
									return 1;
								})
						)
						.then(ClientCommandManager.literal("ToggleForNormalElytra")
								.executes(context -> {
									applyToNormalElytra = !applyToNormalElytra;
									saveConfig();
									context.getSource().sendFeedback(Text.of("[Wingplates] " + (applyToNormalElytra ? "Enabled" : "Disabled") + " elytra visibility settings for normal elytra."));
									return 1;
								})
						)
				)
				.then(ClientCommandManager.literal("reset")
						.executes(context -> {
							chestplateVisibility = "always";
							elytraVisibility = "inFlight";
							applyToNormalElytra = false;
							saveConfig();
							context.getSource().sendFeedback(Text.of("\n[Wingplates] Everything reset to default!\n"));
							return 1;
						})
				)
		));
	}

	public static void saveConfig() {
		try {
			Files.createDirectories(Paths.get("config"));
			Files.write(Paths.get("config/Wingplates.toml"), ("chestplateVisibility = " + chestplateVisibility + "\nelytraVisibility = " + elytraVisibility + "\napplyToNormalElytra = " + applyToNormalElytra).getBytes());
		} catch (IOException e) {
			System.out.println("[Wingplates] Couldn't save config! Error: " + e.getMessage());
		}
	}

}