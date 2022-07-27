package engine.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.models.BaseModel;

public class Renderer {
	public void render(BaseModel model) {
		GL30.glBindVertexArray(model.getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertices());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
