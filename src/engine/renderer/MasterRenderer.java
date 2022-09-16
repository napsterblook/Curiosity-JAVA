package engine.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import engine.entities.Camera;
import engine.entities.Entity;
import engine.entities.Light;
import engine.graphics.Window;
import engine.maths.Matrix4f;
import engine.models.TexturedModel;
import engine.shaders.TerrainShader;
import engine.terrain.Terrain;
import engine.utils.ShaderLoader;

public class MasterRenderer {
	private ShaderLoader shader;
	private EntityRenderer renderer;
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader;
	private Matrix4f projectionMatrix;
	
	private float fov, nearPlane, farPlane;
	private Window window = new Window();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	public MasterRenderer(float fov, float nearPlane, float farPlane, String vertexFile,
			String fragmentFile, String terrainVertexFile, String terrainFragmentFile) {
		this.fov = fov;
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		
		shader = new ShaderLoader(vertexFile, fragmentFile);
		terrainShader = new TerrainShader(terrainVertexFile, terrainFragmentFile);
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}

	public void render(Light sun, Camera camera) {
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		
		if (batch != null)
			batch.add(entity);
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	private void createProjectionMatrix() {
		float ratio = (float) window.getWidth() / (float) window.getHeight();
		float y_scale = (float) ((1.0f / Math.tan(Math.toRadians(fov / 2.0f))) * ratio);
		float x_scale = y_scale / ratio;
		float frustrum = farPlane - nearPlane;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustrum);
		projectionMatrix.m23 = -1.0f;
		projectionMatrix.m32 = -((2.0f * nearPlane * farPlane) / frustrum);
		projectionMatrix.m33 = 0.0f;
	}
	
	public void destroy() {
		shader.destroy();
		terrainShader.destroy();
	}
}
