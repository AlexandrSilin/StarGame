package ru.geekbrains.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;

public class MainShip extends Ship {

    private final float MARGIN = 0.02f;
    private final float HEIGHT = 0.15f;

    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        bulletHeight = 0.01f;
        damage = 1;
        reloadInterval = 0.15f;
        speed = 0.005f;
        hp = 100;
        v = new Vector2(1, 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() - getHeight());
        this.worldBounds = worldBounds;
    }

    private void arrive(){
        if (getBottom() < worldBounds.getBottom() + MARGIN) {
            pos.y += speed;
        } else onField = true;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (!onField)
            arrive();
        if (moveLeft && worldBounds.getLeft() < this.getLeft())
            pos.sub(v.setLength(speed));
        if (moveRight && worldBounds.getRight() > this.getRight())
            pos.add(v.setLength(speed));
        bulletPos.set(pos.x, pos.y + getHalfHeight());
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

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom()
        );
    }

    private void setMoveLeft(boolean move){
        moveLeft = move;
    }

    private void setMoveRight(boolean move){
        moveRight = move;
    }
}
