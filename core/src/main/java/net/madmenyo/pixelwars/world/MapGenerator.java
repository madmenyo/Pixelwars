package net.madmenyo.pixelwars.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import net.madmenyo.pixelwars.components.*;


public class MapGenerator {

    private int width, height;
    private Tile[][] tileMap;

    private TextureAtlas atlas;

    private Engine engine;

    public MapGenerator(int width, int height, TextureAtlas atlas, Engine engine) {
        this.width = width;
        this.height = height;
        this.atlas = atlas;
        this.engine = engine;
        tileMap = new Tile[width][height];

    }

    public Tile[][] generate(){
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

        //createControlPoint(3, 3, 4, 6);
        createControlPoint(3, 3);

        // Create objects
        return tileMap;
    }

    private void createControlPoint(int x, int y, int width, int height){
        Entity controlPointEntity = engine.createEntity();
        ControlPointComponent controlPoint = engine.createComponent(ControlPointComponent.class);
        controlPoint.x = x;
        controlPoint.y = y;
        //controlPoint.width = width;
        //controlPoint.height = height;
        controlPointEntity.add(controlPoint);
        engine.addEntity(controlPointEntity);


        for (int iy = 0; iy < height; iy++) {
            for (int ix = 0; ix < width; ix++) {
                Entity controlTileEntity = engine.createEntity();
                ControlTileComponent controlTile = engine.createComponent(ControlTileComponent.class);
                controlTileEntity.add(controlTile);

                TeamComponent teamComponent = engine.createComponent(TeamComponent.class);
                teamComponent.teamColor.set(Color.WHITE);
                teamComponent.teamName = "Neutral";
                controlTileEntity.add(teamComponent);


                // Add controlTile to map, engine and controlPoint
                engine.addEntity(controlTileEntity);
                controlPoint.controlTiles.add(controlTileEntity);
                tileMap[x + ix][y + iy].setControlTile(controlTileEntity);
            }
        }
    }

    private void createControlPoint(int x, int y){
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


        for (int iy = -1; iy <= 1; iy++) {
            for (int ix = -1; ix <= 1; ix++) {
                if (ix == 0 && iy == 0){
                    // Add turret in the middle
                    Entity turretEntity = engine.createEntity();
                    ControlTileTurretComponent turret = engine.createComponent(ControlTileTurretComponent.class);
                    turretEntity.add(turret);

                    TeamComponent turretTeam = engine.createComponent(TeamComponent.class);
                    turretTeam.teamColor.set(Color.WHITE);
                    turretTeam.teamName = "Neutral";
                    turretEntity.add(turretTeam);

                    engine.addEntity(turretEntity);
                    controlPoint.turretEntity = turretEntity;
                    tileMap[x + ix][y + iy].setControlTurret(turretEntity);

                    Entity weaponEntity = engine.createEntity();

                    WeaponComponent weaponComponent = engine.createComponent(WeaponComponent.class);
                    weaponComponent.maxDistance = 700;
                    weaponComponent.rateOfFire = .6f;
                    weaponComponent.spreadAngle = 30;
                    weaponComponent.damage = 12;
                    weaponEntity.add(weaponComponent);

                    turret.weapon = weaponEntity;


                }else {
                    Entity controlTileEntity = engine.createEntity();
                    ControlTileComponent controlTile = engine.createComponent(ControlTileComponent.class);
                    controlTileEntity.add(controlTile);

                    TeamComponent tileTeam = engine.createComponent(TeamComponent.class);
                    tileTeam.teamColor.set(Color.WHITE);
                    tileTeam.teamName = "Neutral";
                    controlTileEntity.add(tileTeam);


                    // Add controlTile to map, engine and controlPoint
                    engine.addEntity(controlTileEntity);
                    controlPoint.controlTiles.add(controlTileEntity);
                    tileMap[x + ix][y + iy].setControlTile(controlTileEntity);
                }
                tileMap[x + ix][y + iy].setObstacles(null);
                tileMap[x + ix][y + iy].setRegion(null);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile[][] getTileMap() {
        return tileMap;
    }

    private TextureRegion getRandomIndexedRegion(String name){
         Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(name);

         return regions.get(MathUtils.random(regions.size - 1));

    }
}
