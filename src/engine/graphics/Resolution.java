package engine.graphics;

import org.joml.Vector2f;

public class Resolution {
	public Vector2f resolution;
	
	public Resolution(float w, float h) {
		this.resolution = new Vector2f(w, h);
	}
	
	public static Resolution std() { return new Resolution(640f, 480f); }
	public static Resolution sd() { return new Resolution (1280f, 720f); }
	public static Resolution hd() { return new Resolution (1920f, 1080f); }
	public static Resolution uhd() { return new Resolution(3840f, 2160f); }
	
	public String toString() { return String.format("(%1$f, %2$f)", resolution.x, resolution.y); }
	
	public float getW() { return resolution.x; }
	public float getH() { return resolution.y; }
}
