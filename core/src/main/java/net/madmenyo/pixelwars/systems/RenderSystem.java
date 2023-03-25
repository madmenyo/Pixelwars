package net.madmenyo.pixelwars.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.madmenyo.pixelwars.Assets;
import net.madmenyo.pixelwars.components.*;
import net.madmenyo.pixelwars.world.PlayField;
import net.madmenyo.pixelwars.world.Tile;

public class RenderSystem extends SortedIteratingSystem {

    public static final float TILE_SIZE = 64;

    private SpriteBatch worldBatch;
    private Viewport viewport;

    private Assets assets;

    private PlayField playField;

    private TextureAtlas sprites;


    public RenderSystem(SpriteBatch worldBatch, Viewport viewport, Assets assets, PlayField playField) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), new ZComparator());
        this.worldBatch = worldBatch;
        this.viewport = viewport;
        this.assets = assets;
        this.playField = playField;

        sprites = assets.getAssetManager().get(Assets.SPRITE_ASSETS);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent tr = Mapper.TRANS_COMP.get(entity);
        TextureComponent tex = Mapper.TEX_COMP.get(entity);
        TeamComponent team = Mapper.TEAM_COMP.get(entity);

        if (tex.texture == null){
            AssetInfoComponent assetInfo = Mapper.ASSET_COMP.get(entity);
            if (assetInfo != null) {
                tex.texture = assets.getAssetManager().get(Assets.SPRITE_ASSETS).findRegion(assetInfo.textureName);
            }
        }

        if (Mapper.PAWN_COMP.has(entity)) {
            worldBatch.setColor(Color.WHITE);
            worldBatch.setColor(1, 1, 1, tex.alpha);
            //System.out.println(worldBatch.getColor().a);
            worldBatch.draw(tex.texture,
                    tr.position.x - 14, tr.position.y - 22,
                    13, 22,
                    tex.texture.getRegionWidth(), tex.texture.getRegionHeight(),
                    tr.scale.x, tr.scale.y,
                    tr.angle);
        } else if (team != null) {
            worldBatch.setColor(team.teamColor.r, team.teamColor.g, team.teamColor.b, tex.alpha);

            worldBatch.draw(tex.texture,
                    tr.position.x - tex.texture.getRegionWidth() * .5f, tr.position.y - tex.texture.getRegionHeight() * .5f,
                    tex.texture.getRegionWidth() * .5f, tex.texture.getRegionHeight() * .5f,
                    tex.texture.getRegionWidth(), tex.texture.getRegionHeight(),
                    tr.scale.x, tr.scale.y,
                    tr.angle);
        } else {
            worldBatch.setColor(Color.WHITE);
            worldBatch.draw(tex.texture,
                    tr.position.x - tex.texture.getRegionWidth() * .5f, tr.position.y - tex.texture.getRegionHeight() * .5f,
                    tex.texture.getRegionWidth() * .5f, tex.texture.getRegionHeight() * .5f,
                    tex.texture.getRegionWidth(), tex.texture.getRegionHeight(),
                    tr.scale.x, tr.scale.y,
                    tr.angle);
        }

    }

    @Override
    public void update(float deltaTime) {

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        worldBatch.setProjectionMatrix(viewport.getCamera().combined);
        //worldBatch.enableBlending();
        worldBatch.begin();

        worldBatch.setColor(Color.WHITE);
        for (int y = 0; y < playField.getHeight(); y++) {
            for (int x = 0; x < playField.getWidth(); x++) {
                Tile tile = playField.getTileMap()[x][y];
                if (tile.getControlTile() != null) {
                    renderControlTile(x, y, tile);
                }
                else if (tile.getControlTurret() != null){
                    worldBatch.setColor(Color.WHITE);
                    worldBatch.draw(playField.getTileMap()[x][y].getRegion(), x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    renderTurret(x, y, tile);
                }
                else {
                    worldBatch.setColor(Color.WHITE);
                    worldBatch.draw(playField.getTileMap()[x][y].getRegion(), x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }

        worldBatch.setColor(Color.WHITE);

        for (int y = 0; y < playField.getHeight(); y++) {
            for (int x = 0; x < playField.getWidth(); x++) {
                if (playField.getTileMap()[x][y].getControlTile() == null) {
                    if (playField.getTileMap()[x][y].getObstacles() != null) {
                        Tile tile = playField.getTileMap()[x][y];
                        worldBatch.draw(tile.getObstacles(), x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE * .5f, TILE_SIZE * .5f, TILE_SIZE, TILE_SIZE, tile.getObstacleScale(), tile.getObstacleScale(), tile.getObstacleRotation());
                    }
                }
            }
        }

        super.update(deltaTime);
        worldBatch.end();
        //worldBatch.disableBlending();

        Gdx.gl20.glDisable(GL20.GL_BLEND);


    }

    private void renderTurret(int x, int y, Tile tile){
        ControlTileTurretComponent turretEntity = Mapper.CTRL_TURRET_COMP.get(tile.getControlTurret());
        TransformComponent trans = Mapper.TRANS_COMP.get(tile.getControlTurret());
        TeamComponent team = Mapper.TEAM_COMP.get(tile.getControlTurret());

        TextureRegion turretBase = sprites.findRegion("tower01_base");
        TextureRegion turretCanon = sprites.findRegion("tower01_cannon");

        worldBatch.setColor(Color.WHITE);

        worldBatch.draw(turretCanon,
                x * TILE_SIZE, y * TILE_SIZE,
                turretCanon.getRegionWidth() * .5f,  turretCanon.getRegionHeight() * .5f,
                TILE_SIZE, TILE_SIZE,
                1, 1,
                trans.angle);

        worldBatch.setColor(team.teamColor);
        worldBatch.draw(turretBase,
                x * TILE_SIZE, y * TILE_SIZE,
                turretBase.getRegionWidth() * .5f,  turretBase.getRegionHeight() * .5f,
                TILE_SIZE, TILE_SIZE,
                1, 1,
                trans.angle);


    }
    private void renderControlTile(int x, int y, Tile tile) {
        ControlTileChangeComponent change = Mapper.CTRL_TILE_CHANGE_COMP.get(tile.getControlTile());
        if (change != null) {
            worldBatch.setColor(change.currentColor);
        }
        else {
            //ControlTileComponent controlTileComponent = Mapper.CTRL_TILE_COMP.get(tile.getControlTile());
            TeamComponent teamComponent = Mapper.TEAM_COMP.get(tile.getControlTile());
            worldBatch.setColor(teamComponent.teamColor);
        }
        worldBatch.draw(sprites.findRegion("controltile"), x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
