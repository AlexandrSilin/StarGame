package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Title extends Sprite {
    private static final float HEIGHT = 0.18f;
    private static final float TOP = 0.4f;

    public Title(Texture texture){
        super(new TextureRegion(texture));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setTop(TOP);
    }
}
