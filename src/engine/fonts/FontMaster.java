package engine.fonts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.fonts.generator.FontType;
import engine.fonts.generator.GUIText;
import engine.fonts.generator.TextMeshData;
import engine.loaders.ModelLoader;

public class FontMaster {
	private static ModelLoader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init(List<String> shaderFiles, ModelLoader loader) {
		renderer = new FontRenderer(shaderFiles.get(4), shaderFiles.get(5));
		FontMaster.loader = loader;
	}
	
	public static void render(){
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.appendVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty())
			texts.remove(text.getFont());
	}
	
	public static void destroy() {
		renderer.destroy();
	}
}
