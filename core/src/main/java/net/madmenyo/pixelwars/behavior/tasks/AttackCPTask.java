package net.madmenyo.pixelwars.behavior.tasks;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.madmenyo.pixelwars.components.*;
import net.madmenyo.pixelwars.world.EntityFactory;
import net.madmenyo.pixelwars.world.WorldUtils;

public class AttackCPTask extends LeafTask<Entity> {

    float timer = 0;
    Vector2 direction = new Vector2();
    private Vector2 offsetTarget = new Vector2();

    @Override
    public void start() {
        super.start();
        System.out.println("Start attack CP");
    }

    @Override
    public Status execute() {
        AiComponent ai = Mapper.AI_COMP.get(getObject());
        TransformComponent trans = Mapper.TRANS_COMP.get(getObject());
        TeamComponent team = Mapper.TEAM_COMP.get(getObject());

        Gdx.graphics.getDeltaTime();

        ControlPointComponent cp = Mapper.CTRL_POINT_COMP.get(ai.target);
        if (cp == null) return Status.FAILED;


        // get closest control tile
        Entity closest = WorldUtils.getClosestUnownedEntity(trans.position, team, cp.controlTiles);

        // if not within range, walk to it

        if (closest == null){
            System.out.println("All tiles probably taken, wait for transition");

            TeamComponent cpOwner = Mapper.TEAM_COMP.get(ai.target);
            if (cpOwner.equals(team)){
                System.out.println("Successfully taken CP!");
                return Status.SUCCEEDED;
            }

            return Status.RUNNING;
        }

        // This should go in it's own task?
        timer -= Gdx.graphics.getDeltaTime();
        if (timer <= 0) {
            timer += 1;
            // if within range shoot it
            TransformComponent targetTrans = Mapper.TRANS_COMP.get(closest);
            PawnComponent pawn = Mapper.PAWN_COMP.get(getObject());
            WeaponComponent weapon = Mapper.WEAPON_COMP.get(pawn.weapon);


            direction.set(targetTrans.position).sub(trans.position).nor();
            direction.rotateDeg(MathUtils.random(-1f, 1f) * weapon.spreadAngle);
            Entity bullet = EntityFactory.CreateBullet(ai.engine, trans.position, direction, team,trans.position.dst(targetTrans.position), weapon);
            ai.engine.addEntity(bullet);
        }





        return Status.RUNNING;
    }

    @Override
    protected Task copyTo(Task task) {
        return task;
    }
}