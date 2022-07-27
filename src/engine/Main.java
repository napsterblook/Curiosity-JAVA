package engine;

import engine.models.BaseModel;
import engine.models.ModelLoader;
import engine.renderer.Renderer;
import engine.renderer.Window;
import engine.utils.FileLoader;

public class Main {
	
	private static final int WIDTH = 1280, HEIGHT = 720;
	private static final String TITLE = FileLoader.getTitle();
	private Window window;
	private Renderer renderer = new Renderer();
	private ModelLoader loader;
	
	private float[] vertices = {
			
			-0.5f, +0.5f, 0.0f,
			-0.5f, -0.5f, 0.0f,
			+0.5f, -0.5f, 0.0f,
			
			+0.5f, -0.5f, 0.0f,
			+0.5f, +0.5f, 0.0f,
			-0.5f, +0.5f, 0.0f
			
	}; //temporary
	
	private BaseModel model;
	
	private void start() {
		init();
		while (!window.shouldClose()) update(); //call update every frame unless closed
		destroy();
	}
	
	private void init() {
		window = new Window(WIDTH, HEIGHT, TITLE);
		window.create();
		window.setBackground(1.0f, 0.0f, 0.0f);
		
		loader = new ModelLoader();
		model = loader.loadModel(vertices);
	}
	
	private void update() {
		renderer.render(model);
		window.update();;
	}
	
	private void destroy() {
		loader.destroy();
		window.destroy();
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
