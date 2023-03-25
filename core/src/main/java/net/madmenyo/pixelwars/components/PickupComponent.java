package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class PickupComponent implements Component, Pool.Poolable {

    public Entity item;

    @Override
    public void reset() {

    }
}
