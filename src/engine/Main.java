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
import engine.renderer.Texture;
import engine.terrain.Terrain;
import engine.textures.ModelTexture;
import engine.utils.FileLoader;
import engine.utils.ModelLoader;
import engine.utils.OBJLoader;

public class Main implements Runnable {
	private Thread game;
	private Window window;
	private MasterRenderer renderer;
	private BaseModel treeModel;
	private BaseModel grassModel;
	private TexturedModel treeTexturedModel;
	private TexturedModel grassTexturedModel;
	private ModelLoader loader;
	private Camera camera;
	private Light light;
	private Texture texture = new Texture();
	
	private static final String TITLE = FileLoader.getTitle();
	private static final int WIDTH = 1280, HEIGHT = 720;
	private static final float FOV = 70, NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	private static final Vector3f BACKGROUND = new Vector3f(0.5f, 0.0f, 0.0f);
	private static final String VERTEX_FILE = "src/engine/shaders/vertexShader.glsl",
			FRAGMENT_FILE = "src/engine/shaders/fragmentShader.glsl";
	private static final String TERRAIN_VERTEX_FILE = "src/engine/shaders/terrainVertexShader.glsl",
			TERRAIN_FRAGMENT_FILE = "src/engine/shaders/terrainFragmentShader.glsl";
	public static final float MOVE_SPEED = 0.3f, SPRINT_SPEED = 1.0f, CROUCH_SPEED = 0.1f;
	private static final float TERRAIN_SIZE = 1000;
	private static final int TERRAIN_VERTICES = 128;
	
	private String os = System.getProperty("os.name");
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
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
		
		renderer = new MasterRenderer(FOV, NEAR_PLANE, FAR_PLANE, VERTEX_FILE, FRAGMENT_FILE, TERRAIN_VERTEX_FILE, TERRAIN_FRAGMENT_FILE);
		loader = new ModelLoader();

		treeModel = OBJLoader.loadObj("tree", loader);
		treeTexturedModel = new TexturedModel(treeModel, new ModelTexture(
				loader.loadPNG("tree")));
		
		grassModel = OBJLoader.loadObj("grassModel", loader);
		grassTexturedModel = new TexturedModel(grassModel, new ModelTexture(
				loader.loadPNG("grassTexture")));
		
		light = new Light(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(4.0f, 4.0f, 4.0f));
		camera = new Camera(MOVE_SPEED, SPRINT_SPEED, CROUCH_SPEED);
		
		Random random = new Random();
		
		for (int i = 0; i < 2000; i++) {
			float x = random.nextFloat() * 2000 - 1000;
			float y = 0.0f;
			float z = random.nextFloat() * 2000 - 1000;
			entities.add(new Entity(treeTexturedModel, new Vector3f(x, y, z),
				0.0f, 0.0f, 0.0f, 4.0f));
		}
		
		for (int i = 0; i < 2000; i++) {
			float x = random.nextFloat() * 2000 - 1000;
			float y = 1.0f;
			float z = random.nextFloat() * 2000 - 1000;
			entities.add(new Entity(grassTexturedModel, new Vector3f(x, y, z),
					0.0f, 0.0f, 0.0f, 1.0f));
		}
		
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				terrains.add(new Terrain(TERRAIN_SIZE, TERRAIN_VERTICES, i, j, loader,
						new ModelTexture(loader.loadPNG("grass"))));
			}
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
			renderer.render(light, camera);
			
			for (Terrain terrain: terrains) 
				renderer.processTerrain(terrain);
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