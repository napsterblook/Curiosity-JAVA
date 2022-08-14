package engine.models;

import engine.textures.ModelTexture;

public class TexturedModel {
	private BaseModel baseModel;
	private ModelTexture texture;
	
	public TexturedModel(BaseModel baseModel, ModelTexture texture) {
		this.baseModel = baseModel;
		this.texture = texture;
	}

	public BaseModel getBaseModel() {
		return baseModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}
