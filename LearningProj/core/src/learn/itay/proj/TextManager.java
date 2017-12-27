package learn.itay.proj;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextManager {
    public static BitmapFont bitmapFont = new BitmapFont();

    public static void draw(SpriteBatch batch, CharSequence text, float x, float y) {
        bitmapFont.draw(batch, text, x, y);
    }
}
