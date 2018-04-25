package com.itay347.finaldays.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.itay347.finaldays.FinalDays;
import com.itay347.finaldays.MyValues;

/**
 * The main menu screen
 */
public class MenuScreen extends ScreenAdapter {
    /**
     * A reference to the main game class
     */
    private FinalDays game;

    /**
     * The UI stage
     */
    private Stage stage;
    /**
     * The skin for the UI
     */
    private Skin skin;

    /**
     * Init the menu screen
     *
     * @param finalDays A reference to the game
     */
    public MenuScreen(FinalDays finalDays) {
        this.game = finalDays;
        game.getAssetManager().load(MyValues.LOGO_FILE, Texture.class);
        game.getAssetManager().load(MyValues.BACKGROUND_FILE, Texture.class);

        stage = new Stage(new StretchViewport(960, 540));
        skin = new Skin(Gdx.files.internal(MyValues.SKIN_FILE));

        Table table = new Table();
        table.setFillParent(true);
        table.align(Align.center);

        game.getAssetManager().finishLoadingAsset(MyValues.LOGO_FILE);
        final Image logo = new Image(new TextureRegion(game.getAssetManager().get(MyValues.LOGO_FILE, Texture.class)));
        final TextButton playButton = new TextButton("Start Game", skin, "default");
        final TextButton exitButton = new TextButton("Exit Game", skin, "default");

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(logo).size(850, 200).pad(20).row();
        table.add(playButton).size(500, 100).pad(20).row();
        table.add(exitButton).size(500, 100).pad(20);

        game.getAssetManager().finishLoadingAsset(MyValues.BACKGROUND_FILE);
        final Image background = new Image(new TextureRegion(game.getAssetManager().get(MyValues.BACKGROUND_FILE, Texture.class)));

        stage.addActor(background);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /**
     * Clears the resources when the MenuScreen is not used anymore
     */
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
