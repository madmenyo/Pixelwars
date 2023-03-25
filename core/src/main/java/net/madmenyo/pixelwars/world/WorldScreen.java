package net.madmenyo.pixelwars.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.madmenyo.pixelwars.Assets;
import net.madmenyo.pixelwars.PixelWars;
import net.madmenyo.pixelwars.PlayerController;
import net.madmenyo.pixelwars.gui.Gui;
import net.madmenyo.pixelwars.systems.*;

public class WorldScreen extends ScreenAdapter {
    private PixelWars game;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch worldBatch;
    private Viewport worldView;
    private TextureAtlas spriteSheet;

    private Engine engine = new PooledEngine();

    private PlayField playField;

    private Gui gui;

    private Entity player;
    private CamController camController;
    private PlayerController playerController;

    private Entity enemy;


    public WorldScreen(PixelWars game) {
        this.game = game;

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        worldBatch = new SpriteBatch();
        worldView = new ScreenViewport();

        spriteSheet = game.assets.getAssetManager().get(Assets.SPRITE_ASSETS);


    }

    private void setSystems() {
        // Handle asset assignment when needed in renderSystem
        engine.addSystem(new PawnSystem(spriteSheet));
        engine.addSystem(new PickupSystem(playField));
        engine.addSystem(new AiSystem());
        engine.addSystem(new ControlPointSystem());
        engine.addSystem(new TurretSystem(playField.getPawns()));
        engine.addSystem(new BulletSystem(playField));
        engine.addSystem(new SplashSystem());
        engine.addSystem(new HealthSystem());
        engine.addSystem(new MoveSystem());
        engine.addSystem(new RenderSystem(worldBatch, worldView, game.assets, playField));
        engine.addSystem(new PlayerSystem(worldView, playerController));
    }


    @Override
    public void show() {
        playField = new PlayField(24, 24, spriteSheet, engine);

        playerController = new PlayerController();
        Gdx.input.setInputProcessor(playerController.getInputProcessor());

        setSystems();
        testPlayer();
        testEnemy();

        gui = new Gui(player, playField, worldView);

        camController = new CamController(player, worldView.getCamera());
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY, true);
        camController.update(delta);
        engine.update(delta);

        gui.act();
        gui.draw();

        shapeRenderer.setProjectionMatrix(worldView.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(0,0, 16);

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        worldView.update(width, height);
    }

    private void testPlayer(){
        player = EntityFactory.CreatePlayer(engine, new Vector2(560,200), "hitman1", Color.RED);
        engine.addEntity(player);
        playField.addPawn(player);
    }

    private void testEnemy() {
        Array<Entity> others = new Array<>();
        others.add(player);
        //enemy = EntityFactory.CreateEnemy(engine, new Vector2(RenderSystem.TILE_SIZE * 10, RenderSystem.TILE_SIZE * 10), "hitman1", "enemy", Color.BLUE, others);
        enemy = EntityFactory.CreateEnemy(engine, new Vector2(800, 800), "hitman1", "enemy", Color.BLUE, playField);
        engine.addEntity(enemy);
        playField.addPawn(enemy);
    }


}
