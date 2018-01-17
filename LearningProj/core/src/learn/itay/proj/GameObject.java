package learn.itay.proj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class GameObject implements Disposable {
    private float x;
    private float y;
    private Texture texture;

    public GameObject(String texturePath, float x, float y) {
        texture = new Texture(texturePath);
        this.x = x;
        this.y = y;
    }

    public void updatePosition() {
        float speed = 2.5f;

        x += GameInput.KeyForce.x * speed;
        y += GameInput.KeyForce.y * speed;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}