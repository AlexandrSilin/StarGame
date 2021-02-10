package ru.geekbrains.base.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;

public class Logo extends Sprite {
    private Vector2 v;
    private Vector2 touch;
    private Vector2 tmp;

    public Logo(Texture texture) {
        super(new TextureRegion(texture));
        this.setSize(1f, 1f);
        this.scale = 0.1f;
        v = new Vector2();
        touch = new Vector2();
        tmp = new Vector2();
    }

    public void move() {
        tmp.set(touch);
        if (tmp.sub(pos).len() > Math.pow(this.getScale(), 2))
            this.pos.mulAdd(v, (float) Math.pow(this.getScale(), 2));
        else
            pos.set(touch);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.pos.set(worldBounds.pos);
        setHeightProportion(worldBounds.getHeight());
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        v.set(touch.cpy().sub(this.pos)).setLength(this.getScale());
        this.touch = touch;
        return false;
    }
}
