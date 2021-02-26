package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.BaseButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class ButtonNewGame extends BaseButton {
    private final GameScreen gameScreen;

    private static final float HEIGHT = 0.16f;
    private static final float TOP = -0.02f;

    public ButtonNewGame(Texture texture, GameScreen gameScreen) {
        super(new TextureRegion(texture));
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(HEIGHT);
        setTop(TOP);
    }

    @Override
    public void action() {
        gameScreen.startNewGame();
    }
}
