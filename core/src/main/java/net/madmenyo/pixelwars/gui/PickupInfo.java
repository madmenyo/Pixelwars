package net.madmenyo.pixelwars.gui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import net.madmenyo.pixelwars.components.Mapper;
import net.madmenyo.pixelwars.components.PickupComponent;
import net.madmenyo.pixelwars.components.TransformComponent;
import net.madmenyo.pixelwars.components.WeaponComponent;
import net.madmenyo.pixelwars.world.PlayField;

public class PickupInfo extends Group {
    private PlayField playField;
    private Viewport worldView;
    private Entity player;

    private Vector2 tmpV2 = new Vector2();


    private Array<Image> itemIndicators = new Array<>();

    public PickupInfo(PlayField playField, Viewport worldView, Entity player) {
        this.playField = playField;
        this.worldView = worldView;
        this.player = player;

        for (int i = 0; i < 5; i++) {
            Image image = new Image();
            image.setSize(64, 64);
            image.setPosition(i * 54, 0);
            itemIndicators.add(image);
            addActor(image);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (Image image : itemIndicators){
            image.setVisible(false);
        }

        TransformComponent trans = Mapper.TRANS_COMP.get(player);
        int i = 0;
        for (Entity pickup : playField.getPickups()){

            TransformComponent pickupTrans = Mapper.TRANS_COMP.get(pickup);
            if (pickupTrans == null) continue;
            if (trans.position.dst(pickupTrans.position) < 200) {
                System.out.println("Show pickup");
                // Show label
                PickupComponent pick = Mapper.PICKUP_COMP.get(pickup);
                if (pick == null) continue;;
                WeaponComponent weapon = Mapper.WEAPON_COMP.get(pick.item);
                itemIndicators.get(i).setDrawable(VisUI.getSkin(), weapon.textureString);
                itemIndicators.get(i).setVisible(true);
                tmpV2.set(pickupTrans.position);
                worldView.project(tmpV2);
                itemIndicators.get(i).setPosition(tmpV2.x - 32, tmpV2.y - 32);


                i++;
                if (i >= itemIndicators.size) return;
            }
        }

    }
}
