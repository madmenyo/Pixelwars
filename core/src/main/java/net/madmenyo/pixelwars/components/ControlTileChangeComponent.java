package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;

public class ControlTileChangeComponent implements Component, Pool.Poolable {
    public Color oldColor = new Color();
    public Color currentColor = new Color();

    public float timer = 0;
    public float fadeTime = .5f;

    @Override
    public void reset() {
        oldColor.set(Color.WHITE);
        currentColor.set(Color.WHITE);
        timer = 0;
        fadeTime = .5f;
    }
}
