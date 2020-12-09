package com.mygdx.tetslv;

import com.badlogic.gdx.Game;
import com.mygdx.tetslv.view.MenuScreen;

public class TetrisSolver extends Game {

	@Override
	public void create () {
		setScreen(new MenuScreen(this));
	}

	@Override
	public void dispose(){
		super.dispose();
	}

}
