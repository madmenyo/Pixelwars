package net.madmenyo.pixelwars.gui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import net.madmenyo.pixelwars.components.HealthComponent;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.TransformComponent;
import net.madmenyo.pixelwars.world.PlayField;

public class HealthBars extends Group {

    private PlayField playField;
    private Entity player;
    private Viewport worldView;

    private Array<VisProgressBar> bars = new Array<>();

    private Vector2 tmpV2 = new Vector2();

    public HealthBars(PlayField playField, Entity player, Viewport worldView) {
        this.playField = playField;
        this.player = player;
        this.worldView = worldView;
        for (int i = 0; i < playField.getPawns().size; i++) {
            HealthComponent hp = Mapper.HEALTH_COMP.get(playField.getPawns().get(i));
            VisProgressBar pb = new VisProgressBar(0, hp.maxHealth, 1, false, "smallbar");
            bars.add(pb);
            addActor(pb);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (int i = 0; i < playField.getPawns().size; i++) {
            TransformComponent trans = Mapper.TRANS_COMP.get(playField.getPawns().get(i));

            if (!worldView.getCamera().frustum.pointInFrustum(trans.position.x, trans.position.y, 0)) {
                //bars.get(i).setVisible(false);
                //continue;
            }

            if (Mapper.PLAYER_COMP.has(playField.getPawns().get(i))){
                bars.get(i).setVisible(false);
                continue;
            }

            HealthComponent hp = Mapper.HEALTH_COMP.get(playField.getPawns().get(i));
            bars.get(i).setValue(hp.health);

            tmpV2.set(trans.position);
            worldView.project(tmpV2);

            bars.get(i).setBounds(tmpV2.x - 32, tmpV2.y + 32, 64, 8);

        }
    }
}
