package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import net.madmenyo.pixelwars.components.*;

public class ControlPointSystem extends IteratingSystem {

    public ControlPointSystem() {
        super(Family.all(ControlPointComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ControlPointComponent controlPoint = Mapper.CTRL_POINT_COMP.get(entity);
        for (Entity tile : controlPoint.controlTiles){
            ControlTileChangeComponent change = Mapper.CTRL_TILE_CHANGE_COMP.get(tile);
            ControlTileComponent current = Mapper.CTRL_TILE_COMP.get(tile);
            TeamComponent team = Mapper.TEAM_COMP.get(tile);
            if (change != null){
                change.timer += deltaTime;

                float t = change.timer / change.fadeTime;

                //System.out.println("Current: " + current.currentColor);
                //System.out.println("Target: " + change.targetColor);

                float r = MathUtils.lerp(change.oldColor.r, team.teamColor.r, t);
                float g = MathUtils.lerp(change.oldColor.g, team.teamColor.g, t);
                float b = MathUtils.lerp(change.oldColor.b, team.teamColor.b, t);
                //System.out.println("color: " + r + ", " + g + ", " + b);
                change.currentColor.set(r, g, b, 1);

                if (t >= 1){
                    /*
                    ControlTileComponent controlTile = Mapper.CTRL_TILE_COMP.get(tile);
                    controlTile.currentColor.set(change.targetColor);
                    //controlTile.currentColor.a = 1;

                     */

                    tile.remove(ControlTileChangeComponent.class);
                }
            }
        }

        TeamComponent pointTeam = Mapper.TEAM_COMP.get(entity);

        if (controlPoint.status.equals(ControlPointComponent.Status.Owned)){
            if (isContested(controlPoint,pointTeam)){
                controlPoint.status = ControlPointComponent.Status.Contested;
                System.out.println("Control point is being contested by another team");
            }
        }

        else if (controlPoint.status.equals(ControlPointComponent.Status.Contested)){
            if (!isContested(controlPoint, pointTeam)){
                controlPoint.status = ControlPointComponent.Status.Owned;

                System.out.println("Contested control point has been taken back to original owner");

            }

            if (isTakenOver(controlPoint, pointTeam)){
                pointTeam.teamColor.set(Mapper.TEAM_COMP.get(controlPoint.controlTiles.get(0)).teamColor);
                controlPoint.status = ControlPointComponent.Status.Transition;
                System.out.println("Contested control point has started transitioning to team");
            }
        }
        else if (controlPoint.status.equals(ControlPointComponent.Status.Transition)){
            System.out.println("Time should pass to complete take over");
            controlPoint.status = ControlPointComponent.Status.Owned;

            // Get one of the tiles teams
            TeamComponent newTeam = Mapper.TEAM_COMP.get(controlPoint.controlTiles.get(0));

            // set point and turret to that team
            TeamComponent turretTeam = Mapper.TEAM_COMP.get(controlPoint.turretEntity);
            turretTeam.teamName = newTeam.teamName;
            turretTeam.teamColor.set(newTeam.teamColor);

            pointTeam.teamName = newTeam.teamName;
            pointTeam.teamColor.set(newTeam.teamColor);

        }
        else if (controlPoint.status.equals(ControlPointComponent.Status.Free)){
            if (isTakenOver(controlPoint, pointTeam)){
                controlPoint.status = ControlPointComponent.Status.Transition;
                System.out.println("Free control point has started transitioning to team: " + pointTeam.teamName);

            }
        }
    }
    private boolean isTakenOver(ControlPointComponent controlPoint, TeamComponent pointTeam) {
        Color firstColor = Mapper.TEAM_COMP.get(controlPoint.controlTiles.get(0)).teamColor;

        // If first tile equal the points team it can never be taken over, at most remain the same
        TeamComponent firstTeam = Mapper.TEAM_COMP.get(controlPoint.controlTiles.get(0));
        if (firstTeam.equals(pointTeam)) return false;

        for (Entity tile : controlPoint.controlTiles){
            TeamComponent tileTeam = Mapper.TEAM_COMP.get(tile);
            if (!tileTeam.equals(firstTeam)) return false;
            if (tileTeam.teamColor.equals(pointTeam)) return false;
            //if (tileTeam.teamColor.equals(pointTeam.teamColor)) return false;
            //if (!tileTeam.teamColor.equals(firstColor)) return false;
        }
        return true;
    }

    private boolean isContested(ControlPointComponent controlPoint, TeamComponent pointTeam){

        for (Entity tile : controlPoint.controlTiles){
            TeamComponent tileTeam = Mapper.TEAM_COMP.get(tile);

            if (!tileTeam.teamColor.equals(pointTeam.teamColor)) return true;
        }
        return false;
    }
}
