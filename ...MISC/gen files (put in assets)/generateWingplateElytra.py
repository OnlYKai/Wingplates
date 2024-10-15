import json
import os

output_folder = "wingplates/models/item"
textures_folder = "wingplates/textures/trims/items"

armortypes = [
    "diamond",
    "netherite"
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

    # Generate Wingplate without trim
    data = {
        "parent": "minecraft:item/generated",
        "textures": {
            "layer0": f"wingplates:item/elytra",
            "layer1": f"wingplates:item/{armortype}_chestplate"
        }
    }
    # Set name and path
    file_name = f"elytra_{armortype}.json"
    file_path = os.path.join(output_folder, file_name)
    # Write data to JSON file
    with open(file_path, 'w') as json_file:
        json.dump(data, json_file, indent=2)
    print(f"Generated {file_name}")
    print()

    for filename in os.listdir(textures_folder):
        if not filename.startswith("elytra"):
            continue
        for material in materials:
            if armortype == material:
                material = f"{material}_darker"
            if armortype == "golden":
                if material == "gold":
                    material = f"{material}_darker"
            armorpiece_pattern = os.path.splitext(filename)[0]
            pattern = armorpiece_pattern.split("_")[1]
            print(armortype)
            print(pattern)
            print(material)
            print()

            # Generate JSON data
            data = {
                "parent": "minecraft:item/generated",
                "textures": {
                    "layer0": f"wingplates:item/elytra",
                    "layer1": f"wingplates:item/{armortype}_chestplate",
                    "layer2": f"wingplates:trims/items/{armorpiece_pattern}_{material}"
                }
            }
            
            # Set name and path
            file_name = f"elytra_{armortype}_{pattern}_{material}.json"
            file_path = os.path.join(output_folder, file_name)
            
            # Write data to JSON file
            with open(file_path, 'w') as json_file:
                json.dump(data, json_file, indent=2)

            print(f"Generated {file_name}")
            print()
            
# Completed
print()
print("JSON generation complete.")