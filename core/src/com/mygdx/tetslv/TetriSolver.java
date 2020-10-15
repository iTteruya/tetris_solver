package com.mygdx.tetslv;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.tetslv.view.MenuScreen;

public class TetriSolver extends Game {

	public AssetManager manager = new AssetManager();

	@Override
	public void create () {
		setScreen(new MenuScreen(this));
	}

//	@Override
//	public void render () {
//	}
//
//	@Override
//	public void resize(int width, int height) {
//	}
//
//	@Override
//	public void dispose () {
//		super.dispose();
//	}
}
