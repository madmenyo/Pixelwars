package net.madmenyo.pixelwars.world;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;


public class Tile {
    private TextureRegion region;

    private TextureRegion obstacles;
    private float obstacleRotation = 0;
    private float obstacleScale = 1;

    private Entity controlTile;
    private Entity controlTurret;

    public Tile(TextureRegion region) {
        this.region = region;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    public TextureRegion getObstacles() {
        return obstacles;
    }

    public void setObstacles(TextureRegion obstacles) {
        this.obstacles = obstacles;
        obstacleRotation = MathUtils.random() * 360;
        obstacleScale = MathUtils.random() * .3f + .85f;
    }

    public Entity getControlTile() {
        return controlTile;
    }

    public void setControlTile(Entity controlTile) {
        this.controlTile = controlTile;
    }

    public float getObstacleRotation() {
        return obstacleRotation;
    }

    public float getObstacleScale() {
        return obstacleScale;
    }

    public Entity getControlTurret() {
        return controlTurret;
    }

    public void setControlTurret(Entity controlTurret) {
        this.controlTurret = controlTurret;
    }
}
