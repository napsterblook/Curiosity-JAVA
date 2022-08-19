package engine.renderer;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Texture {
	private static HashMap<String, Integer> idMap = new HashMap<String, Integer>();
	
	public static int loadTexture(String texture) {
		if (idMap.containsKey(texture))
			return idMap.get("res/textures/" + texture);
		
		int width;
		int height;
		ByteBuffer buffer;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);
			
			@SuppressWarnings("unused")
			URL url = Texture.class.getResource("res/textures/" + texture);
			File file = new File("res/textures/" + texture);
			String filePath = file.getAbsolutePath();
			buffer = STBImage.stbi_load(filePath, w, h, channels, 4);
			
			if (buffer == null) {
				System.err.println(String.format("[ERROR]: File cannot be loaded at %s", texture));
				System.err.println(STBImage.stbi_failure_reason());
				System.exit(-1);
			}
			
			width = w.get();
			height = h.get();
			
			int id = GL11.glGenTextures();
			idMap.put(texture, id);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			STBImage.stbi_image_free(buffer);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			
			return id;
		} catch (Exception e) {
			System.err.println("[ERROR]: Failed to generate texture, printing stacktrace...");
			e.printStackTrace();
		}
		return 0;
	}
}
