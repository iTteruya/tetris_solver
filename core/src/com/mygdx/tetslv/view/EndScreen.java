package com.mygdx.tetslv.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.tetslv.TetriSolver;

public class EndScreen extends AbstractScreen {

    private Stage stage;
    private Image image;
    private int score;
    private boolean solverStatus;

    EndScreen(TetriSolver solver, int score, boolean solverStatus) {
        super(solver);
        this.score = score;
        this.solverStatus = solverStatus;
        stage = new Stage(new ExtendViewport(640, 480));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Texture txt = new Texture(Gdx.files.internal("grey.jpg"));
        image = new Image(txt);
        stage.addActor(image);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("shade_skin\\uiskin.json"));

        TextButton menu = new TextButton(null, skin);
        menu.setColor(Color.VIOLET);
        menu.setLabel(new Label("Menu", skin.get("title-plain", Label.LabelStyle.class)));
        menu.getLabel().setAlignment(Align.center);

        menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                solver.setScreen(new MenuScreen(solver));
            }
        });

        TextButton restart = new TextButton(null, skin);
        restart.setColor(Color.VIOLET);
        restart.setLabel(new Label("Restart", skin.get("title-plain", Label.LabelStyle.class)));
        restart.getLabel().setAlignment(Align.center);
        restart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (solverStatus) solver.setScreen(new SolverSettingsScreen(solver));
                else solver.setScreen(new GameScreen(solver, 0, false));
            }
        });


        Label gameOver = new Label("Your Score: " + score, skin, "title-plain");
        gameOver.setAlignment(Align.center);

        table.add(gameOver);
        table.row().pad(10,0,0,10);
        table.add(restart).width(stage.getWidth() / 3f).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.row().pad(10,0,0,10).row();
        table.add(menu).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.row();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        image.setSize(stage.getWidth(), stage.getHeight());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void dispose() {
        image.remove();
        stage.dispose();
    }
}
