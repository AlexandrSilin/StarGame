package ru.geekbrains.base.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;

public class Logo extends Sprite {
    private Vector2 v;
    private Vector2 touch;
    private Vector2 tmp;
    private final float V_LEN = 0.01f;

    public Logo(Texture texture) {
        super(new TextureRegion(texture));
        this.setSize(1f, 1f);
        this.scale = 0.1f;
        v = new Vector2();
        touch = new Vector2();
        tmp = new Vector2();
    }

    @Override
    public void update(float delta) {
        tmp.set(touch);
        if (tmp.sub(pos).len() > V_LEN)
            pos.add(v);
        else
            pos.set(touch);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        this.touch = touch;
        v.set(touch.cpy().sub(this.pos)).setLength(V_LEN);
        return false;
    }
}
