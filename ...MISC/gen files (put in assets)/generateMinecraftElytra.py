import json
import os

output_folder = "minecraft/models/item"
models_folder = "wingplates/models/item"

def getArmorTypeFloat(armortype):
    if armortype == "diamond":
        return "5"
    if armortype == "netherite":
        return "6"

def getPatternFloat(pattern):
    if pattern == "bolt":
        return "01"
    if pattern == "coast":
        return "02"
    if pattern == "dune":
        return "03"
    if pattern == "eye":
        return "04"
    if pattern == "flow":
        return "05"
    if pattern == "host":
        return "06"
    if pattern == "raiser":
        return "07"
    if pattern == "rib":
        return "08"
    if pattern == "sentry":
        return "09"
    if pattern == "shaper":
        return "10"
    if pattern == "silence":
        return "11"
    if pattern == "snout":
        return "12"
    if pattern == "spire":
        return "13"
    if pattern == "tide":
        return "14"
    if pattern == "vex":
        return "15"
    if pattern == "ward":
        return "16"
    if pattern == "wayfinder":
        return "17"
    if pattern == "wild":
        return "18"

def getMaterialFloat(material):
    if material == "amethyst":
        return "01"
    if material == "copper":
        return "02"
    if material == "diamond":
        return "03"
    if material == "emerald":
        return "04"
    if material == "gold":
        return "05"
    if material == "iron":
        return "06"
    if material == "lapis":
        return "07"
    if material == "netherite":
        return "08"
    if material == "quartz":
        return "09"
    if material == "redstone":
        return "10"

# Generate the main JSON data structure
data = {
    "parent": "minecraft:item/generated",
    "textures": {
        "layer0": f"minecraft:item/elytra"
    },
    "overrides": [
        {
            "predicate": {
                "broken": 1
            },
            "model": "item/broken_elytra"
        },
    ]
}

for filename in os.listdir(models_folder):
    if filename.startswith(f"elytra"):
        # Remove the file extension
        filename_clean = os.path.splitext(filename)[0]

        # Check for trim
        if len(filename_clean.split("_")) >= 4:
            # Construct wingplates predicate float
            armortype = filename_clean.split("_")[1]
            pattern = filename_clean.split("_")[2]
            material = filename_clean.split("_")[3]
                        
            armortypefloat = getArmorTypeFloat(armortype)
            patternfloat = getPatternFloat(pattern)
            materialfloat = getMaterialFloat(material)

            wingplatesstring = f"0.{armortypefloat}{patternfloat}{materialfloat}"
            wingplates = float(wingplatesstring)
        else:
            armortype = filename_clean.split("_")[1]
            armortypefloat = getArmorTypeFloat(armortype)
            wingplatesstring = f"0.{armortypefloat}"
            wingplates = float(wingplatesstring)

        print(wingplates)

        # Create the override entry
        override_entry = {
            "model": f"wingplates:item/{filename_clean}",
            "predicate": {
                "wingplates": wingplates
            }
        }

        # Append the override entry to the overrides list
        data["overrides"].append(override_entry)

        # Set name and path
        file_name = f"elytra.json"
        file_path = os.path.join(output_folder, file_name)
                        
        # Write data to JSON file
        try:
            with open(file_path, 'w') as json_file:
                json.dump(data, json_file, indent=2)
            print(f"Generated: {file_name}")
        except Exception as e:
            print(f"Error generating {file_name}: {e}")

# Completed
print()
print("JSON generation complete.")