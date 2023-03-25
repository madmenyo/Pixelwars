package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.madmenyo.pixelwars.components.*;

public class HealthSystem extends IteratingSystem {

    private float fadeTime = 2;

    public HealthSystem() {
        super(Family.all(HealthComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent hp = Mapper.HEALTH_COMP.get(entity);
        PawnComponent pawn = Mapper.PAWN_COMP.get(entity);

        pawn.deathTimer += deltaTime;

        if (hp.health <= 0) {
            if (pawn.state != PawnComponent.State.Dead) {
                pawn.state = PawnComponent.State.Dead;
                pawn.deathTimer = 0;
            }
        } else {
            if (!pawn.state.equals(PawnComponent.State.Dead)){
                return;
            }
        }

        float t = pawn.deathTimer / fadeTime;

        TextureComponent texture = Mapper.TEX_COMP.get(entity);
        texture.alpha = 1 - t;

        if (t >= 1){
            TransformComponent trans = Mapper.TRANS_COMP.get(entity);
            trans.position.set(pawn.spawnPoint);
            texture.alpha = 1;
            hp.health = hp.maxHealth;;
            pawn.state = PawnComponent.State.Idle;
        }

    }
}
