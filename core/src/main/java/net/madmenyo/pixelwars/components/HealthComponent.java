package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class HealthComponent implements Component, Pool.Poolable {
    public int maxHealth = 150;
    public int health = 150;

    @Override
    public void reset() {
        maxHealth = 150;
        health = 150;
    }
}
