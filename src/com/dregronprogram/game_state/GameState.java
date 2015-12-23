package com.dregronprogram.game_state;

import java.awt.Canvas;
import java.awt.Graphics2D;

import com.dregronprogram.application.ApplicationResources;
import com.dregronprogram.application.ApplicationResourcesImpl;
import com.dregronprogram.state.State;
import com.dregronprogram.state.StateMachine;
import com.dregronprogram.tiled_map.TiledMap;

public class GameState extends State {

	private final ApplicationResources applicationResources;
	private final TiledMap tiledmap;
	
	public GameState(StateMachine stateMachine) {
		super(stateMachine);
		
		this.applicationResources = new ApplicationResourcesImpl();
		this.tiledmap = new TiledMap("../tiled_map/Level1_Map.json");
	}

	@Override
	public void update(double delta) {

	}

	@Override
	public void draw(Graphics2D g) {

	}

	@Override
	public void init(Canvas canvas) {

	}
	
	public TiledMap getTiledmap() {
		return tiledmap;
	}
	
	public ApplicationResources getApplicationResources() {
		return applicationResources;
	}
}
