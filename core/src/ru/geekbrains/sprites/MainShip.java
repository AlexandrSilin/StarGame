package ru.geekbrains.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;

public class Ship extends Sprite {
    private final float SPEED = 0.005f;
    private final float MARGIN = 0.02f;
    private final float HEIGHT = 0.15f;

    private boolean moveLeft = false;
    private boolean moveRight = false;


    private float reloadInterval;
    private float reloadTimer;

    private final Vector2 v = new Vector2(1, 0);
    private Rect worldBounds;
    private BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private Vector2 bulletV;
    private Vector2 bulletPos;
    private Sound sound;

    public Ship(TextureAtlas atlas, BulletPool bulletPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        reloadInterval = 0.15f;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + MARGIN);
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
        if (moveLeft && worldBounds.getLeft() < this.getLeft())
            pos.sub(v.setLength(SPEED));
        if (moveRight && worldBounds.getRight() > this.getRight())
            pos.add(v.setLength(SPEED));
    }

    public boolean keyDown(int keycode) {
        if (keycode == LEFT || keycode == A)
            setMoveLeft(true);
        if (keycode == RIGHT || keycode == D)
            setMoveRight(true);
        return false;
    }

    public boolean keyUp(int keycode) {
        if (keycode == LEFT || keycode == A)
            setMoveLeft(false);
        if (keycode == RIGHT || keycode == D)
            setMoveRight(false);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x > worldBounds.pos.x)
            setMoveRight(true);
        else
            setMoveLeft(true);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (moveRight)
            setMoveRight(false);
        if (moveLeft)
            setMoveLeft(false);
        return false;
    }

    private void setMoveLeft(boolean move){
        moveLeft = move;
    }

    private void setMoveRight(boolean move){
        moveRight = move;
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        bullet.set(this, bulletRegion, bulletPos, bulletV, 0.01f, worldBounds, 1);
        sound.setVolume(sound.play(), 0.05f);
    }
}
