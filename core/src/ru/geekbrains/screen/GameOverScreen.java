package ru.geekbrains.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.ButtonNewGame;
import ru.geekbrains.sprites.GameOver;
import ru.geekbrains.sprites.Star;

public class GameOverScreen extends BaseScreen {
    private final int STAR_COUNT = 128;
    private GameOver gameOver;
    private Game game;
    private ButtonNewGame newGame;
    private TextureAtlas mainAtlas;
    private Texture bg;
    private Star[] stars;
    private Background background;

    public GameOverScreen(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.jpg");
        background = new Background(bg);
        mainAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++)
            stars[i] = new Star(mainAtlas);
        gameOver = new GameOver(mainAtlas);
        newGame = new ButtonNewGame(mainAtlas, game);
    }

    @Override
    public void dispose() {
        bg.dispose();
        super.dispose();
    }

    @Override
    public void render(float delta) {
        for (Star star : stars)
            star.update(delta);
        batch.begin();
        background.draw(batch);
        for (Star star : stars)
            star.draw(batch);
        gameOver.draw(batch);
        newGame.draw(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Star star : stars)
            star.resize(worldBounds);
        gameOver.resize(worldBounds);
        newGame.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        newGame.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        newGame.touchUp(touch, pointer, button);
        return false;
    }
}
