package com.mygdx.tetslv.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Screen;
import com.mygdx.tetslv.TetrisSolver;

public abstract  class AbstractScreen extends ApplicationAdapter implements Screen {

    TetrisSolver solver;

    AbstractScreen(TetrisSolver solver) {
        this.solver = solver;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

}
