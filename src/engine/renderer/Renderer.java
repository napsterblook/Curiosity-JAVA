package engine.renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.entities.Entity;
import engine.graphics.Window;
import engine.maths.Maths;
import engine.maths.Matrix4f;
import engine.models.BaseModel;
import engine.models.TexturedModel;
import engine.textures.ModelTexture;
import engine.utils.ShaderLoader;

public class Renderer {
	private static final float FOV = 60, NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	private Window window = new Window();
	
	private Matrix4f projectionMatrix;
	
	private ShaderLoader shader;
	
	public Renderer(ShaderLoader shader) {
		this.shader = shader;
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model: entities.keySet())
		{
			List<Entity> batch = entities.get(model);
			
			for (Entity entity: batch) {
				BaseModel baseModel = model.getBaseModel();
				
				GL30.glBindVertexArray(baseModel.getVao());
				GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
				GL20.glEnableVertexAttribArray(2);
				
				Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
						entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
				
				shader.loadTransformationMatrix(transformationMatrix);
				ModelTexture texture = model.getTexture();
				shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
				
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
				GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, baseModel.getVao());
				GL11.glDrawElements(GL11.GL_TRIANGLES, baseModel.getVertices(), GL11.GL_UNSIGNED_INT, 0);
				unbind();
			}
		}
	}
	
	private void unbind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void createProjectionMatrix() {
		float ratio = (float) window.getWidth() / (float) window.getHeight();
		float y_scale = (float) ((1.0f / Math.tan(Math.toRadians(FOV / 2.0f))) * ratio);
		float x_scale = y_scale / ratio;
		float frustrum = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum);
		projectionMatrix.m23 = -1.0f;
		projectionMatrix.m32 = -((2.0f * NEAR_PLANE * FAR_PLANE) / frustrum);
		projectionMatrix.m33 = 0.0f;
	}
}