package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import net.madmenyo.pixelwars.Assets;
import net.madmenyo.pixelwars.components.AssetInfoComponent;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.TextureComponent;

public class AssetAssignSystem extends IteratingSystem {

    private Assets assets;
    private TextureAtlas spriteSheet;

    public AssetAssignSystem(Assets assets) {
        super(Family.all(AssetInfoComponent.class).get());
        this.assets = assets;
        spriteSheet = assets.getAssetManager().get(Assets.SPRITE_ASSETS);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AssetInfoComponent as = Mapper.ASSET_COMP.get(entity);
        if (as.processed) return;

        TextureComponent tex = Mapper.TEX_COMP.get(entity);
        if (tex != null && tex.texture == null) {
            System.out.println(as.textureName + " is being assigned!");
            tex.texture = spriteSheet.findRegion(as.textureName);
        }

        as.processed = true;

    }
}
