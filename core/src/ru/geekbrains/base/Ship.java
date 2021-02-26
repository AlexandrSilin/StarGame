package ru.geekbrains.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprites.Bullet;
import ru.geekbrains.sprites.Explosion;

public class Ship extends Sprite{
    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;

    protected Rect worldBounds;
    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
    protected TextureRegion bulletRegion;
    protected Sound sound;

    protected Vector2 v;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;

    protected float reloadInterval;
    protected float reloadTimer;
    protected float speed;
    protected float bulletHeight;
    private float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;
    protected int damage;
    protected int hp;

    protected boolean moveLeft = false;
    protected boolean moveRight = false;
    protected boolean onField = false;

    public Ship(){

    }

    public Ship(TextureRegion region, int rows, int cols, int frames){
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval && onField) {
            reloadTimer = 0f;
            shoot();
        }
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL)
            frame = 0;
    }

    public int getDamage(){
        return damage;
    }

    public void damage(int damage){
        hp -= damage;
        frame = 1;
        damageAnimateTimer = 0;
        if (hp <= 0) {
            hp = 0;
            destroy();
        }
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, damage);
        sound.setVolume(sound.play(), 0.1f);
    }

    private void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }
}