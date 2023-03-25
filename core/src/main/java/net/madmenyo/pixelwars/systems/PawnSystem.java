package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.PawnComponent;
import net.madmenyo.pixelwars.components.TextureComponent;

public class PawnSystem extends IteratingSystem {

    private TextureAtlas textureAtlas;

    public PawnSystem(TextureAtlas textureAtlas) {
        super(Family.all(PawnComponent.class).get());

        this.textureAtlas = textureAtlas;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //Set correct region
        TextureComponent tex = Mapper.TEX_COMP.get(entity);
        if (tex == null) throw new NullPointerException("Pawn does not have a TextureComponent!");

        PawnComponent pawn = Mapper.PAWN_COMP.get(entity);

        switch (pawn.state){
            case Idle:
                break;
            case Walking:
                break;
            case Running:
                break;
            case Shooting:
                break;
        }

        // For now, just give him a gun image
        tex.texture = textureAtlas.findRegion(pawn.textureName + "_gun");
    }
}
