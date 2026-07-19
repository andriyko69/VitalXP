# VitalXP

VitalXP is a NeoForge mod that increases a player’s maximum health as they gain experience levels.

Each time a player reaches a configured level interval, they permanently gain +1 heart (2 health points). Health increases are applied through attribute modifiers and persist between sessions.

Progress is based on the highest XP level the player has reached. Spending XP through enchanting, anvils, commands, death, or another mod does not remove earned hearts, and regaining the same levels does not award them again. Large or multi-step XP grants are reconciled to the same final result.

---

## Configuration

- **Base Vitality**  
  Starting health (in health points) for new players or after a reset.  
  `20 = 10 hearts`

- **Health Cap**  
  Maximum health managed by VitalXP. Set to `-1` to disable. Health from other mods remains additive and can raise the final effective maximum above this cap.

- **Level Interval**  
  Gain 1 heart every X levels (1–255).

- **Play Upgrade Sound**  
  Plays a sound when a heart is gained.

- **Reset Progress on Death**  
  If enabled, earned hearts are reset on death.

- **Restore Health on Upgrade**  
  Heals the player when a new heart is unlocked.

- **Consume XP on Upgrade**  
  Removes experience levels when earning a heart.

- **XP Cost Per Upgrade**  
  Levels removed per heart gain.  
  `-1` removes all experience levels.

If several milestones are reached in one operation, VitalXP applies the final health value once, plays at most one upgrade sound, heals at most once, and consumes the combined level cost once. The progression checkpoint is saved before XP is consumed.

Changing the level interval does not retroactively grant or remove hearts. The new interval applies as the player exceeds their saved highest level. Milestones earned beyond the health cap remain saved and become visible if the cap is raised later.

Existing worlds are migrated from the exact legacy `vitalxp:bonus_health` modifier and the player's current XP level. Progress hidden above an old cap, or already erased by an older VitalXP login, cannot always be reconstructed exactly.

---

Built for NeoForge 1.21.1.

Inspired by the LevelHearts mod by FireController1847: https://modrinth.com/mod/levelhearts
