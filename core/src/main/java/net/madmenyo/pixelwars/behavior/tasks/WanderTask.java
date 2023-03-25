package net.madmenyo.pixelwars.behavior.tasks;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import net.madmenyo.pixelwars.components.AiComponent;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.TransformComponent;
import net.madmenyo.pixelwars.components.VelocityComponent;

public class WanderTask extends LeafTask<Entity> {

    private Vector2 basePoint = new Vector2();

    private Vector2 desiredDirection = new Vector2();

    private float rotationSpeed = 2;

    private final float wonderRadius = 75;

    @Override
    public void start() {
        System.out.println("Wander task started!");
        // Set initial position as basepoint.
        TransformComponent transform = Mapper.TRANS_COMP.get(getObject());
        basePoint.set(transform.position);
        transform.position.set(64, 0);
        VelocityComponent vel = Mapper.VEL_COMP.get(getObject());
        vel.velocity.set(.2f, -1).nor().scl(50);
        super.start();
    }

    @Override
    public Status execute() {
        System.out.println("Wandering...");
        AiComponent aiComponent = Mapper.AI_COMP.get(getObject());
        TransformComponent transform = Mapper.TRANS_COMP.get(getObject());
        VelocityComponent vel = Mapper.VEL_COMP.get(getObject());

        float dst = transform.position.dst(basePoint);

        float delta = wonderRadius - dst;

        if (dst > wonderRadius) turnToBasePoint(transform, vel);

        transform.angle = vel.velocity.angleDeg();


        return Status.RUNNING;
    }

    private void turnToBasePoint(TransformComponent trans, VelocityComponent vel) {
        desiredDirection.set(basePoint).sub(trans.position);

        float angleDifference = desiredDirection.angleDeg() - basePoint.angleDeg();

        float dotSide = vel.velocity.dot(basePoint);
        if (dotSide <= 0){
            vel.velocity.rotateDeg(-2);
        } else {
            vel.velocity.rotateDeg(2);
        }
        System.out.println(angleDifference);
    }

    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        WanderTask wanderTask = (WanderTask) task;
        wanderTask.basePoint.set(0, 0);
        return task;
    }
}
