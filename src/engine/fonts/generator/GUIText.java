package engine.fonts.generator;

import org.joml.Vector2f;

import engine.fonts.FontMaster;
import engine.graphics.Colour;

public class GUIText {

	private String textString;

	private int textMeshVao, vertexCount, numberOfLines;
	private Colour colour;
	private float width, edge, borderWidth, borderEdge, fontSize, lineMaxSize;
	private Colour outlineColour;

	private Vector2f position, offset;
	private FontType font;
	private boolean centerText = false;

	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength,
			boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		FontMaster.loadText(this);
	}

	public void remove() {
		FontMaster.removeText(this);
	}
	
	public FontType getFont() {
		return font;
	}
	
	public void setColour(Colour colour) {
		this.colour = colour;
	}

	public Colour getColour() {
		return colour;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getEdge() {
		return edge;
	}

	public void setEdge(float edge) {
		this.edge = edge;
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}

	public float getBorderEdge() {
		return borderEdge;
	}

	public void setBorderEdge(float borderEdge) {
		this.borderEdge = borderEdge;
	}

	public Colour getOutlineColour() {
		return outlineColour;
	}

	public void setOutlineColour(Colour outlineColour) {
		this.outlineColour = outlineColour;
	}

	public Vector2f getOffset() {
		return offset;
	}

	public void setOffset(Vector2f offset) {
		this.offset = offset;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public Vector2f getPosition() {
		return position;
	}

	public int getMesh() {
		return textMeshVao;
	}

	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}

	protected float getFontSize() {
		return fontSize;
	}

	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	protected float getMaxLineSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	protected String getTextString() {
		return textString;
	}

}
