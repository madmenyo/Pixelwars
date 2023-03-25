package net.madmenyo.pixelwars.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.madmenyo.pixelwars.components.ControlPointComponent;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.TeamComponent;
import net.madmenyo.pixelwars.systems.RenderSystem;

public class WorldUtils {

    public static Vector2 CoordToWorld(int x, int y, Vector2 out){
        return out.set(x * RenderSystem.TILE_SIZE + RenderSystem.TILE_SIZE * .5f, y * RenderSystem.TILE_SIZE + RenderSystem.TILE_SIZE * .5f);
    }

    public static Entity getClosestUnownedEntity(Vector2 position, TeamComponent teamComponent, Array<Entity> entities){
        if (entities.isEmpty()) return null;

        Entity closest = null;
        for (Entity entity : entities){
            TeamComponent team = Mapper.TEAM_COMP.get(entity);
            if (teamComponent == null){
                throw new NullPointerException("Cannot find closest Unowned entity. Entity does not have a TeamComponent");
            }
            if (!team.equals(teamComponent)){
                if (closest == null) {
                    closest = entity;
                }
                else {
                    Vector2 otherPos = Mapper.TRANS_COMP.get(entity).position;
                    Vector2 closestPos = Mapper.TRANS_COMP.get(closest).position;

                    if (position.dst2(closestPos) > position.dst2(otherPos)){
                        closest = entity;
                    }
                }
            }
        }
        return closest;
    }

    public static float getClosestRotationDirection(Vector2 desiredDirection, Vector2 currentDirection){

        float rotation = 0;

        float dotSide = currentDirection.dot(desiredDirection);
        if (dotSide <= 0){
            rotation = -1;
        } else {
            rotation = 1;
        }

        return rotation;
    }

}
