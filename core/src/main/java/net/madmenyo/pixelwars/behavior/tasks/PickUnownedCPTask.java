package net.madmenyo.pixelwars.behavior.tasks;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import net.madmenyo.pixelwars.components.*;

public class PickUnownedCPTask extends LeafTask<Entity> {
    private GridPoint2 pawnCoord = new GridPoint2();

    @Override
    public Status execute() {
        TransformComponent trans = Mapper.TRANS_COMP.get(getObject());
        AiComponent ai = Mapper.AI_COMP.get(getObject());
        TeamComponent teamComponent = Mapper.TEAM_COMP.get(getObject());

        pawnCoord.set(MathUtils.floor(trans.position.x), MathUtils.floor(trans.position.y));

        if (ai.playField.getControlPoints().isEmpty()) return Status.FAILED;

        Entity closest = null;
        for (Entity cpEntity : ai.playField.getControlPoints()){
            TeamComponent team = Mapper.TEAM_COMP.get(cpEntity);
            if (!team.equals(teamComponent)){
                if (closest == null) {
                    closest = cpEntity;
                }
                else {
                    ControlPointComponent closestCP = Mapper.CTRL_POINT_COMP.get(closest);
                    ControlPointComponent otherCP = Mapper.CTRL_POINT_COMP.get(cpEntity);

                    if (pawnCoord.dst2(closestCP.x, closestCP.y) > pawnCoord.dst2(otherCP.x, otherCP.y)){
                        closest = cpEntity;
                    }
                }
            }
        }

        if (closest == null)return Status.FAILED;

        ai.target = closest;
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        return task;
    }
}
