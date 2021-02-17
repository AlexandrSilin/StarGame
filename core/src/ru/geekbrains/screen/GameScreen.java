package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.Ship;
import ru.geekbrains.sprites.Star;

import static com.badlogic.gdx.Input.Keys.*;

public class GameScreen extends BaseScreen {
    private TextureAtlas mainAtlas;
    private Texture bg;
    private Background background;
    private final int STAR_COUNT = 128;
    private Star[] stars;
    private Ship ship;
    private BulletPool bulletPool;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.jpg");
        background = new Background(bg);
        mainAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++)
            stars[i] = new Star(mainAtlas);
        bulletPool = new BulletPool();
        ship = new Ship(mainAtlas, bulletPool);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Star star : stars)
            star.resize(worldBounds);
        ship.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        mainAtlas.dispose();
        bulletPool.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        ship.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        ship.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        ship.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        ship.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float delta){
        for (Star star : stars)
            star.update(delta);
        ship.update(delta);
        bulletPool.updateActiveSprites(delta);
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for (Star star : stars)
            star.draw(batch);
        ship.draw(batch);
        bulletPool.drawActiveSprites(batch);
        batch.end();
    }
}
