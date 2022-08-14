package engine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.entities.Entity;
import engine.graphics.Window;
import engine.maths.Matrix4f;
import engine.models.BaseModel;
import engine.models.TexturedModel;
import engine.utils.Maths;
import engine.utils.ShaderLoader;

public class Renderer {
//	public void renderMesh(Mesh mesh) {
//		GL30.glBindVertexArray(mesh.getVAO());
//		GL30.glEnableVertexAttribArray(0);
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIBO());
//		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
//		GL30.glDisableVertexAttribArray(0);
//		GL30.glBindVertexArray(0);
//	}
	
	private static final float FOV = 60, NEAR_PLANE = 0.1f, FAR_PLANE = 1000;
	
	private Matrix4f viewMatrix;
	public Renderer(ShaderLoader shader) {
		createViewMatrix();
		shader.start();
		shader.loadViewMatrix(viewMatrix);
		shader.stop();
	}
	
	public void render(Entity entity, ShaderLoader shader) {
		TexturedModel model = entity.getModel();
		BaseModel baseModel = model.getBaseModel();
		
		GL30.glBindVertexArray(baseModel.getVao());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		
		shader.loadTransformationMatrix(transformationMatrix);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, baseModel.getVao());
		GL11.glDrawElements(GL11.GL_TRIANGLES, baseModel.getVertices(), GL11.GL_UNSIGNED_INT, 0);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void createViewMatrix() {
		float ratio = (float) Window.getWidth() / (float) Window.getHeight();
		float y_scale = (float) ((1.0f / Math.tan(Math.toRadians(FOV / 2.0f))) * ratio);
		float x_scale = y_scale / ratio;
		float frustrum = FAR_PLANE - NEAR_PLANE;
		
		viewMatrix = new Matrix4f();
		viewMatrix.m00 = x_scale;
		viewMatrix.m11 = y_scale;
		viewMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum);
		viewMatrix.m23 = -1.0f;
		viewMatrix.m32 = -((2.0f * NEAR_PLANE * FAR_PLANE) / frustrum);
		viewMatrix.m33 = 0.0f;
	}
}