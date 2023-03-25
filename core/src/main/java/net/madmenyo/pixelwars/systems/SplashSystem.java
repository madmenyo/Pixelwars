package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.SplashComponent;
import net.madmenyo.pixelwars.components.TextureComponent;
import net.madmenyo.pixelwars.components.TransformComponent;
import net.madmenyo.pixelwars.world.PlayField;

public class SplashSystem extends IteratingSystem {
    float fadeTime = 1.5f;
    float splashStartFading = 5;

    public SplashSystem() {
        super(Family.all(SplashComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent trans = Mapper.TRANS_COMP.get(entity);
        SplashComponent splash = Mapper.SPLASH_COMP.get(entity);

        splash.currentTime += deltaTime;

        float t = splash.currentTime / splash.endTime;

        t = Interpolation.exp5Out.apply(t);

        trans.scale.set(t * splash.xScaleMultiplier, t * splash.yScaleMultiplier);

        if (splash.currentTime > splashStartFading){
            t = (splash.currentTime - 5) / fadeTime;

            float a = MathUtils.lerp(.4f, 0, t);

            TextureComponent tex = Mapper.TEX_COMP.get(entity);
            tex.alpha = a;

            if (t > 1){
                getEngine().removeEntity(entity);
            }
        }

    }


}
