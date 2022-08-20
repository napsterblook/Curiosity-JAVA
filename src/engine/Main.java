package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.graphics.Window;
import engine.io.Input;
import engine.maths.Vector3f;
import engine.models.BaseModel;
import engine.models.TexturedModel;
import engine.renderer.MasterRenderer;
import engine.textures.ModelTexture;
import engine.utils.FileLoader;
import engine.utils.ModelLoader;
import engine.utils.OBJLoader;

public class Main implements Runnable {
	private Thread game;
	private Window window;
	private MasterRenderer renderer;
	private BaseModel model;
	private TexturedModel texturedModel;
	private ModelLoader loader;
	private Camera camera;
	private Light light;
	
	private static final String TITLE = FileLoader.getTitle();
	private static final int WIDTH = 1280, HEIGHT = 720;
	private static final Vector3f BACKGROUND = new Vector3f(0.5f, 0.0f, 0.0f);
	private static final String VERTEX_FILE = "src/engine/shaders/vertexShader.glsl",
			FRAGMENT_FILE = "src/engine/shaders/fragmentShader.glsl";
	public static final float MOVE_SPEED = 0.3f, SPRINT_SPEED = 1.0f, CROUCH_SPEED = 0.1f;
	private String os = System.getProperty("os.name");
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	public void run() {
		start();
		update();
		stop();
	}
	
	public void awake() {
		game = new Thread(this, "game");
		
		if (os.contains("Mac"))
			game.run();
		else
			game.start();
	}
	
	public void start() {
		window = new Window(WIDTH, HEIGHT, BACKGROUND, TITLE);
		window.create();
		renderer = new MasterRenderer();
		loader = new ModelLoader();

		model = OBJLoader.loadObj("cube", loader);
		texturedModel = new TexturedModel(model, new ModelTexture(
				loader.loadPNG("mat")));
		
		light = new Light(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(4.0f, 4.0f, 4.0f));
		camera = new Camera(MOVE_SPEED, SPRINT_SPEED, CROUCH_SPEED);
		
		Random random = new Random();
		
		for (int i = 0; i < 500; i++) {
			float x = random.nextFloat() * 100 - 50;
			float y = random.nextFloat() * 100 - 50;
			float z = random.nextFloat() * -150;
			entities.add(new Entity(texturedModel, new Vector3f(x, y, z),
				0.0f, 0.0f, 0.0f, 1.0f));
		}
	}

	private void update() {
		while (!window.shouldClose())
		{
			//input handling
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11))
				window.setFullscreen(!window.isFullscreen());
			
			camera.move();
			window.update();
			renderer.render(light, camera, VERTEX_FILE, FRAGMENT_FILE);
			
			for (Entity entity: entities)
				renderer.processEntity(entity);
			
			window.swapBuffers();
		}
	}
	
	private void stop() {
		renderer.destroy();
		loader.destroy();
		window.destroy();
	}
	
	public static void main(String[] args) {
		new Main().awake();
	}
}

//TODO: network with kryonet