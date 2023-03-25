package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import net.madmenyo.pixelwars.components.Mapper;

import java.util.Comparator;

public class ZComparator implements Comparator<Entity> {
    @Override
    public int compare(Entity entity1, Entity entity2) {
        int z1 = Mapper.TRANS_COMP.get(entity1).zLevel;
        int z2 = Mapper.TRANS_COMP.get(entity2).zLevel;

        int result = 0;
        if (z1 > z2) result = 1;
        else if (z1 < z2) result = -1;
        return result;
    }
}
