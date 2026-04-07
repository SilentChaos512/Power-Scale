package net.silentchaos512.powerscale.api.event;

import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.Event;

/**
 * Fired on {@link net.neoforged.neoforge.common.NeoForge#EVENT_BUS} when a mob's difficulty level is being determined.
 * This event allows other mods to change the difficulty and level of a mob. Note that you may only set the difficulty
 * value; the mob's level is their difficulty rounded down plus one.
 */
public class SetMobDifficultyEvent extends Event {
    private final Mob mob;
    private final double originalDifficulty;
    private double newDifficulty;

    public SetMobDifficultyEvent(Mob mob, double originalDifficulty) {
        this.mob = mob;
        this.originalDifficulty = this.newDifficulty = originalDifficulty;
    }

    /**
     * @return The mob having their difficulty and level assigned
     */
    public Mob getMob() {
        return this.mob;
    }

    /**
     * @return The original and unmodified difficulty value that was calculated before the event was fired.
     */
    public double getOriginalDifficulty() {
        return this.originalDifficulty;
    }

    /**
     * @return The current and possibly modified difficulty value. This value is used to assign the mob their difficulty
     * and level after all event handlers have processed the event.
     */
    public double getNewDifficulty() {
        return this.newDifficulty;
    }

    /**
     * Sets the new difficulty value. Depending on what you are aiming to do, you may wish to calculate your new value
     * based on either the original or new difficulty value.
     *
     * @param difficulty The new difficulty value
     */
    public void setNewDifficulty(double difficulty) {
        this.newDifficulty = difficulty;
    }

    /**
     * @return The new power level for the mob, which is simply difficulty rounded down plus one
     */
    public int getNewPowerLevel() {
        return (int) this.newDifficulty + 1;
    }
}
