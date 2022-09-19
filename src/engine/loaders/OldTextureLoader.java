package engine.loaders;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

public class OldTextureLoader {

    private int width, height, texture;
    private int[] data;
    private IntBuffer buffer;

    public int loadTexture(String path) {
    	int[] pixels = null;
    	
    	try {
    		BufferedImage image = ImageIO.read(new FileInputStream("res/textures/"+path));
    		width = image.getWidth();
    		height = image.getHeight();
    		pixels = new int[width * height];
    		image.getRGB(0, 0, width, height, pixels, 0, width);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}

    	data = new int[width * height];
    	
    	for (int i = 0; i < width * height; i++) {
    		int a = (pixels[i] & 0xff000000) >> 24;
    		int r = (pixels[i] & 0xff0000) >> 16;
    		int g = (pixels[i] & 0xff00) >> 8;
    		int b = (pixels[i] & 0xff);

    		data[i] = a << 24 | b << 16 | g << 8 | r;
    	}

    	int result = GL11.glGenTextures();
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
    	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
    	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

    	buffer = ByteBuffer.allocateDirect(data.length << 2)
    			.order(ByteOrder.nativeOrder()).asIntBuffer();
    	buffer.put(data).flip();

    	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA,
    			GL11.GL_UNSIGNED_BYTE, buffer);
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    	return result;
    }
    
    public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void bind() {
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
    }

    public void unbind() {
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public int getTextureID() {
    	return texture;
    }
}