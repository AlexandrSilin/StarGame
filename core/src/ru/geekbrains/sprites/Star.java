package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Star extends Sprite {
    private Rect worldBounds;
    protected final Vector2 v;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        setHeightProportion(Rnd.nextFloat(0.005f, 0.015f));
        v = new Vector2(Rnd.nextFloat(-0.01f, 0.01f), getHeight() * -7);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        checkBounds();
    }

    public void checkBounds(){
        if (getLeft() + getWidth() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
        if (getRight() - getWidth() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getBottom() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(x, y);
    }
}
