package net.madmenyo.pixelwars.gui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;
import net.madmenyo.pixelwars.components.*;
import net.madmenyo.pixelwars.world.PlayField;

public class Gui extends Stage {
    private Entity player;
    private PlayField playField;

    private Viewport guiView;
    private VisTable mainTable = new VisTable();

    private VisProgressBar healthBar;

    private ControlPointLocators controlPointLocators;
    private HealthBars healthBars;
    private PickupInfo pickupInfo;

    private Group selectedGun = new Group();
    private Image gunImage = new Image();



    public Gui(Entity player, PlayField playField, Viewport worldView) {
        this.player = player;
        this.playField = playField;

        mainTable.setFillParent(true);
        mainTable.top();
        addActor(mainTable);

        // Top bar
        VisTable topBar = new VisTable();

        // Gun selection
        Image bg = new Image(VisUI.getSkin().getDrawable("hotbar_unselected"));
        bg.setSize(64, 64);
        selectedGun.addActor(bg);
        selectedGun.setSize(64, 64);
        //gunImage.setPosition(16, 16);
        gunImage.setSize(64, 64);
        gunImage.setAlign(Align.center);
        selectedGun.addActor(gunImage);
        topBar.add(selectedGun);

        // Health
        healthBar = new VisProgressBar(0, 100, 1, false, "healthbar");
        topBar.add(healthBar).width(400).padLeft(80).padRight(80);

        mainTable.add(topBar).padTop(40);

        // Global hud

        controlPointLocators = new ControlPointLocators(playField, player, worldView);
        addActor(controlPointLocators);

        healthBars = new HealthBars(playField, player, worldView);
        addActor(healthBars);

        pickupInfo = new PickupInfo(playField, worldView, player);
        addActor(pickupInfo);

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        HealthComponent health = Mapper.HEALTH_COMP.get(player);
        healthBar.setRange(0, health.maxHealth);
        healthBar.setValue(health.health);

        PawnComponent pawn = Mapper.PAWN_COMP.get(player);
        WeaponComponent weapon = Mapper.WEAPON_COMP.get(pawn.weapon);
        gunImage.setDrawable(VisUI.getSkin().getDrawable(weapon.textureString));
    }
}
