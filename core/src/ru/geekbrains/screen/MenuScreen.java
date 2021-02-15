package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.ButtonExit;
import ru.geekbrains.sprites.ButtonPlay;
import ru.geekbrains.sprites.Star;
import ru.geekbrains.math.Rect;

public class MenuScreen extends BaseScreen  {
    private Texture bg;
    private Vector2 touch;
    private Background background;
    private Star[] stars;
    private final int STAR_COUNT = 256;
    private TextureAtlas menuAtlas;
    private final Game game;
    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.jpg");
        menuAtlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        background = new Background(bg);
        touch = new Vector2();
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++)
            stars[i] = new Star(menuAtlas);
        buttonExit = new ButtonExit(menuAtlas);
        buttonPlay = new ButtonPlay(menuAtlas, game);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        menuAtlas.dispose();
        bg.dispose();
        super.dispose();
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch, pointer, button);
        buttonPlay.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button){
        buttonExit.touchDown(touch, pointer, button);
        buttonPlay.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Star star : stars)
            star.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
    }

    private void update(float delta){
        for (Star star : stars)
            star.update(delta);
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for (Star star : stars)
            star.draw(batch);
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }
}
