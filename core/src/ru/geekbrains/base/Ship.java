package ru.geekbrains.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.sprites.Bullet;

public class Ship extends Sprite{
    protected Rect worldBounds;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;
    protected Sound sound;

    protected Vector2 v;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;

    protected float reloadInterval;
    protected float reloadTimer;
    protected float speed;
    protected float bulletHeight;

    protected int damage;
    protected int hp;

    protected boolean moveLeft = false;
    protected boolean moveRight = false;

    public Ship(){

    }

    public Ship(TextureRegion region, int rows, int cols, int frames){
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }

    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, damage);
        sound.setVolume(sound.play(), 0.05f);
    }
}
