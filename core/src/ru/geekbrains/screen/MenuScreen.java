package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.ButtonExit;
import ru.geekbrains.sprites.ButtonPlay;
import ru.geekbrains.sprites.Star;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprites.Title;

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
    private Music music;
    private Title title;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.jpg");
        Texture t = new Texture("textures/title.png");
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        title = new Title(t);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
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
        music.dispose();
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
        title.resize(worldBounds);
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
        title.draw(batch);
        for (Star star : stars)
            star.draw(batch);
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }
}
