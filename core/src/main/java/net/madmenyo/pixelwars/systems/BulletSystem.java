package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import net.madmenyo.pixelwars.components.*;
import net.madmenyo.pixelwars.world.EntityFactory;
import net.madmenyo.pixelwars.world.PlayField;

public class BulletSystem extends IteratingSystem {


    private Vector2 tmpV2 = new Vector2();
    private PlayField playField;

    public BulletSystem(PlayField playField) {
        super(Family.all(BulletComponent.class).get());
        this.playField = playField;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BulletComponent bullet = Mapper.BULLET_COMP.get(entity);
        VelocityComponent velocityComponent = Mapper.VEL_COMP.get(entity);
        TransformComponent transformComponent = Mapper.TRANS_COMP.get(entity);

        bullet.lifeTime += deltaTime;
        float t = bullet.lifeTime / bullet.airTime;
        t = Interpolation.exp5Out.apply(t);

        if (t > 1) {
            explode(entity, velocityComponent);
        }
        else if (transformComponent.position.dst(bullet.startPosition) >= bullet.maxDistance){
            explode(entity, velocityComponent);
        }
        else {

            float currentSpeed = bullet.startSpeed - (bullet.startSpeed - bullet.endSpeed) * t;

            tmpV2.set(bullet.direction).scl(currentSpeed);
            velocityComponent.velocity.set(tmpV2);

            for (Entity pawn : playField.getPawns()){
                TeamComponent bulletTeam = Mapper.TEAM_COMP.get(entity);
                TeamComponent pawnTeam = Mapper.TEAM_COMP.get(pawn);

                if (!bulletTeam.equals(pawnTeam)) {
                    CircleCollisionComponent pawnCollision = Mapper.CIRCLE_COMP.get(pawn);
                    if (pawnCollision.circle.contains(transformComponent.position)) {
                        explode(entity, velocityComponent);
                        HealthComponent hp = Mapper.HEALTH_COMP.get(pawn);
                        hp.health -= bullet.damage;
                    }
                }
            }
        }
    }

    private void handleBullet(){

    }



    private void explode(Entity entity, VelocityComponent velocityComponent) {
        //remove bullet
        velocityComponent.velocity.set(0,0);

        // create splash
        getEngine().removeEntity(entity);

        TransformComponent trans = Mapper.TRANS_COMP.get(entity);
        TeamComponent bulletTeam = Mapper.TEAM_COMP.get(entity);
        Entity splash = EntityFactory.CreateSplash(getEngine(), trans.position, bulletTeam);
        getEngine().addEntity(splash);

        // if out of bounds do nothing
        if (!playField.withinBounds(trans.position.x, trans.position.y)) return;

        Entity controlTile = playField.getTileMap()[(int)(trans.position.x / RenderSystem.TILE_SIZE)][(int)(trans.position.y / RenderSystem.TILE_SIZE)].getControlTile();
        if (controlTile == null) return;

        // Check if controltile is of same team already
        TeamComponent controlTileTeam = Mapper.TEAM_COMP.get(controlTile);

        if (controlTileTeam.teamColor.equals(bulletTeam.teamColor)) return;


        /*
        ControlTileChangeComponent change = Mapper.CTRL_TILE_CHANGE_COMP.get(controlTile);
        if (change != null){
            if (change.targetColor.equals(bulletTeam.teamColor)) return;
        }

         */


        System.out.println("Hit a unowned control tile!");

        // Save old color in change component and add it to the tile

        ControlTileChangeComponent change = getEngine().createComponent(ControlTileChangeComponent.class);
        change.oldColor.set(controlTileTeam.teamColor);
        //ControlTileComponent controlTileComponent = Mapper.CTRL_TILE_COMP.get(controlTile);
        //change.currentColor.set(controlTileComponent.currentColor);
        controlTile.add(change);

        // Change the actual team imidiatly
        System.out.println("team changed to " + bulletTeam.teamName);
        controlTileTeam.teamColor.set(bulletTeam.teamColor);
        controlTileTeam.teamName = bulletTeam.teamName;

        System.out.println(controlTileTeam.equals(bulletTeam));

    }

}
