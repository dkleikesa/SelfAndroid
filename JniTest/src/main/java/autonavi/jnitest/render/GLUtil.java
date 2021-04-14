package autonavi.jnitest.render;

import android.opengl.GLES30;
import android.util.Log;

public class GLUtil {
    private static final String TAG = "GLUtil";

    public static int createProgram(String vertex, String fragment) {
        int vertexShader = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER);
        if (vertexShader == 0) {
            Log.e(TAG, "load vertex shader failed! ");
            return 0;
        }
        GLES30.glShaderSource(vertexShader, vertex);
        GLES30.glCompileShader(vertexShader);
        int[] compiled = new int[1];
        GLES30.glGetShaderiv(vertexShader, GLES30.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) { // compile failed
            Log.e(TAG, "Error compiling shader. vertexShader: ");
            GLES30.glDeleteShader(vertexShader); // delete shader
            return 0;
        }

        int fragmentShader = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER);
        if (fragmentShader == 0) {
            Log.e(TAG, "load fragment shader failed! ");
            return 0;
        }
        GLES30.glShaderSource(fragmentShader, fragment);
        GLES30.glCompileShader(fragmentShader);
        compiled = new int[1];
        GLES30.glGetShaderiv(fragmentShader, GLES30.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) { // compile failed
            Log.e(TAG, "Error compiling shader. fragmentShader: ");
            GLES30.glDeleteShader(fragmentShader); // delete shader
            return 0;
        }

        int program = GLES30.glCreateProgram();
        if (program == 0) {
            Log.e(TAG, "create program failed! ");
            return 0;
        }

        GLES30.glAttachShader(program, vertexShader);
        GLES30.glAttachShader(program, fragmentShader);
        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(fragmentShader);
        GLES30.glLinkProgram(program);
        int[] linkStatus = new int[1];
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) { // link failed
            Log.e(TAG, "Error link program: ");
            Log.e(TAG, GLES30.glGetProgramInfoLog(program));
            GLES30.glDeleteProgram(program); // delete program
            return 0;
        }
        return program;
    }
}
