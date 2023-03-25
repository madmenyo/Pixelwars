package net.madmenyo.pixelwars.behavior.tasks;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

public class AttackTask extends LeafTask<Entity> {
    @Override
    public Status execute() {
        System.out.println("Shooting!");
        return Status.RUNNING;
    }

    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        return task;
    }
}
