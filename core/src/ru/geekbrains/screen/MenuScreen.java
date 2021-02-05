package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen  {
    private Texture img;
    private Vector2 touch;
    private Vector2 v;
    private Vector2 point;

    @Override
    public void show() {
        super.show();
        img = new Texture("icon.jpg");
        touch = new Vector2();
        v = new Vector2();
        point = new Vector2();
    }

    private void destination(Vector2 point){
        v.x = Float.compare(point.x, touch.x);
        v.y = Float.compare(point.y, touch.y);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (point.x != touch.x || point.y != touch.y) {
            destination(point);
            touch.add(v);
        }
        batch.draw(img, touch.x, touch.y);
        batch.end();
    }

    @Override
    public void dispose() {
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        point.x = screenX;
        point.y = Gdx.graphics.getHeight() - screenY;
        return false;
    }
}
