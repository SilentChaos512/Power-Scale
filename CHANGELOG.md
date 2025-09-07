# Power Scale Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.21.1-0.3.0] - 2025-09-07
### Added
- Scaling attributes now have a `player_bonus_settings` field which can be used to set a starting value and min/max values
  - Max health has a unique override option in the config. If set to anything other than zero, it overrides the scaling attribute's starting value for player max health. Any other changes will require a data pack.
- Options to display a mob's level from a distance. By default, levels are displayed only when looking through a spyglass.
  - `powerscale:power_level_detectors` item tag is used to determine which items detect levels
  - The server config contains options to change whether an item is required, whether the item must be used or not, or you can disable this feature completely.
### Fixed
- Some mobs not received a level and attribute bonuses. This should be fixed for most if not all mobs now. [#8]
- Alchemy Set now has a completed (albeit basic) model

## [1.21.1-0.2.5] - 2025-03-22
### Added
- Config options for tonic boost amounts
- Config options to turn off crystal drops from blights and bosses
### Changed
- Mobs are now determined to be bosses if they are in the `c:bosses` tag

## [1.21.1-0.2.4] - 2025-03-09
### Changed
- EvalEx library is now shadowed to a new package, which may help prevent mod conflicts

## [1.12.1-0.2.3] - 2025-02-23
### Added
- Scaling attributes now have separate expressions for determining the attribute bonuses of blights
- Two new tags and config options related to blights
  - `powerscale:blights/make_giant` - Causes the mob to double in size when becoming a blight, if the relevant config option is enabled.
  - `powerscale:blights/strike_with_lightning` - Simulates the mob being struck by lightning when becoming a blight, if the relevant config option is enabled. Creepers are in this tag, but there is also a separate config option that will strike all creepers (including modded ones, theoretically) with lightning.

## [1.21.1-0.2.2] - 2025-02-06
### Added
- Difficulty meter item
  - Detects and displays difficulty in the form of an animated texture and a message
  - Use while crouching to change between local and player difficulty detecting modes.
  - Use without crouching to display difficulty as a message in the action bar. By default, this displays a percentage rounded down to the nearest 5%. A config option can change it to display an exact value instead.
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