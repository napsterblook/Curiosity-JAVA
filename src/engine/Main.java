package engine;

import engine.renderer.Window;

public class Main {
	
	private static final int WIDTH = 1280, HEIGHT = 720;
	private static final String TITLE = ""; //TODO: ADD STB XML FILE PARSER
	private Window window;
	
	private void start() {
		init();
		while (!window.shouldClose()) update(); //call update every frame unless closed
	}
	
	private void init() {
		window = new Window(WIDTH, HEIGHT, TITLE);
		window.create();
	}
	
	private void update() {
		window.update();
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
