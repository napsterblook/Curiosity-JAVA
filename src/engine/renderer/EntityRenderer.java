package engine.renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.entities.Entity;
import engine.maths.Maths;
import engine.maths.Matrix4f;
import engine.models.BaseModel;
import engine.models.TexturedModel;
import engine.textures.ModelTexture;
import engine.utils.ShaderLoader;

public class EntityRenderer {
	private ShaderLoader shader;
	
	public EntityRenderer(ShaderLoader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
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
}