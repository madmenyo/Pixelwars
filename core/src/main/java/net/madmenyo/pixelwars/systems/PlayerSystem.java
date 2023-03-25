package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.madmenyo.pixelwars.PlayerController;
import net.madmenyo.pixelwars.components.*;
import net.madmenyo.pixelwars.world.EntityFactory;

public class PlayerSystem extends IteratingSystem {

    private Viewport viewport;
    private PlayerController playerController;

    private Vector2 mousePosition = new Vector2();
    private Vector2 towardMouse = new Vector2();

    private Vector2 tmpV2 = new Vector2();



    public PlayerSystem(Viewport viewport, PlayerController playerController) {
        super(Family.all(PlayerComponent.class).get());
        this.viewport = viewport;
        this.playerController = playerController;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent trans = Mapper.TRANS_COMP.get(entity);
        TeamComponent team = Mapper.TEAM_COMP.get(entity);
        VelocityComponent vel = Mapper.VEL_COMP.get(entity);
        PawnComponent pawn = Mapper.PAWN_COMP.get(entity);
        WeaponComponent weapon = Mapper.WEAPON_COMP.get(pawn.weapon);

        rotateToMouse(trans);

        weapon.fireTime += deltaTime;

        if (playerController.fire){
            if (weapon.fireTime >= weapon.rateOfFire) {
                tmpV2.set(0, 0);
                tmpV2.set(towardMouse).nor();

                tmpV2.rotateDeg(MathUtils.random(-1f, 1f) * weapon.spreadAngle);

                Entity bullet = EntityFactory.CreateBullet(getEngine(), trans.position, tmpV2, team, mousePosition.dst(trans.position), weapon);
                getEngine().addEntity(bullet);
                weapon.fireTime = 0;
            }
        }

        tmpV2.set(0,0);

        if (playerController.moveForward){
            if (!playerController.moveBackward){
                // Move up
                tmpV2.y = 1;
            }
        }
        if (playerController.moveBackward){
            if (!playerController.moveForward){
                // Move down
                tmpV2.y = -1f;
            }
        }
        if (playerController.strafeLeft){
            if (!playerController.strafeRight){
                // Move left
                tmpV2.x = -1f;
            }
        }
        if (playerController.strafeRight){
            if (!playerController.strafeLeft){
                // Move right
                tmpV2.x = 1f;
            }
        }

        vel.velocity.set(tmpV2.nor().scl(pawn.speed));

        //moveToMouse(entity, tr, team, vel);

    }

    private void rotateToMouse(TransformComponent trans) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePosition);
        towardMouse.set(mousePosition).sub(trans.position);
        trans.angle = towardMouse.angleDeg();
    }


}
