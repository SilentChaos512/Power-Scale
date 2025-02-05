# Power Scale Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Difficulty meter item (hints at local difficulty, still needs some work)
### Fixed
- Players with boosted health healing when killing mobs

## [1.21.1-0.2.1] - 2025-01-31
Blights are still pending a lot of work... Please be patient with me :)
### Added
- Blight creepers will now be supercharged by default
- Player difficulty expression config (`difficulty.player_difficulty.expression`). This allows the difficulty value stored on a player to be modified before being used in local difficulty calculations. It does not change the difficulty value stored on the player. It makes no modifications to the returned value by default.
- `ARMOR_VALUE()` function, which returns the total armor value of a player. This can be used either in the new player difficulty expression, or the player difficulty mutator. It is not used by default.
### Fixed
- Scaling Attributes not syncing from server to client, causing a disconnect error when drinking tonics on dedicated servers

## [1.21.1-0.2.0] - 2025-01-22
### Added
- Blights can now spawn (WIP). They currently have no special attributes or bonuses, just the purple fire effect. But that will change.
### Fixed
- Blight fires rendering incorrectly

## [1.21.1-0.1.4] - 2024-12-22
### Added
- Two config options to set a **drop chance for crystals** from hostile mobs, including a base chance and looting bonus. While this is far from a total substitute for editing the loot table with a data pack, it does allow for some quick and easy customization.
- A config option to set a **cooldown time for crystal drops**. Whenever a crystal drops, a cooldown timer is attached to the player. Random crystal drops are blocked while the cooldown timer is active, but guaranteed drops (like those from bosses) are still possible.
- New `custom` list to `mob_scaling` object in scaling attributes, which allows a scaling expression to be set for mobs in a given tag. Only a single tag is accepted currently; no entity IDs. See the new "Example" data pack under `optional_data_packs` on GitHub for a full example file.
    ```
    "custom": [
    {
      "entity_tag": "minecraft:skeletons",
      "expression": "base_value * 0.5 * FLOOR((level - 26) / 25)"
      }
    ]
    ```

## [1.21.1-0.1.3] - 2024-12-15
### Added
- Code to render blight fires (blights are still not truly in the mod yet...)
- Players will be notified (by default) when their attributes change (this can be configured per mutator category)
- Flasks can now be filled from a cauldron, in addition to water source blocks (like bottles)
- Jade plugin for alchemy set
### Fixed
- Players not having attribute bonuses reapplied after dying
- Attribute mutator configs now function

## [1.21.1-0.1.2] - 2024-12-12
### Changed
- Alchemy set now requires a pickaxe to mine
### Fixed
- Alchemy Set not dropping itself when broken

## [1.21.1-0.1.1] - 2024-12-09
### Fixed
- Build error which causes a crash on launch [#1]

## [1.21.1-0.1.0] - 2024-12-07
- First release
### Added
- Difficulty system (incomplete, but functioning)
- Scaling Attributes system
- Alchemy Set with brews and tonics
- Commands