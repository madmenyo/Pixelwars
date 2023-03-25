package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

public class TextureComponent  implements Component, Pool.Poolable {
    public TextureRegion texture;
    public float alpha = 1;

    @Override
    public void reset() {
        texture = null;
        alpha = 1;
    }
}