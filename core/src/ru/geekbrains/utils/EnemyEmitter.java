package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprites.EnemyShip;

public class EnemyEmitter {
    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 1f;
    private static final float ENEMY_SMALL_SPEED = 0.0025f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final int ENEMY_SMALL_HP = 1;

    private static final float ENEMY_MEDIUM_HEIGHT = 0.15f;
    private static final float ENEMY_MEDIUM_BULLET_HEIGHT = 0.02f;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 2f;
    private static final float ENEMY_MEDIUM_SPEED = 0.0015f;
    private static final int ENEMY_MEDIUM_BULLET_DAMAGE = 5;
    private static final int ENEMY_MEDIUM_HP = 5;

    private static final float ENEMY_BIG_HEIGHT = 0.2f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 4f;
    private static final float ENEMY_BIG_SPEED = 0.0005f;
    private static final int ENEMY_BIG_BULLET_DAMAGE = 10;
    private static final int ENEMY_BIG_HP = 10;

    private final Vector2 enemySmallBulletV = new Vector2(0, -0.3f);
    private final Vector2 enemyMediumBulletV = new Vector2(0, -0.25f);
    private final Vector2 enemyBigBulletV = new Vector2(0, -0.2f);

    private TextureRegion[] enemySmallRegions;
    private TextureRegion[] enemyMediumRegions;
    private TextureRegion[] enemyBigRegions;

    private Rect worldBounds;
    private TextureRegion bulletRegion;
    private EnemyPool enemyPool;

    private final float generateInterval = 4f;
    private float generateTimer;

    public EnemyEmitter(TextureAtlas atlas, Rect worldBounds, EnemyPool enemyPool) {
        this.worldBounds = worldBounds;
        this.enemyPool = enemyPool;
        enemySmallRegions = Regions.split(atlas.findRegion("enemy0"), 1, 2 ,2);
        enemyMediumRegions = Regions.split(atlas.findRegion("enemy1"), 1, 2 ,2);
        enemyBigRegions = Regions.split(atlas.findRegion("enemy2"), 1, 2 ,2);
        bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta){
        generateTimer += delta;
        if (generateTimer >= generateInterval){
            generateTimer = 0f;
            EnemyShip enemyShip = enemyPool.obtain();
            float enemyType = (float)Math.random();
            if (enemyType < 0.5f) {
                enemyShip.set(enemySmallRegions,
                        ENEMY_SMALL_SPEED,
                        bulletRegion,
                        ENEMY_SMALL_BULLET_HEIGHT,
                        enemySmallBulletV,
                        ENEMY_SMALL_BULLET_DAMAGE,
                        ENEMY_SMALL_RELOAD_INTERVAL,
                        ENEMY_SMALL_HEIGHT,
                        ENEMY_SMALL_HP);
            } else if (enemyType < 0.8f) {
                    enemyShip.set(enemyMediumRegions,
                        ENEMY_MEDIUM_SPEED,
                        bulletRegion,
                        ENEMY_MEDIUM_BULLET_HEIGHT,
                        enemyMediumBulletV,
                        ENEMY_MEDIUM_BULLET_DAMAGE,
                        ENEMY_MEDIUM_RELOAD_INTERVAL,
                        ENEMY_MEDIUM_HEIGHT,
                        ENEMY_MEDIUM_HP);
            } else {
                    enemyShip.set(enemyBigRegions,
                        ENEMY_BIG_SPEED,
                        bulletRegion,
                        ENEMY_BIG_BULLET_HEIGHT,
                        enemyBigBulletV,
                        ENEMY_BIG_BULLET_DAMAGE,
                        ENEMY_BIG_RELOAD_INTERVAL,
                        ENEMY_BIG_HEIGHT,
                        ENEMY_BIG_HP);
                }
                enemyShip.pos.x = Rnd.nextFloat(
                        worldBounds.getLeft() + enemyShip.getHalfWidth(),
                        worldBounds.getRight() - enemyShip.getHalfWidth());
                enemyShip.pos.y = worldBounds.getTop() + enemyShip.getHeight();
        }
    }
}
