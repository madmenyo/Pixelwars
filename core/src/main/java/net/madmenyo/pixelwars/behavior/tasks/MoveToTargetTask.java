package net.madmenyo.pixelwars.behavior.tasks;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import net.madmenyo.pixelwars.components.*;


public class MoveToTargetTask extends LeafTask<Entity> {

    Vector2 targetPosition = new Vector2();

    // This needs to be variable, attacking control point vs player vs pickup vs fleeing, etc
    private float withinRange = 256;

    @Override
    public Status execute() {
        TransformComponent trans = Mapper.TRANS_COMP.get(getObject());
        AiComponent aiComponent = Mapper.AI_COMP.get(getObject());
        VelocityComponent vel = Mapper.VEL_COMP.get(getObject());
        PawnComponent pawn = Mapper.PAWN_COMP.get(getObject());

        if (aiComponent.target == null) return Status.FAILED;

        vel.velocity.set(targetPosition).sub(trans.position).nor().scl(pawn.speed);
        trans.angle = vel.velocity.angleDeg();

        targetPosition.set(Mapper.TRANS_COMP.get(aiComponent.target).position);

        if (trans.position.dst(targetPosition) <= withinRange){
            System.out.println(trans.position.dst(targetPosition));
            System.out.println("Target reached");
            vel.velocity.set(0, 0);
            return Status.SUCCEEDED;
        }

        return Status.RUNNING;
    }

    @Override
    protected Task copyTo(Task task) {
        return task;
    }
}
