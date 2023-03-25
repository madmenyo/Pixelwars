package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class SplashComponent implements Component, Pool.Poolable {
    public float currentTime = 0;
    public float endTime = .2f;

    public float xScaleMultiplier = 1;
    public float yScaleMultiplier = 1;

    @Override
    public void reset() {
        currentTime = 0;
        xScaleMultiplier = 1;
        yScaleMultiplier = 1;
    }
}
