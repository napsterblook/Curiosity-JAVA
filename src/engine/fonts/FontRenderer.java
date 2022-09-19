package engine.fonts;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import engine.fonts.generator.FontType;
import engine.fonts.generator.GUIText;
import engine.shaders.FontShader;

public class FontRenderer {
	private FontShader shader;
	
	public FontRenderer(String vertexShader, String fragmentShader) {
		shader = new FontShader(vertexShader, fragmentShader);
	}
	
	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		for (FontType font: texts.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
			
			for(GUIText text: texts.get(font))
				renderText(text);
		}
		unbind();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		shader.start();
	}
	
	private void renderText(GUIText text) {
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		shader.loadColour(text.getColour());
		shader.loadTranslation(text.getPosition());
		shader.loadWidth(text.getWidth());
		shader.loadOffset(text.getOffset());
		shader.loadBorderWidth(text.getBorderWidth());
		shader.loadBorderEdge(text.getBorderEdge());
		shader.loadOffset(text.getOffset());
		shader.loadOutlineColour(text.getOutlineColour());
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void unbind() {
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public void destroy() {
		shader.destroy();
	}
}
