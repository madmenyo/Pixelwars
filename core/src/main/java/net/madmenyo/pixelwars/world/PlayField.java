package net.madmenyo.pixelwars.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.madmenyo.pixelwars.components.*;
import net.madmenyo.pixelwars.systems.RenderSystem;


public class PlayField {

    private Tile[][] tileMap;
    private int width, height;

    private Array<Entity> controlPoints = new Array<>();

    private Array<Entity> pawns = new Array<>();

    private TextureAtlas atlas;
    private Engine engine;

    private float nextPickupSpawn = 1;
    private int maxPickups = 10;
    private Array<Entity> pickups = new Array<>();




    public PlayField(int width, int height, TextureAtlas atlas, Engine engine) {
        this.width = width;
        this.height = height;
        this.atlas = atlas;
        this.engine = engine;

        tileMap = new Tile[width][height];

        generateTiles();
        controlPoints.add(createControlPointAt(3, 3));
        controlPoints.add(createControlPointAt(14, 7));

        for (int i = 0; i < 12; i++) {
            generateRandomPickup();
        }
    }

    private void generateRandomPickup() {
        Vector2 pos = new Vector2();
        //pos.set(getWidth() * RenderSystem.TILE_SIZE + RenderSystem.TILE_SIZE * .5f, getHeight() * RenderSystem.TILE_SIZE + RenderSystem.TILE_SIZE + .5f);
        pos.set(MathUtils.random(getWidth()) * RenderSystem.TILE_SIZE + RenderSystem.TILE_SIZE * .5f, MathUtils.random(getHeight()) * RenderSystem.TILE_SIZE + RenderSystem.TILE_SIZE * .5f);

        Entity item = null;
        int nr = MathUtils.random(100);
        if (nr < 40){
            item = EntityFactory.CreateRifle(engine);
        } else {
            item = EntityFactory.CreateMachineGun(engine);
        }

        Entity crate = EntityFactory.CreatePickup(engine, atlas, pos, item);
        engine.addEntity(crate);
        pickups.add(crate);
    }

    public Tile[][] generateTiles(){
        // generate terrain
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TextureRegion region = getRandomIndexedRegion("grass");
                tileMap[x][y] = new Tile(region);

                int rnd = MathUtils.random(100);
                if (rnd < 80) continue;
                else if (rnd < 82) tileMap[x][y].setObstacles(getRandomIndexedRegion("tree"));
                else if (rnd < 87) tileMap[x][y].setObstacles(getRandomIndexedRegion("bush"));
                else if (rnd < 92) tileMap[x][y].setObstacles(getRandomIndexedRegion("rock"));
                else if (rnd < 97) tileMap[x][y].setObstacles(getRandomIndexedRegion("leaves"));
                else tileMap[x][y].setObstacles(getRandomIndexedRegion("plant"));
            }
        }

        // Create objects
        return tileMap;
    }


    private TextureRegion getRandomIndexedRegion(String name){
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(name);

        return regions.get(MathUtils.random(regions.size - 1));

    }

    private Entity createControlPointAt(int x, int y){
        Entity controlPointEntity = engine.createEntity();
        TeamComponent pointTeam = engine.createComponent(TeamComponent.class);
        pointTeam.teamColor.set(Color.WHITE);
        pointTeam.teamName = "Neutral";
        controlPointEntity.add(pointTeam);

        ControlPointComponent controlPoint = engine.createComponent(ControlPointComponent.class);
        controlPoint.x = x;
        controlPoint.y = y;
        controlPointEntity.add(controlPoint);
        engine.addEntity(controlPointEntity);

        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        transformComponent.position.set(x * RenderSystem.TILE_SIZE + RenderSystem.TILE_SIZE * .5f, y * RenderSystem.TILE_SIZE + RenderSystem.TILE_SIZE * .5f);
        controlPointEntity.add(transformComponent);


        for (int iy = -1; iy <= 1; iy++) {
            for (int ix = -1; ix <= 1; ix++) {
                if (ix == 0 && iy == 0){
                    tileMap[x + ix][y + iy].setRegion(atlas.findRegion("towerfloor"));
                    // Add turret in the middle
                    Entity turretEntity = engine.createEntity();
                    ControlTileTurretComponent turret = engine.createComponent(ControlTileTurretComponent.class);
                    turretEntity.add(turret);

                    TeamComponent turretTeam = engine.createComponent(TeamComponent.class);
                    turretTeam.teamColor.set(Color.WHITE);
                    turretTeam.teamName = "Neutral";
                    turretEntity.add(turretTeam);

                    TransformComponent turretTrans = engine.createComponent(TransformComponent.class);
                    WorldUtils.CoordToWorld(x + ix, y+iy, turretTrans.position);
                    turretEntity.add(turretTrans);

                    Entity weaponEntity = engine.createEntity();

                    WeaponComponent weaponComponent = engine.createComponent(WeaponComponent.class);
                    weaponComponent.maxDistance = 700;
                    weaponComponent.rateOfFire = .2f;
                    weaponComponent.spreadAngle = 10;
                    weaponComponent.damage = 12;
                    weaponEntity.add(weaponComponent);

                    turret.weapon = weaponEntity;

                    engine.addEntity(turretEntity);
                    controlPoint.turretEntity = turretEntity;
                    tileMap[x + ix][y + iy].setControlTurret(turretEntity);
                }else {
                    Entity controlTileEntity = engine.createEntity();
                    ControlTileComponent controlTile = engine.createComponent(ControlTileComponent.class);
                    controlTileEntity.add(controlTile);

                    TeamComponent tileTeam = engine.createComponent(TeamComponent.class);
                    tileTeam.teamColor.set(Color.WHITE);
                    tileTeam.teamName = "Neutral";
                    controlTileEntity.add(tileTeam);

                    TransformComponent tileTransform = engine.createComponent(TransformComponent.class);
                    WorldUtils.CoordToWorld(x + ix, y + iy, tileTransform.position);
                    controlTileEntity.add(tileTransform);



                    // Add controlTile to map, engine and controlPoint
                    engine.addEntity(controlTileEntity);
                    controlPoint.controlTiles.add(controlTileEntity);
                    tileMap[x + ix][y + iy].setControlTile(controlTileEntity);
                }
                tileMap[x + ix][y + iy].setObstacles(null);
            }
        }

        return controlPointEntity;
    }

    public void update(float delta){
        // Respawn crates?
    }

    public boolean withinBounds(int tileX, int tileY){
        return tileX >= 0 && tileX < width && tileY >= 0 && tileY < height;
    }

    public boolean withinBounds(float worldX, float worldY){
        return withinBounds(MathUtils.floor(worldX / RenderSystem.TILE_SIZE), MathUtils.floor(worldY / RenderSystem.TILE_SIZE));
    }

    public void addPawn(Entity pawn){
        pawns.add(pawn);
    }

    public void removePawn(Entity pawn){
        pawns.removeValue(pawn, true);
    }


    public Tile[][] getTileMap() {
        return tileMap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Array<Entity> getPawns() {
        return pawns;
    }

    public Array<Entity> getControlPoints() {
        return controlPoints;
    }

    public Array<Entity> getPickups() {
        return pickups;
    }
}
