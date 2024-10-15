import json
import os

output_folder = "wingplates/models/item"
textures_folder = "wingplates/textures/trims/items"

armortypes = [
    "leather",
    "chainmail",
    "iron",
    "golden",
    "diamond",
    "netherite",
    "turtle"
]

materials = [
    "amethyst",
    "copper",
    "diamond",
    "emerald",
    "gold",
    "iron",
    "lapis",
    "netherite",
    "quartz",
    "redstone"
]

for armortype in armortypes:
    if armortype == "leather":
        for filename in os.listdir(textures_folder):
            if filename.startswith("elytra"):
                continue
            for material in materials:
                # Remove file extension
                armorpiece_pattern = os.path.splitext(filename)[0]
                # Split at "_" and take first part
                armorpiece = armorpiece_pattern.split("_")[0]
                print(armortype)
                print(armorpiece)
                print(armorpiece_pattern)
                print(material)
                print()

                # Generate JSON data
                data = {
                    "parent": "minecraft:item/generated",
                    "textures": {
                        "layer0": f"minecraft:item/{armortype}_{armorpiece}",
                        "layer1": f"minecraft:item/{armortype}_{armorpiece}_overlay",
                        "layer2": f"wingplates:trims/items/{armorpiece_pattern}_{material}"
                    }
                }
                
                # Set name and path
                file_name = f"{armortype}_{armorpiece_pattern}_{material}.json"
                file_path = os.path.join(output_folder, file_name)
                
                # Write data to JSON file
                with open(file_path, 'w') as json_file:
                    json.dump(data, json_file, indent=2)

                print(f"Generated {file_name}")
                print()
    else:
        for filename in os.listdir(textures_folder):
            if armortype == "turtle":
                if not filename.startswith("helmet"):
                    continue
            if filename.startswith("elytra"):
                continue
            for material in materials:
                if armortype == material:
                    material = f"{material}_darker"
                if armortype == "golden":
                    if material == "gold":
                        material = f"{material}_darker"
                # Remove file extension
                armorpiece_pattern = os.path.splitext(filename)[0]
                # Split at "_" and take first part
                armorpiece = armorpiece_pattern.split("_")[0]
                print(armortype)
                print(armorpiece)
                print(armorpiece_pattern)
                print(material)
                print()

                # Generate JSON data
                data = {
                    "parent": "minecraft:item/generated",
                    "textures": {
                        "layer0": f"minecraft:item/{armortype}_{armorpiece}",
                        "layer1": f"wingplates:trims/items/{armorpiece_pattern}_{material}"
                    }
                }
                
                # Set name and path
                file_name = f"{armortype}_{armorpiece_pattern}_{material}.json"
                file_path = os.path.join(output_folder, file_name)
                
                # Write data to JSON file
                with open(file_path, 'w') as json_file:
                    json.dump(data, json_file, indent=2)

                print(f"Generated {file_name}")
                print()

# Completed
print()
print("JSON generation complete.")