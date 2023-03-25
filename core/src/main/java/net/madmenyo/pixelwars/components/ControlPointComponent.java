package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;


public class ControlPointComponent implements Component, Pool.Poolable {
    public enum Status{
        Free, // No owners
        Owned, // Owned and protected
        Contested, // Owned and contested by other player
        Transition // In transition to new owner
    }

    public Status status = Status.Free;

    public Array<Entity> controlTiles = new Array<>();
    public Entity turretEntity;

    public int x, y;

    public final float transitionDelay = 5;
    public float transitionTimer = 0;


    @Override
    public void reset() {
        controlTiles.clear();
        status = Status.Free;

        x = 0;
        y = 0;

        transitionTimer = 0;

        turretEntity = null;
    }
}
