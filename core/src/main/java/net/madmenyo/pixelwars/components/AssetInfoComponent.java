package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class AssetInfoComponent implements Component, Pool.Poolable {
    /** The name of the region to lookup. **/
    public String textureName;

    /** If the asset info is already processed **/
    public boolean processed = false;

    @Override
    public void reset() {
        textureName = "";
        processed = false;
    }
}
