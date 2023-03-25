package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class ControlTileTurretComponent implements Component, Pool.Poolable {
    public boolean hidden = true;
    public float time = 0;

    public Entity weapon;

    @Override
    public void reset() {
        hidden = true;
    }
}
