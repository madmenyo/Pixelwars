package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.madmenyo.pixelwars.components.*;
import net.madmenyo.pixelwars.world.EntityFactory;
import net.madmenyo.pixelwars.world.WorldUtils;

public class TurretSystem extends IteratingSystem {

    private Array<Entity> pawns;

    private Vector2 desiredDirection= new Vector2();
    private Vector2 direction = new Vector2();

    private float fireCooldown = .8f;

    public TurretSystem(Array<Entity> pawns) {
        super(Family.all(ControlTileTurretComponent.class).get());
        this.pawns = pawns;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TeamComponent team = Mapper.TEAM_COMP.get(entity);
        TransformComponent trans = Mapper.TRANS_COMP.get(entity);
        ControlTileTurretComponent turret = Mapper.CTRL_TURRET_COMP.get(entity);
        WeaponComponent weaponComponent = Mapper.WEAPON_COMP.get(turret.weapon);
        weaponComponent.fireTime += deltaTime;

        // standard rotation
        float rotation = 45;

        if (!team.teamColor.equals(Color.WHITE)) {

            Entity closestPawn = WorldUtils.getClosestUnownedEntity(trans.position, team, pawns);
            if (closestPawn == null){
                throw new NullPointerException("Turret cannot find any pawns in the playfield, why should it exist anyway?");
            }
            Vector2 pawnPos = Mapper.TRANS_COMP.get(closestPawn).position;

            desiredDirection.set(pawnPos).sub(trans.position).nor();
            direction.set(1, 0);
            direction.setAngleDeg(trans.angle);

            if (trans.position.dst(pawnPos) < weaponComponent.maxDistance) {
                // rotate towards target

                rotation = -WorldUtils.getClosestRotationDirection(desiredDirection, direction);
                rotation *= 75;
            }

            desiredDirection.set(pawnPos).sub(trans.position).nor();
            // super weird I need to add 90 degrees to it
            direction.setAngleDeg(trans.angle + 90);

            if (Math.abs(desiredDirection.angleDeg() - direction.angleDeg()) < 3 && trans.position.dst(pawnPos) < weaponComponent.maxDistance){

                    if (weaponComponent.fireTime >= weaponComponent.rateOfFire) {
                    Entity bullet = EntityFactory.CreateBullet(getEngine(), trans.position, desiredDirection.rotateDeg(MathUtils.random(-weaponComponent.spreadAngle, weaponComponent.spreadAngle)), team, trans.position.dst(pawnPos), Mapper.WEAPON_COMP.get(turret.weapon));
                    getEngine().addEntity(bullet);
                    weaponComponent.fireTime = 0;
                }
            }
        }

        // If no enemy in sight
        trans.angle += (rotation * deltaTime);
        // If free turret, just rotate

    }
}
