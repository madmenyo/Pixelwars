package net.madmenyo.pixelwars.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.TransformComponent;


public class CamController {
    private Entity follower;
    private Camera camera;

    public CamController(Entity follower, Camera camera) {
        this.follower = follower;
        this.camera = camera;
    }

    public void update(float delta){
        TransformComponent trans = Mapper.TRANS_COMP.get(follower);
        camera.position.set(MathUtils.round(trans.position.x), MathUtils.round(trans.position.y), 0);
        camera.update();
    }
}
