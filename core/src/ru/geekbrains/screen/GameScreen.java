package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
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

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.jpg");
        background = new Background(bg);
        mainAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++)
            stars[i] = new Star(mainAtlas);
        ship = new Ship(mainAtlas.findRegion("main_ship"));
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
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == LEFT)
            ship.setMoveLeft(true);
        if (keycode == RIGHT)
            ship.setMoveRight(true);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == LEFT)
            ship.setMoveLeft(false);
        if (keycode == RIGHT)
            ship.setMoveRight(false);
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

    private void update(float delta){
        for (Star star : stars)
            star.update(delta);
        ship.update(delta);
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for (Star star : stars)
            star.draw(batch);
        ship.draw(batch);
        batch.end();
    }
}
