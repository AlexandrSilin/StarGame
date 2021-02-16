package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;

public class Ship extends Sprite {
    private final float SPEED = 0.005f;
    private final float MARGIN = 0.02f;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private static float HEIGHT = 0.15f;
    private Vector2 v;
    private Rect worldBounds;

    public Ship(TextureRegion texture) {
        super(texture.split(texture.getRegionWidth() / 2, texture.getRegionHeight())[0][0]);
        v = new Vector2(1, 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setBottom(worldBounds.getBottom() + MARGIN);
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
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
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    private void setMoveLeft(boolean move){
        moveLeft = move;
    }

    private void setMoveRight(boolean move){
        moveRight = move;
    }
}
