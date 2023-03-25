package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import net.madmenyo.pixelwars.world.PlayField;

public class AiComponent implements Component, Pool.Poolable {
    public BehaviorTree<Entity> behavior;
    public Engine engine;

    public PlayField playField;
    public Entity target;

    @Override
    public void reset() {
        behavior.reset();
        playField = null;
        target = null;
        engine = null;
    }
}
