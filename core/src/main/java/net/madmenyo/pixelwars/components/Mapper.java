package net.madmenyo.pixelwars.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.utils.Array;

public class Mapper {
    public static final ComponentMapper<TransformComponent> TRANS_COMP = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<VelocityComponent> VEL_COMP = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<TextureComponent> TEX_COMP = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<PawnComponent> PAWN_COMP = ComponentMapper.getFor(PawnComponent.class);
    public static final ComponentMapper<PlayerComponent> PLAYER_COMP = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<BulletComponent> BULLET_COMP = ComponentMapper.getFor(BulletComponent.class);
    public static final ComponentMapper<AssetInfoComponent> ASSET_COMP = ComponentMapper.getFor(AssetInfoComponent.class);
    public static final ComponentMapper<SplashComponent> SPLASH_COMP = ComponentMapper.getFor(SplashComponent.class);

    //public static final ComponentMapper<PaintComponent> PAINT_COMP = ComponentMapper.getFor(PaintComponent.class);
    public static final ComponentMapper<TeamComponent> TEAM_COMP = ComponentMapper.getFor(TeamComponent.class);
    public static final ComponentMapper<ControlTileComponent> CTRL_TILE_COMP = ComponentMapper.getFor(ControlTileComponent.class);
    public static final ComponentMapper<ControlPointComponent> CTRL_POINT_COMP = ComponentMapper.getFor(ControlPointComponent.class);
    public static final ComponentMapper<ControlTileChangeComponent> CTRL_TILE_CHANGE_COMP = ComponentMapper.getFor(ControlTileChangeComponent.class);
    public static final ComponentMapper<ControlTileTurretComponent> CTRL_TURRET_COMP = ComponentMapper.getFor(ControlTileTurretComponent.class);
    public static final ComponentMapper<AiComponent> AI_COMP = ComponentMapper.getFor(AiComponent.class);
    public static final ComponentMapper<WeaponComponent> WEAPON_COMP = ComponentMapper.getFor(WeaponComponent.class);
    public static final ComponentMapper<HealthComponent> HEALTH_COMP = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<CircleCollisionComponent> CIRCLE_COMP = ComponentMapper.getFor(CircleCollisionComponent.class);
    public static final ComponentMapper<PickupComponent> PICKUP_COMP = ComponentMapper.getFor(PickupComponent.class);
}
