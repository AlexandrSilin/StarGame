package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Ship extends Sprite {
    private final float SPEED = 0.005f;
    private final float MARGIN = 0.02f;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private Vector2 v;
    private Rect worldBounds;

    public Ship(TextureRegion texture) {
        super(texture.split(texture.getRegionWidth() / 2, texture.getRegionHeight())[0][0]);
        v = new Vector2(1, 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        setSize(0.2f, 0.2f);
        setBottom(worldBounds.getBottom() + MARGIN);
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (moveLeft && worldBounds.getLeft() < this.getLeft())
            pos.sub(v.setLength(SPEED));
        if (moveRight && worldBounds.getRight() > this.getRight())
            pos.add(v.setLength(SPEED));
    }

    public void setMoveLeft(boolean move){
        moveLeft = move;
    }

    public void setMoveRight(boolean move){
        moveRight = move;
    }
}
