package ru.geekbrains.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;

public class EnemyShip extends Ship {
    public EnemyShip(BulletPool bulletPool, Rect worldBounds, Sound sound) {
        this.sound = sound;
        this.worldBounds = worldBounds;
        this.bulletPool = bulletPool;
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        v = new Vector2(0, -1f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (getBottom() < worldBounds.getBottom()){
            destroy();
        }
        pos.add(v);
        bulletPos.set(pos.x, pos.y - getHalfHeight());
    }

    public void set(
            TextureRegion[] regions,
            float speed,
            TextureRegion bulletRegion,
            float bulletHeight,
            Vector2 bulletV,
            int damage,
            float reloadInterval,
            float height,
            int hp){
        this.regions = regions;
        this.v.setLength(speed);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(bulletV);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        setHeightProportion(height);
        this.hp = hp;
    }
}
