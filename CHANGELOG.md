# Power Scale Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.21.1-0.1.4]
### Added
- Two config options to set a drop chance for crystals from hostile mobs, including a base chance and looting bonus. While this is far from a total substitute for editing the loot table with a data pack, it does allow for some quick and easy customization.
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