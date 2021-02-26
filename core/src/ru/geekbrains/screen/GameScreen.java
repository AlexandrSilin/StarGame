package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.Bullet;
import ru.geekbrains.sprites.ButtonNewGame;
import ru.geekbrains.sprites.EnemyShip;
import ru.geekbrains.sprites.GameOver;
import ru.geekbrains.sprites.MainShip;
import ru.geekbrains.sprites.Star;
import ru.geekbrains.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {
    private final int STAR_COUNT = 128;
    private TextureAtlas mainAtlas;
    private Texture bg;
    private Background background;
    private Star[] stars;
    private MainShip mainShip;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private Music music;
    private Sound enemyBulletSound;
    private EnemyEmitter enemyEmitter;
    private Sound explosionSound;
    private State state;
    private GameOver gameOver;
    private ButtonNewGame newGame;

    private enum State{PLAYING, GAME_OVER}

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        enemyBulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        mainAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        music.setLooping(true);
        music.play();
        bg = new Texture("textures/bg.jpg");
        background = new Background(bg);
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++)
            stars[i] = new Star(mainAtlas);
        bulletPool = new BulletPool();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(mainAtlas, explosionSound);
        mainShip = new MainShip(mainAtlas, bulletPool, explosionPool);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, enemyBulletSound);
        enemyEmitter = new EnemyEmitter(mainAtlas, worldBounds, enemyPool);
        state = State.PLAYING;
        gameOver = new GameOver(mainAtlas);
        newGame = new ButtonNewGame(mainAtlas, this);
    }

    public void startNewGame(){
        state = State.PLAYING;
        mainShip.startNewGame();
        bulletPool.freeAllActiveSprites();
        enemyPool.freeAllActiveSprites();
        explosionPool.freeAllActiveSprites();
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Star star : stars)
            star.resize(worldBounds);
        mainShip.resize(worldBounds);
        newGame.resize(worldBounds);
        gameOver.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        mainAtlas.dispose();
        bulletPool.dispose();
        music.dispose();
        enemyBulletSound.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        explosionSound.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING)
            mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING)
            mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING)
            mainShip.touchDown(touch, pointer, button);
        if (state == State.GAME_OVER)
            newGame.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING)
            mainShip.touchUp(touch, pointer, button);
        if (state == State.GAME_OVER)
            newGame.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float delta){
        for (Star star : stars)
            star.update(delta);
        if (mainShip.isDestroyed()) {
            state = State.GAME_OVER;
        }
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta);
        }
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        for (Star star : stars)
            star.draw(batch);
        explosionPool.drawActiveSprites(batch);
        if (state == State.PLAYING) {
            mainShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        }
        if (state == State.GAME_OVER){
            gameOver.draw(batch);
            newGame.draw(batch);
        }
        batch.end();
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
    }

    private void checkCollisions() {
        if (state == State.GAME_OVER)
            return;
        List<EnemyShip> enemyShipList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (EnemyShip enemyShip : enemyShipList) {
            if (enemyShip.isDestroyed())
                continue;
            float minDist = enemyShip.getHalfWidth() + mainShip.getHalfWidth();
            if (enemyShip.pos.dst(mainShip.pos) < minDist) {
                enemyShip.destroy();
                mainShip.damage(enemyShip.getDamage());
            }
        }
        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed())
                continue;
            if (bullet.getOwner() != mainShip) {
                if (mainShip.isBulletCollision(bullet)) {
                    mainShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
                continue;
            }
            for (EnemyShip enemyShip : enemyShipList) {
                if (enemyShip.isDestroyed())
                    continue;
                if (enemyShip.isBulletCollision(bullet)) {
                    enemyShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
        }
    }
}
