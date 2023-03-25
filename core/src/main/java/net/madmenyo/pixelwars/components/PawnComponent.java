package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class PawnComponent implements Component, Pool.Poolable {

    public enum State{
        Idle,
        Walking,
        Running,
        Shooting,
        Dead
    }

    public State state = State.Idle;

    public String textureName;

    public float speed = 400;

    public Vector2 spawnPoint = new Vector2();

    public float deathTimer = 0;

    public Entity weapon;

    @Override
    public void reset() {
        state = State.Idle;
        textureName = "";
        speed = 200;
        deathTimer = 0;
        weapon = null;
    }
}
