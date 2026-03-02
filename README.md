# VitalXP

VitalXP is a NeoForge mod that increases a player’s maximum health as they gain experience levels.

Each time a player reaches a configured level interval, they permanently gain +1 heart (2 health points). Health increases are applied through attribute modifiers and persist between sessions.

---

## Configuration

- **Base Vitality**  
  Starting health (in health points) for new players or after a reset.  
  `20 = 10 hearts`

- **Health Cap**  
  Maximum total health allowed. Set to `-1` to disable.

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

---

Built for NeoForge 1.21.1.

Inspired by the LevelHearts mod by FireController1847: https://modrinth.com/mod/levelhearts