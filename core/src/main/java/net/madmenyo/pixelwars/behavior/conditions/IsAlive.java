package net.madmenyo.pixelwars.behavior.conditions;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.PawnComponent;

public class IsAlive extends LeafTask<Entity> {

    @Override
    public Status execute() {
        PawnComponent pawn = Mapper.PAWN_COMP.get(getObject());
        if (pawn.state.equals(PawnComponent.State.Dead)){
            return Status.FAILED;
        }

        return Status.SUCCEEDED;
    }

    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        return null;
    }
}
