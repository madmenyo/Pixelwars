package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class TransformComponent implements Component, Pool.Poolable {
    public Vector2 position = new Vector2();
    public Vector2 scale = new Vector2(1, 1);
    public float angle = 0;

    public int zLevel = 10;



    @Override
    public void reset() {
        position.set(0, 0);
        scale.set(1, 1);
        angle = 0;
        zLevel = 10;

    }
}
