package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class BulletComponent implements Component, Pool.Poolable {

    public Vector2 startPosition = new Vector2(0,0);
    public Vector2 direction = new Vector2(0, 1);
    public float lifeTime = 0;
    public float airTime = .6f;

    public float startSpeed = 1200;
    public float endSpeed = 300;

    public float maxDistance = 300;

    public float damage = 10;

    public Entity sourcePawn;

    @Override
    public void reset() {
        direction.set(0,1);
        maxDistance = 300;

        lifeTime = 0;
        damage = 10;

    }
}
