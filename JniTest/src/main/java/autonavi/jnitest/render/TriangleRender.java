package autonavi.jnitest.render;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRender implements GLSurfaceView.Renderer {
    private static final String TAG = "TriangleSample";
    int program;
    int position;


    private static final String VERTEX_SHADER = "" +
            "#version 300 es                          \n" +
            "layout(location = 0) in vec4 vPosition;  \n" +
            "void main()                              \n" +
            "{                                        \n" +
            "   gl_Position = vPosition;              \n" +
            "}                                        \n";
    /**
     * 片段着色器
     */
    private static final String FRAGMENT_SHADER = "" +
            "#version 300 es                              \n" +
            "precision mediump float;                     \n" +
            "out vec4 fragColor;                          \n" +
            "void main()                                  \n" +
            "{                                            \n" +
            "   fragColor = vec4 ( 1.0, 0.0, 0.0, 1.0 );  \n" +
            "}                                            \n";


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        program = GLUtil.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        position = GLES30.glGetAttribLocation(program, "vPosition");
        Log.d(TAG, "mPositionHandle: " + position);
        GLES30.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        int vertexCount = 3;
        // OpenGL的世界坐标系是 [-1, -1, 1, 1]
        float[] vertices = new float[]{
                0.0f, 0.5f, 0, // 第一个点（x, y, z）
                -0.5f, -0.5f, 0, // 第二个点（x, y, z）
                0.5f, -0.5f, 0 // 第三个点（x, y, z）
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4); // 一个 float 是四个字节
        vbb.order(ByteOrder.nativeOrder()); // 必须要是 native order
        FloatBuffer vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0); // 这一行不要漏了

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT); // clear color buffer
        // 1. 选择使用的程序
        GLES30.glUseProgram(program);
        // 2. 加载顶点数据
        GLES30.glVertexAttribPointer(position, vertexCount, GLES30.GL_FLOAT, false, 3 * 4, vertexBuffer);
        GLES30.glEnableVertexAttribArray(position);
        // 3. 绘制
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount);
    }
}
