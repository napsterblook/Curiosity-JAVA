package engine.shaders;

import org.joml.Vector2f;

import engine.graphics.Colour;

public class FontShader extends ShaderProgram {
	private int location_colour;
	private int location_translation;
	private int location_width;
	private int location_edge;
	private int location_borderWidth;
	private int location_borderEdge;
	private int location_offset;
	private int location_outlineColour;
	
	public FontShader(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
	}

	@Override
	protected void getUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
		location_width = super.getUniformLocation("width");
		location_edge = super.getUniformLocation("edge");
		location_borderWidth = super.getUniformLocation("borderWidth");
		location_borderEdge = super.getUniformLocation("borderEdge");
		location_offset = super.getUniformLocation("offset");
		location_outlineColour = super.getUniformLocation("outlineColour");
	}

	@Override
	protected void bindAttribs() {
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "textureCoords");
	}
	
	public void loadColour(Colour colour) {
		super.loadColour(location_colour, colour);
	}
	
	public void loadTranslation(Vector2f translation) {
		super.load2DVector(location_translation, translation);
	}
	
	public void loadWidth(float width) {
		super.loadFloat(location_width, width);
	}
	
	public void loadEdge(float edge) {
		super.loadFloat(location_edge, edge);
	}
	
	public void loadBorderWidth(float borderWidth) {
		super.loadFloat(location_borderWidth, borderWidth);
	}
	
	public void loadBorderEdge(float borderEdge) {
		super.loadFloat(location_borderEdge, borderEdge);
	}
	
	public void loadOffset(Vector2f offset) {
		super.load2DVector(location_offset, offset);
	}
	
	public void loadOutlineColour(Colour outlineColour) {
		super.loadColour(location_outlineColour, outlineColour);
	}
}
