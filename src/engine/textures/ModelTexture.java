package engine.textures;

public class ModelTexture {
	private int texture;
	
	private float shineDamper = 1.0f;
	private float reflectivity = 0.0f;
	
	private boolean hasTransparency = false;
	
	public ModelTexture(int texture) {
		this.texture = texture;
	}
	
	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public int getID() {
		return this.texture;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
}
