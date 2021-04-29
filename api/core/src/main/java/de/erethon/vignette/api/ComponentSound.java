package de.erethon.vignette.api;

/**
 * @author Fyreum
 */
public class ComponentSound {

    private String sound;
    private float volume;
    private float pitch;

    public ComponentSound(String sound) {
        this(sound, 1f);
    }

    public ComponentSound(String sound, float volume) {
        this(sound, volume, 1f);
    }

    public ComponentSound(String sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /* getter and setter */

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

}
