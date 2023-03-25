package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class WeaponComponent implements Component, Pool.Poolable {
    public float rateOfFire = .1f;
    public float fireTime = .5f;

    public float spreadAngle = 1f;
    public float maxDistance = 256f;

    public String textureString;
    public int damage;

    @Override
    public void reset() {
        rateOfFire = .5f;
        fireTime = .5f;
        spreadAngle = 1f;
        maxDistance = 256f;
        damage = 10;
    }
}
