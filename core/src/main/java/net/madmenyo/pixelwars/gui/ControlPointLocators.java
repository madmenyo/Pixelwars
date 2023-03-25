package net.madmenyo.pixelwars.gui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import net.madmenyo.pixelwars.components.ControlPointComponent;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.TeamComponent;
import net.madmenyo.pixelwars.components.TransformComponent;
import net.madmenyo.pixelwars.world.PlayField;

public class ControlPointLocators extends Group {
    private PlayField playField;
    private Entity player;
    private Viewport worldView;
    private Array<Image> indicatorImages = new Array<>();

    private float flicketTime = 0;
    private float changeColorTime = .5f;
    private boolean colorSwitch = false;

    private Vector2 tmpV2 = new Vector2();

    public ControlPointLocators(PlayField playField, Entity player, Viewport worldView) {
        this.playField = playField;
        this.player = player;
        this.worldView = worldView;

        for (Entity entity : playField.getControlPoints()){
            Image image = new Image(VisUI.getSkin(), "indicator");
            indicatorImages.add(image);
            addActor(image);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        updateIndictors(delta);
    }

    private void updateIndictors(float delta){
        TeamComponent playerTeam = Mapper.TEAM_COMP.get(player);
        flicketTime += delta;

        for (int i = 0; i < playField.getControlPoints().size; i++) {
            Entity cpEntity = playField.getControlPoints().get(i);
            ControlPointComponent cp = Mapper.CTRL_POINT_COMP.get(cpEntity);
            TeamComponent cpTeam = Mapper.TEAM_COMP.get(cpEntity);

            // Set color
            if (cp.status.equals(ControlPointComponent.Status.Contested)){
                // Flicker animation
                if (flicketTime >= changeColorTime){
                    flicketTime -= changeColorTime;
                    colorSwitch = !colorSwitch;

                    if (colorSwitch){
                        indicatorImages.get(i).setColor(cpTeam.teamColor);
                    } else {
                        indicatorImages.get(i).setColor(Color.WHITE);
                    }
                }
            }
            else if (cp.status.equals(ControlPointComponent.Status.Free)){
                indicatorImages.get(i).setColor(Color.WHITE);
            }
            else if (cp.status.equals(ControlPointComponent.Status.Owned)){
                indicatorImages.get(i).setColor(cpTeam.teamColor);
            }

            // Set position
            TransformComponent playerTrans = Mapper.TRANS_COMP.get(player);
            TransformComponent cpTrans = Mapper.TRANS_COMP.get(cpEntity);

            tmpV2.set(cpTrans.position).sub(playerTrans.position).add(playerTrans.position);

            if (worldView.getCamera().frustum.pointInFrustum(tmpV2.x, tmpV2.y, 0)){
                indicatorImages.get(i).setVisible(false);
            } else {
                indicatorImages.get(i).setVisible(true);
            }

            worldView.project(tmpV2);


            tmpV2.x = MathUtils.clamp(tmpV2.x, 20, getStage().getWidth() - 20);
            tmpV2.y = MathUtils.clamp(tmpV2.y, 20, getStage().getHeight() - 20);


            indicatorImages.get(i).setPosition(tmpV2.x, tmpV2.y, Align.center);

        }



    }
}
