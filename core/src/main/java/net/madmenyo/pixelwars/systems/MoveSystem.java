package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import net.madmenyo.pixelwars.components.CircleCollisionComponent;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.TransformComponent;
import net.madmenyo.pixelwars.components.VelocityComponent;

public class MoveSystem extends IteratingSystem {

    private Vector2 tmpV2 = new Vector2();

    public MoveSystem() {
        super(Family.all(VelocityComponent.class, TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent tr = Mapper.TRANS_COMP.get(entity);
        VelocityComponent vel = Mapper.VEL_COMP.get(entity);

        tmpV2.set(vel.velocity).scl(deltaTime);
        tr.position.add(tmpV2);

        CircleCollisionComponent col = Mapper.CIRCLE_COMP.get(entity);
        if (col != null){
            col.circle.setPosition(tr.position);
        }
    }
}
