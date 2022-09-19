package engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.fonts.FontMaster;
import engine.fonts.generator.FontType;
import engine.fonts.generator.GUIText;
import engine.graphics.Colour;
import engine.graphics.Resolution;
import engine.graphics.Window;
import engine.inputs.Input;
import engine.loaders.FileLoader;
import engine.loaders.ModelLoader;
import engine.loaders.OBJLoader;
import engine.loaders.OldTextureLoader;
import engine.loaders.TextureLoader;
import engine.models.BaseModel;
import engine.models.TexturedModel;
import engine.renderer.MasterRenderer;
import engine.terrain.Terrain;
import engine.textures.ModelTexture;

public class Main implements Runnable {
	private Thread game;
	private Window window;
	private MasterRenderer renderer;
	private ModelLoader loader;
	private Camera camera;
	private Light light;
	private FontType font;
	
	public static final String TITLE = FileLoader.getTitle();
	public static final Resolution RES = Resolution.sd();
	public static final float FOV = 70f, NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	public static final Colour BACKGROUND = Colour.cyan();
	public static final float MOVE_SPEED = 0.3f, SPRINT_SPEED = 1.0f, CROUCH_SPEED = 0.1f, SENSITIVITY = 0.1f;
	public static final engine.maths.vector.Vector3f CAMERA_POSITION = new engine.maths.vector.Vector3f(0f, 10f, 0f);
	public static final float TERRAIN_SIZE = 1000f;
	public static final int TERRAIN_VERTICES = 128;
	public static final String OS = System.getProperty("os.name");
	
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<String> shaderFiles = new ArrayList<String>();
	
	public void run() {
		start();
		update();
		stop();
	}
	
	public void awake() {
		game = new Thread(this, "game");
		
		//lwjgl has specific requirements for Mac
		if (OS.contains("Mac")) game.run();
		else game.start();
	}
	
	public void start() {
		String prefix = "res/shaders/";
		String suffix = ".glsl";
		shaderFiles.add(prefix+"vertexShader"+suffix);
		shaderFiles.add(prefix+"fragmentShader"+suffix);
		shaderFiles.add(prefix+"terrain/terrainVertex"+suffix);
		shaderFiles.add(prefix+"terrain/terrainFragment"+suffix);
		shaderFiles.add(prefix+"fonts/fontVertex"+suffix);
		shaderFiles.add(prefix+"fonts/fontFragment"+suffix);
		
		window = new Window(RES, BACKGROUND, TITLE);
		
		window.create();
		window.setIcon(TextureLoader.loadTexture("eclipse.png"));
		
		renderer = new MasterRenderer(FOV, NEAR_PLANE, FAR_PLANE, shaderFiles);
		loader = new ModelLoader();
		
		BaseModel treeModel = OBJLoader.loadObj("tree", loader);
		TexturedModel treeTexturedModel = new TexturedModel(treeModel, new ModelTexture(
				loader.loadPNG("tree")));
		
		FontMaster.init(shaderFiles, loader);
		font = new FontType(loader.loadPNG("yaheiDistance"), new File("res/fonts/yaheiDistance.fnt"));
		GUIText text = new GUIText("Welcome to Curiosity Engine!", 3, font, new Vector2f(0f, 0.4f), 1f, true);
		
		text.setColour(Colour.white());
		text.setWidth(0.52f);
		text.setEdge(0.05f);
		text.setBorderWidth(0.4f);
		text.setBorderEdge(0.5f);
		text.setOffset(new Vector2f(0.005f, 0.005f));
		text.setOutlineColour(Colour.red());
		
		light = new Light(new Vector3f(0f, 0f, 0f), new Vector3f(4f, 4f, 4f));
		camera = new Camera(CAMERA_POSITION, MOVE_SPEED, SPRINT_SPEED, CROUCH_SPEED, SENSITIVITY);
		
		for (int i = 0; i < 2000; i++) {
			
			List<Float> xyz = new ArrayList<Float>();
			for (int j = 0; j < 3; j++)
				xyz.add(new Random().nextFloat() * 2000f - 1000f);
			
			entities.add(new Entity(treeTexturedModel, new Vector3f(xyz.get(0), 0f, xyz.get(2)),
				0f, 0f, 0f, 4f));
		}
		
		OldTextureLoader obsolete = new OldTextureLoader();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				terrains.add(new Terrain(TERRAIN_SIZE, TERRAIN_VERTICES, i, j, loader,
						new ModelTexture(obsolete.loadTexture("grass.png"))));
			}
		}
	}

	private void update() {
		while (!window.shouldClose())
		{
			//input handling
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11))
				window.setFullscreen(!window.isFullscreen());
			
			if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT))
				window.setLocked(true);
			
			if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE))
				window.setLocked(false);
			
			camera.move();
			window.update();
			renderer.render(light, camera);
			
			for (Terrain terrain: terrains) 
				renderer.processTerrain(terrain);
			for (Entity entity: entities)
				renderer.processEntity(entity);
			
			FontMaster.render();
			window.swapBuffers();
		}
	}
	
	private void stop() {
		FontMaster.destroy();
		renderer.destroy();
		loader.destroy();
		window.destroy();
	}
	
	public static void main(String[] args) { new Main().awake(); }
}