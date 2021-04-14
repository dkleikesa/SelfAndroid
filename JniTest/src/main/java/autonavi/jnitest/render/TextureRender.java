package autonavi.jnitest.render;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TextureRender implements GLSurfaceView.Renderer {
    private static final String TAG = "TriangleSample";
    int program;
    int position;
    int textureId;
    Bitmap bitmap;

    public TextureRender(Bitmap b) {
        bitmap = b;
    }

    private static final String VERTEX_SHADER = "" +
            "#version 300 es                            \n" +
            "layout(location = 0) in vec4 a_position;   \n" +
            "layout(location = 1) in vec2 a_texCoord;   \n" +
            "out vec2 v_texCoord;                       \n" +
            "void main()                                \n" +
            "{                                          \n" +
            "   gl_Position = a_position;               \n" +
            "   v_texCoord = a_texCoord;                \n" +
            "}                                          \n";
    /**
     * 片段着色器
     */
    private static final String FRAGMENT_SHADER = "" +
            "#version 300 es                                     \n" +
            "precision mediump float;                            \n" +
            "in vec2 v_texCoord;                                 \n" +
            "layout(location = 0) out vec4 outColor;             \n" +
            "uniform sampler2D s_TextureMap;                     \n" +
            "void main()                                         \n" +
            "{                                                   \n" +
            "  outColor = texture(s_TextureMap, v_texCoord);     \n" +
            "}                                                   \n";


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        GLES30.glGenTextures(1, intBuffer);
        textureId = intBuffer.get();
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, GLES30.GL_NONE);

        program = GLUtil.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        position = GLES30.glGetUniformLocation(program, "s_TextureMap");


        Log.d(TAG, "mPositionHandle: " + position);
        GLES30.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (program == GLES30.GL_NONE || textureId == GLES30.GL_NONE) return;
        float verticesCoords[] = {
                -1.0f, 0.5f, 0.0f,  // Position 0
                -1.0f, -0.5f, 0.0f,  // Position 1
                1.0f, -0.5f, 0.0f,  // Position 2
                1.0f, 0.5f, 0.0f,  // Position 3
        };

        float textureCoords[] = {
                0.0f, 0.0f,        // TexCoord 0
                0.0f, 1.0f,        // TexCoord 1
                1.0f, 1.0f,        // TexCoord 2
                1.0f, 0.0f         // TexCoord 3
        };


        //upload RGBA image data
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.position(0);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, byteBuffer);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, GLES30.GL_NONE);

        // Use the program object
        GLES30.glUseProgram(program);


        ByteBuffer vbb = ByteBuffer.allocateDirect(verticesCoords.length * 4); // 一个 float 是四个字节
        vbb.order(ByteOrder.nativeOrder()); // 必须要是 native order
        FloatBuffer vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(verticesCoords);
        vertexBuffer.position(0); // 这一行不要漏了

        // Load the vertex position
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT,
                false, 3 * 4, vertexBuffer);

        vbb = ByteBuffer.allocateDirect(textureCoords.length * 4); // 一个 float 是四个字节
        vbb.order(ByteOrder.nativeOrder()); // 必须要是 native order
        FloatBuffer fragmentBuffer = vbb.asFloatBuffer();
        fragmentBuffer.put(textureCoords);
        fragmentBuffer.position(0); // 这一行不要漏了
        // Load the texture coordinate
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT,
                false, 2 * 4, fragmentBuffer);

        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);

        // Bind the RGBA map
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);

        // Set the RGBA map sampler to texture unit to 0
        GLES30.glUniform1i(position, 0);

        short indices[] = {0, 1, 2, 0, 2, 3};
        vbb = ByteBuffer.allocateDirect(indices.length * 2);
        vbb.order(ByteOrder.nativeOrder()); // 必须要是 native order
        ShortBuffer indicesBuffer = vbb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0); // 这一行不要漏了

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_SHORT, indicesBuffer);
    }
}
