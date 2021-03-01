package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

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
import ru.geekbrains.sprites.TrackingStar;
import ru.geekbrains.utils.EnemyEmitter;
import ru.geekbrains.utils.Font;

public class GameScreen extends BaseScreen {
    private int kills;
    private float move;
    private float pointx;

    private static final int STAR_COUNT = 128;

    private static final float FONT_SIZE = 0.02f;
    private static final float MARGIN = 0.01f;

    private static final String FRAGS = "Kills: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private TextureAtlas mainAtlas;
    private Texture bg;

    private Background background;
    private TrackingStar[] stars;
    private MainShip mainShip;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private Music music;
    private Sound enemyBulletSound;
    private Sound explosionSound;

    private EnemyEmitter enemyEmitter;

    private State state;

    private GameOver gameOver;
    private ButtonNewGame newGame;

    private Font font;

    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

    private Texture gameOverTexture;
    private Texture newGameTexture;

    public enum State{PLAYING, GAME_OVER}

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
        bulletPool = new BulletPool();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(mainAtlas, explosionSound);
        mainShip = new MainShip(mainAtlas, bulletPool, explosionPool);
        pointx = mainShip.pos.x;
        stars = new TrackingStar[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++)
            stars[i] = new TrackingStar(mainAtlas);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, enemyBulletSound);
        enemyEmitter = new EnemyEmitter(mainAtlas, worldBounds, enemyPool);
        state = State.PLAYING;
        gameOverTexture = new Texture("textures/gameOver.png");
        gameOverTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        gameOver = new GameOver(gameOverTexture);
        newGameTexture = new Texture("textures/newGame.png");
        newGameTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        newGame = new ButtonNewGame(newGameTexture, this);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
    }

    public void startNewGame(){
        state = State.PLAYING;
        mainShip.startNewGame();
        bulletPool.freeAllActiveSprites();
        enemyPool.freeAllActiveSprites();
        explosionPool.freeAllActiveSprites();
        kills = 0;
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
        for (TrackingStar star : stars)
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
        font.dispose();
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
        if (pointx < mainShip.pos.x){
            move = 0.5f;
            pointx = mainShip.pos.x;
        } else if (pointx > mainShip.pos.x){
            move = -0.5f;
            pointx = mainShip.pos.x;
        } else
            move = 0;
        for (TrackingStar star : stars)
            star.update(delta, move);
        if (mainShip.isDestroyed()) {
            state = State.GAME_OVER;
        }
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, kills);
        }
    }

    private void draw(){
        batch.begin();
        background.draw(batch);
        printInfo();
        for (TrackingStar star : stars)
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

    private void printInfo(){
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(kills),
                worldBounds.getLeft() + MARGIN, worldBounds.getTop() - MARGIN);
        font.draw(batch, sbHp.append(HP).append(mainShip.getHp()),
                worldBounds.pos.x, worldBounds.getTop() - MARGIN, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()),
                worldBounds.getRight(), worldBounds.getTop() - MARGIN, Align.right);
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
                    if (enemyShip.isDestroyed())
                        kills++;
                }
            }
        }
        if (mainShip.isDestroyed())
            state = State.GAME_OVER;
    }
}
