package engine;

import org.lwjgl.glfw.GLFW;

import engine.entities.Camera;
import engine.entities.Entity;
import engine.graphics.Window;
import engine.io.Input;
import engine.maths.Vector3f;
import engine.models.BaseModel;
import engine.models.TexturedModel;
import engine.renderer.Renderer;
import engine.textures.ModelTexture;
import engine.utils.ModelLoader;
import engine.utils.ShaderLoader;

public class Main implements Runnable {
	private Thread game;
	private Window window;
	private ShaderLoader shader;
	private BaseModel model;
	private ModelTexture texture;
	private TexturedModel texturedModel;
	private Entity entity;
	private Renderer renderer;
	private ModelLoader loader;
	private Camera camera;
	
	private static final int WIDTH = 1280, HEIGHT = 720;
	private static final Vector3f BACKGROUND = new Vector3f(0.0f, 0.0f, 0.0f);
	private static final String VERTEX_FILE = "src/engine/shaders/vertexShader.glsl",
			FRAGMENT_FILE = "src/engine/shaders/fragmentShader.glsl";
	private static final float MOVE_SPEED = 0.1f;
	//TODO: Implement flexible title
	private String os = System.getProperty("os.name");
	
//	public Mesh mesh = new Mesh(new Vertex[] {
//			new Vertex(new Vector3f(-0.5f,  0.5f, 0.0f)),
//			new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f)),
//			new Vertex(new Vector3f( 0.5f, -0.5f, 0.0f)),
//			new Vertex(new Vector3f( 0.5f,  0.5f, 0.0f))
//		}, new int[] {
//			0, 1, 2,
//			0, 3, 2
//		});
	
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
		window = new Window(WIDTH, HEIGHT, BACKGROUND, "Game");
		window.create();
		shader = new ShaderLoader(VERTEX_FILE, FRAGMENT_FILE);
		renderer = new Renderer(shader);
		loader = new ModelLoader();
		model = loader.appendVAO(vertices, textureCoords, indices);
		texture = new ModelTexture(loader.loadTexture("textures/stanley.png"));
		texturedModel = new TexturedModel(model, texture);
		entity = new Entity(texturedModel, new Vector3f(0.0f, 0.0f, -5.0f), 0.0f, 0.0f, 0.0f, 1.0f);
		camera = new Camera(MOVE_SPEED);
//		mesh.create();
	}
	
	private void update() {
		while (!window.shouldClose())
		{
			//input handling
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11))
				window.setFullscreen(!window.isFullscreen());
			
			entity.rotate(3.0f, 3.0f, 0.0f);
			
			camera.move();
			window.update();
			
			shader.start();
			shader.loadViewMatrix(camera);
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