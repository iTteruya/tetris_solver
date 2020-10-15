package com.mygdx.tetslv.view;

import com.badlogic.gdx.Screen;
import com.mygdx.tetslv.TetriSolver;

public abstract  class AbstractScreen implements Screen {

    TetriSolver solver;

    AbstractScreen(TetriSolver solver) {
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
