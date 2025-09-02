package lepus.graphics;

import org.lwjgl.opengl.GL30;

public class GL {
	public static int getGLType(String str) {
		boolean at_least_2_chars = str.length() > 1;
		switch (str.charAt(0)) {
		case 'b':
			return GL30.GL_BYTE;
		case 's':
			return GL30.GL_SHORT;
		case 'i':
			return GL30.GL_INT;
		case 'f':
			return GL30.GL_FLOAT;
		case 'd':
			return GL30.GL_DOUBLE;
		case 'u':
			if (at_least_2_chars)
				switch (str.charAt(1)) {
				case 'b':
					return GL30.GL_UNSIGNED_BYTE;
				case 's':
					return GL30.GL_UNSIGNED_SHORT;
				case 'i':
					return GL30.GL_UNSIGNED_INT;
				}
			break;
		case '2':
			if (at_least_2_chars)
				if (str.charAt(1) == 'b')
					return GL30.GL_2_BYTES;
			break;
		case '3':
			if (at_least_2_chars)
				if (str.charAt(1) == 'b')
					return GL30.GL_3_BYTES;
			break;
		case '4':
			if (at_least_2_chars)
				if (str.charAt(1) == 'b')
					return GL30.GL_4_BYTES;
			break;
		}
		return GL30.GL_NONE;
	}

	public static int getGLTypeSize(int type) {
		switch (type) {
		case GL30.GL_BYTE:
		case GL30.GL_UNSIGNED_BYTE:
			return 1;
		case GL30.GL_SHORT:
		case GL30.GL_UNSIGNED_SHORT:
		case GL30.GL_2_BYTES:
			return 2;
		case GL30.GL_INT:
		case GL30.GL_UNSIGNED_INT:
		case GL30.GL_FLOAT:
		case GL30.GL_4_BYTES:
			return 4;
		case GL30.GL_DOUBLE:
			return 8;
		case GL30.GL_3_BYTES:
			return 3;
		}
		return 0; // 返回0表示查询失败，传入的type不是OpenGL的数据类型
	}
}
