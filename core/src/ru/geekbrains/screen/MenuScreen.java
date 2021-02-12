package ru.geekbrains.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.sprites.Background;
import ru.geekbrains.base.sprites.Logo;
import ru.geekbrains.math.Rect;

public class MenuScreen extends BaseScreen  {
    private Texture bg;
    private Texture lg;
    private Vector2 touch;
    private Background background;
    private Logo logo;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.jpg");
        lg = new Texture("textures/icon.jpg");
        background = new Background(bg);
        logo = new Logo(lg);
        touch = new Vector2();
    }

    @Override
    public void render(float delta) {
        batch.begin();
        background.draw(batch);
        logo.draw(batch);
        logo.update(delta);
        batch.end();
    }

    @Override
    public void dispose() {
        lg.dispose();
        bg.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button){
        logo.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        logo.resize(worldBounds);
    }
}
