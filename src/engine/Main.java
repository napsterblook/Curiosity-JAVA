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
import engine.renderer.Renderer;
import engine.textures.ModelTexture;
import engine.utils.FileLoader;
import engine.utils.ModelLoader;
import engine.utils.ShaderLoader;

public class Main implements Runnable {
	private Thread game;
	private Window window;
	private ShaderLoader shader;
	private BaseModel model;
//	private ModelTexture texture;
	private TexturedModel texturedModel;
	private Renderer renderer;
	private ModelLoader loader;
	private Camera camera;
	private Light light;
	
	private static final int WIDTH = 1280, HEIGHT = 720;
	private static final Vector3f BACKGROUND = new Vector3f(0.0f, 0.0f, 0.0f);
	private static final String VERTEX_FILE = "src/engine/shaders/vertexShader.glsl",
			FRAGMENT_FILE = "src/engine/shaders/fragmentShader.glsl";
	public static final float MOVE_SPEED = 0.3f, SPRINT_SPEED = 1.0f;
	private String os = System.getProperty("os.name");
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	float[] vertices = {			
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,0.5f,-0.5f,		
			
			-0.5f,0.5f,0.5f,	
			-0.5f,-0.5f,0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			0.5f,0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			-0.5f,-0.5f,0.5f,	
			-0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,0.5f,
			-0.5f,0.5f,-0.5f,
			0.5f,0.5f,-0.5f,
			0.5f,0.5f,0.5f,
			
			-0.5f,-0.5f,0.5f,
			-0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,0.5f
			
	};
	
	float[] textureCoords = {
			
			0,0,
			0,1,
			1,1,
			1,0,			
			0,0,
			0,1,
			1,1,
			1,0,			
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0

			
	};
	
	float[] normals = {
			
			0, 1, 0,
			0, 0, 1,
			-1, 0, 0,
			0, -1, 0,
			1, 0, 0,
			0, 0, -1
			
	};
 	
	int[] indices = {
			0,1,3,	
			3,1,2,	
			4,5,7,
			7,5,6,
			8,9,11,
			11,9,10,
			12,13,15,
			15,13,14,	
			16,17,19,
			19,17,18,
			20,21,23,
			23,21,22

	};
	
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
		String title = FileLoader.getTitle();
		window = new Window(WIDTH, HEIGHT, BACKGROUND, title);
		window.create();
		
		shader = new ShaderLoader(VERTEX_FILE, FRAGMENT_FILE);
		renderer = new Renderer(shader);
		loader = new ModelLoader();
		
		model = loader.appendVAO(vertices, textureCoords, normals, indices);
//		model = OBJLoader.loadObj("cube", loader);
		texturedModel = new TexturedModel(model, new ModelTexture(
				loader.loadPNG("mat")));
		
		light = new Light(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(4.0f, 4.0f, 4.0f));
		
		camera = new Camera(MOVE_SPEED, SPRINT_SPEED);
		
		Random random = new Random();
		
		for (int i = 0; i < 30; i++) {
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
			
			shader.start();
			shader.loadLight(light);
			
			shader.loadViewMatrix(camera);
			for (Entity entity: entities)
				renderer.render(entity, shader);
			shader.stop();
			
			window.swapBuffers();
		}
	}
	
	private void stop() {
		shader.destroy();
		window.destroy();
		loader.destroy();
	}
	
	public static void main(String[] args) {
		new Main().awake();
	}
}

//TODO: network with kryonet