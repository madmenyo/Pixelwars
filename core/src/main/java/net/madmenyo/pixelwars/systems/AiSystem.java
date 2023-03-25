package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.madmenyo.pixelwars.components.AiComponent;
import net.madmenyo.pixelwars.components.Mapper;

public class AiSystem extends IteratingSystem {
    public AiSystem() {
        super(Family.all(AiComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AiComponent ai = Mapper.AI_COMP.get(entity);
        ai.behavior.step();
    }
}
