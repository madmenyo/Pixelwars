package net.madmenyo.pixelwars.behavior.conditions;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import net.madmenyo.pixelwars.components.AiComponent;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.TransformComponent;
import net.madmenyo.pixelwars.systems.RenderSystem;

public class IsEnemyWithinRange extends LeafTask<Entity> {
    @Override
    public Status execute() {
        AiComponent ai = Mapper.AI_COMP.get(getObject());

        Vector2 ownPosition = Mapper.TRANS_COMP.get(getObject()).position;
        for (Entity pawn : ai.playField.getPawns()){
            Vector2 otherPosition = Mapper.TRANS_COMP.get(pawn).position;

            if (otherPosition.dst(ownPosition) < RenderSystem.TILE_SIZE * 6){
                ai.target = pawn;
                System.out.println("Enemy in range");
                return Status.SUCCEEDED;
            }
        }

        return Status.FAILED;
    }

    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        return task;
    }
}
