package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import net.madmenyo.pixelwars.components.*;
import net.madmenyo.pixelwars.world.PlayField;

public class PickupSystem extends IteratingSystem {

    private PlayField playField;

    public PickupSystem(PlayField playField) {
        super(Family.all(PickupComponent.class).get());
        this.playField = playField;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CircleCollisionComponent crateCol = Mapper.CIRCLE_COMP.get(entity);

        for (Entity pawnEntity : playField.getPawns()){
            CircleCollisionComponent pawnCol = Mapper.CIRCLE_COMP.get(pawnEntity);

            if (crateCol.circle.overlaps(pawnCol.circle)){
                // Take weapon
                PickupComponent pickup = Mapper.PICKUP_COMP.get(entity);
                if (Mapper.WEAPON_COMP.has(pickup.item)){
                    // Add weapon
                    WeaponComponent weapon = Mapper.WEAPON_COMP.get(pickup.item);

                    PawnComponent pawnComponent = Mapper.PAWN_COMP.get(pawnEntity);
                    getEngine().removeEntity(pawnComponent.weapon);
                    pawnComponent.weapon = pickup.item;
                }

                getEngine().removeEntity(entity);
            }

        }

    }
}
