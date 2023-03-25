package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Pool;

public class CircleCollisionComponent implements Component, Pool.Poolable {

    public Circle circle = new Circle();

    @Override
    public void reset() {
        circle.set(0, 0, 0);
    }
}
