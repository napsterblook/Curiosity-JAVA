package engine.graphics;

import org.joml.Vector4f;

import engine.maths.Maths;

public class Colour {
	public Vector4f colour;

	public Colour(float r, float g, float b, float a) {
		this.colour = Maths.Clamp(new Vector4f(r, g, b, a), 0f, 1f);
	}
	
	public Colour(float r, float g, float b) {
		this.colour = Maths.Clamp(new Vector4f(r, g, b, 1f), 0f, 1f) ;
	}
	
	public static Colour black() { return new Colour    (0f, 0f, 0f); }
	public static Colour white() { return new Colour    (1f, 1f, 1f); }
	public static Colour red() { return new Colour      (1f, 0f, 0f); }
	public static Colour green() { return new Colour    (0f, 1f, 0f); }
	public static Colour blue() { return new Colour     (0f, 0f, 1f); }
	public static Colour grey() { return new Colour     (0.5f, 0.5f, 0.5f); }
	public static Colour darkRed() { return new Colour  (0.5f, 0f, 0f); }
	public static Colour darkGreen() { return new Colour(0f, 0.5f, 0f); }
	public static Colour darkBlue() { return new Colour (0f, 0f, 0.5f); }
	public static Colour yellow() { return new Colour   (1f, 1f, 0f); }
	public static Colour cyan() { return new Colour     (0f, 1f, 1f); }
	public static Colour magenta() { return new Colour  (1f, 0f, 1f); }
	
	public String toString() { return String.format("(%1$f, %2$f, %3$f)", colour.x, colour.y, colour.z); }
	
	public float getR() { return colour.x; }
	public float getG() { return colour.y; }
	public float getB() { return colour.z; }
	public float getA() { return colour.w; }
}