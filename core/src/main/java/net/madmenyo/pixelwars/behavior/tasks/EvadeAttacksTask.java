package net.madmenyo.pixelwars.behavior.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

import javax.swing.text.html.parser.Entity;

public class EvadeAttacksTask extends LeafTask<Entity> {
    @Override
    public Status execute() {
        return null;
    }

    @Override
    protected Task<Entity> copyTo(Task<Entity> task) {
        return task;
    }
}
