package net.madmenyo.pixelwars;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    private AssetManager assetManager;

    public static final AssetDescriptor<TextureAtlas> SPRITE_ASSETS = new AssetDescriptor<TextureAtlas>("sprites/spritesheet.atlas", TextureAtlas.class);
    //public static final AssetDescriptor<Skin> SKIN = new AssetDescriptor<Skin>("gui/uiskin.json", Skin.class, new SkinLoader.SkinParameter("gui/uiskin.atlas"));

    public Assets() {
        assetManager = new AssetManager();
    }

    public void load(){
        assetManager.load(SPRITE_ASSETS);
        //assetManager.load(SKIN);

        assetManager.finishLoading();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
