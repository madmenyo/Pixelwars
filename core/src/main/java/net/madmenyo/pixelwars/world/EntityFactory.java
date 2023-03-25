package net.madmenyo.pixelwars.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StreamUtils;
import net.madmenyo.pixelwars.components.*;

import java.io.Reader;

public class EntityFactory {

    public static Entity CreatePlayer(Engine engine, Vector2 spawnPoint, String pawnType, Color teamColor){
        Entity player = CreatePawn(engine, spawnPoint, pawnType);

        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        player.add(playerComponent);

        TeamComponent teamComponent = engine.createComponent(TeamComponent.class);
        teamComponent.teamName = "Might reds";
        teamComponent.teamColor.set(teamColor);
        player.add(teamComponent);
        /*
        PaintComponent paint = engine.createComponent(PaintComponent.class);
        paint.color = paintColor;
        player.add(paint);
         */

        HealthComponent health = Mapper.HEALTH_COMP.get(player);
        health.maxHealth = 200;
        health.health = 200;

        return player;
    }

    public static Entity CreateEnemy(Engine engine, Vector2 spawnPoint, String textureName, String teamName, Color teamColor, PlayField playeField){
        Entity enemy = CreatePawn(engine, spawnPoint, textureName);

        TeamComponent teamComponent = engine.createComponent(TeamComponent.class);
        teamComponent.teamName = teamName;
        teamComponent.teamColor.set(teamColor);
        enemy.add(teamComponent);

        AiComponent aiComponent = engine.createComponent(AiComponent.class);
        aiComponent.playField = playeField;
        aiComponent.engine = engine;

        Reader reader = null;
        try {
            reader = Gdx.files.internal("behavior/simple.tree").reader();
            BehaviorTreeParser<Entity> parser = new BehaviorTreeParser<>(BehaviorTreeParser.DEBUG_HIGH);
            aiComponent.behavior = parser.parse(reader, enemy);
        } finally {
            StreamUtils.closeQuietly(reader);
        }


        enemy.add(aiComponent);

        return enemy;
    }

    public static Entity CreatePawn(Engine engine, Vector2 spawnPoint, String pawnType){
        Entity pawn = CreateObjectAt(engine, spawnPoint);

        PawnComponent pawnComponent = engine.createComponent(PawnComponent.class);
        pawnComponent.textureName = pawnType;
        pawnComponent.spawnPoint.set(spawnPoint);
        pawn.add(pawnComponent);

        pawnComponent.weapon = CreateGun(engine);

        TextureComponent tex = engine.createComponent(TextureComponent.class);
        pawn.add(tex);

        VelocityComponent vel = engine.createComponent(VelocityComponent.class);
        pawn.add(vel);

        HealthComponent health = engine.createComponent(HealthComponent.class);
        health.maxHealth = 100;
        health.health = 100;
        pawn.add(health);

        CircleCollisionComponent collision = engine.createComponent(CircleCollisionComponent.class);
        collision.circle.radius = 14;
        pawn.add(collision);

        // set color later?
        return pawn;
    }

    private static Entity CreateObjectAt(Engine engine, Vector2 position){
        Entity entity = engine.createEntity();

        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        transformComponent.position.set(position);
        entity.add(transformComponent);
        return entity;
    }

    public static Entity CreateBullet(Engine engine, Vector2 origin, Vector2 direction, TeamComponent teamComponent, float maxDistance, WeaponComponent weaponComponent){
        Entity bullet = CreateObjectAt(engine, origin);



        BulletComponent bulletComponent = engine.createComponent(BulletComponent.class);
        bulletComponent.direction.set(direction);
        bulletComponent.startPosition.set(origin);
        bulletComponent.maxDistance = maxDistance + MathUtils.random(12)  -6;
        bulletComponent.damage = weaponComponent.damage;
        bullet.add(bulletComponent);

        TeamComponent team = engine.createComponent(TeamComponent.class);
        team.teamName = teamComponent.teamName;
        team.teamColor.set(teamComponent.teamColor);
        bullet.add(team);

        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        bullet.add(textureComponent);

        AssetInfoComponent assetInfoComponent = engine.createComponent(AssetInfoComponent.class);
        assetInfoComponent.textureName = "bullet";
        bullet.add(assetInfoComponent);

        VelocityComponent vel = engine.createComponent(VelocityComponent.class);
        bullet.add(vel);

        // For now shrink by scale
        TransformComponent transformComponent = Mapper.TRANS_COMP.get(bullet);
        transformComponent.scale.set(.5f, .5f);
        transformComponent.zLevel = 2;

        return bullet;

    }

    public static Entity CreateStatic(Engine engine, Vector2 position, String texture){
        Entity fixed = CreateObjectAt(engine, position);

        AssetInfoComponent assetInfo = engine.createComponent(AssetInfoComponent.class);
        assetInfo.textureName = texture;

        fixed.add(assetInfo);

        return fixed;
    }

    public static Entity CreateSplash(Engine engine, Vector2 position, TeamComponent teamComponent){
        Entity splash = CreateObjectAt(engine, position);


        AssetInfoComponent assetInfo = engine.createComponent(AssetInfoComponent.class);
        int rnd = MathUtils.random(2) + 1;
        switch (rnd){
            case 1:
                assetInfo.textureName = "splat01";
                break;
            case 2:
                assetInfo.textureName = "splat02";
                break;
            case 3:
                assetInfo.textureName = "splat03";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rnd);
        }
        splash.add(assetInfo);

        SplashComponent splashComponent = engine.createComponent(SplashComponent.class);
        splashComponent.xScaleMultiplier = MathUtils.random() * .2f + .4f;
        splashComponent.yScaleMultiplier = MathUtils.random() * .2f + .4f;

        splash.add(splashComponent);

        TransformComponent trans = Mapper.TRANS_COMP.get(splash);
        trans.scale.set(0, 0);
        trans.angle = MathUtils.random(360);
        trans.zLevel = 1;
        splash.add(trans);

        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        textureComponent.alpha = .4f;
        splash.add(textureComponent);

        TeamComponent team = engine.createComponent(TeamComponent.class);
        team.teamName = teamComponent.teamName;
        team.teamColor.set(teamComponent.teamColor);
        splash.add(team);

        /*
        PaintComponent paintComponent = engine.createComponent(PaintComponent.class);
        paintComponent.color.set(color);
        splash.add(paintComponent);
         */


        return splash;
    }

    public static Entity CreateGun(Engine engine){
        Entity weaponEntity = engine.createEntity();
        WeaponComponent weapon = engine.createComponent(WeaponComponent.class);
        weapon.rateOfFire = .5f;
        weapon.spreadAngle = 5f;
        weapon.maxDistance = 256;
        weapon.textureString = "pistol";
        weapon.damage = 12;
        weaponEntity.add(weapon);

        return weaponEntity;
    }

    public static Entity CreateMachineGun(Engine engine){
        Entity weaponEntity = engine.createEntity();
        WeaponComponent weapon = engine.createComponent(WeaponComponent.class);
        weapon.rateOfFire = .15f;
        weapon.spreadAngle = 10f;
        weapon.maxDistance = 256;
        weapon.textureString = "machinegun";
        weapon.damage = 10;
        weaponEntity.add(weapon);

        return weaponEntity;
    }

    public static Entity CreateRifle(Engine engine){
        Entity weaponEntity = engine.createEntity();
        WeaponComponent weapon = engine.createComponent(WeaponComponent.class);
        weapon.rateOfFire = .3f;
        weapon.spreadAngle = 5f;
        weapon.maxDistance = 512;
        weapon.textureString = "rifle";
        weapon.damage = 24;
        weaponEntity.add(weapon);

        return weaponEntity;
    }

    public static Entity CreatePickup(Engine engine, TextureAtlas sprites, Vector2 position, Entity item){
        Entity crate = engine.createEntity();

        TransformComponent trans = engine.createComponent(TransformComponent.class);
        trans.position.set(position);
        trans.angle = MathUtils.random(0, 90);
        trans.scale.set(.7f, .7f);
        crate.add(trans);

        TextureComponent tex = engine.createComponent(TextureComponent.class);
        tex.texture = sprites.findRegion("crate");
        crate.add(tex);

        PickupComponent pickup = engine.createComponent(PickupComponent.class);
        pickup.item = item;
        crate.add(pickup);

        CircleCollisionComponent col = engine.createComponent(CircleCollisionComponent.class);
        col.circle.set(position, 20);
        crate.add(col);

        return crate;
    }
}
