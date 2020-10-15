package com.mygdx.tetslv.view;

import com.badlogic.gdx.Game;
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

public class MenuScreen extends AbstractScreen {

    private Stage stage;
    private Image image;
    private boolean solverON = false;

    public MenuScreen(TetriSolver solver) {
        super(solver);
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
        TextButton start = new TextButton(null, skin);
        start.setLabel(new Label("Start", skin.get("title-plain", Label.LabelStyle.class)));
        start.setColor(Color.VIOLET);
        start.getLabel().setAlignment(Align.center);
        TextButton solverStatus = new TextButton(null, skin, "toggle");
        solverStatus.setLabel(new Label("Solver", skin.get("title-plain", Label.LabelStyle.class)));
        solverStatus.isTransform();
        solverStatus.getLabel().setAlignment(Align.center);
        solverStatus.setColor(Color.VIOLET);

        solverStatus.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                solverON = true;
            }
        });

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if ((solverON)) {
                    solver.setScreen(new SolverSettingsScreen(solver));
                } else {
                    solver.setScreen(new GameScreen(solver));
                }
            }
        });


        table.add(start).width(stage.getWidth() / 3f).height(stage.getHeight() * 0.1f).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(solverStatus).height(stage.getHeight() * 0.1f).fillX().uniformX();


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        image.setSize(stage.getWidth(), stage.getHeight());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
