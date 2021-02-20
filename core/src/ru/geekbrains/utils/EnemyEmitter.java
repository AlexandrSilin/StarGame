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
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 3f;
    private static final float ENEMY_SPEED = 0.0035f;

    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final int ENEMY_SMALL_HP = 1;

    private final Vector2 enemySmallBulletV = new Vector2(0, -0.3f);
    private TextureRegion[] enemySmallRegions;

    private Rect wordBounds;
    private TextureRegion bulletRegion;
    private EnemyPool enemyPool;

    private final float generateInterval = 4f;
    private float generateTimer;

    public EnemyEmitter(TextureAtlas atlas, Rect wordBounds, EnemyPool enemyPool) {
        this.wordBounds = wordBounds;
        this.enemyPool = enemyPool;
        enemySmallRegions = Regions.split(atlas.findRegion("enemy0"), 1, 2 ,2);
        bulletRegion = atlas.findRegion("bulletEnemy");
    }

    public void generate(float delta){
        generateTimer += delta;
        if (generateTimer >= generateInterval){
            generateTimer = 0f;
            EnemyShip enemyShip = enemyPool.obtain();
            enemyShip.set(enemySmallRegions,
                    ENEMY_SPEED,
                    bulletRegion,
                    ENEMY_SMALL_BULLET_HEIGHT,
                    enemySmallBulletV,
                    ENEMY_SMALL_BULLET_DAMAGE,
                    ENEMY_SMALL_RELOAD_INTERVAL,
                    ENEMY_SMALL_HEIGHT,
                    ENEMY_SMALL_HP);
            enemyShip.pos.x = Rnd.nextFloat(
                    wordBounds.getLeft() + enemyShip.getHalfWidth(),
                    wordBounds.getRight() - enemyShip.getHalfWidth());
            enemyShip.pos.y = wordBounds.getTop();
        }
    }
}
