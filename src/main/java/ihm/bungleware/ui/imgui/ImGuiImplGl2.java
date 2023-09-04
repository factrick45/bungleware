package ihm.bungleware.ui.imgui;

import java.nio.ByteBuffer;

import imgui.ImDrawData;
import imgui.ImGui;
import imgui.flag.ImGuiBackendFlags;
import imgui.type.ImInt;
import static org.lwjgl.opengl.GL11.*;

/** Implementation of the ImGui renderer for legacy OpenGL. */
public class ImGuiImplGl2 {

    private int FontTexture = -1;

    public void init() {
        var io = ImGui.getIO();
        io.setBackendRendererName("bungleware_opengl2");
    }

    public void newFrame() {
        if (FontTexture != -1)
            return;
        // generate font texture
        var io = ImGui.getIO();
        var width = new ImInt();
        var height = new ImInt();
        ByteBuffer image = io.getFonts().getTexDataAsRGBA32(width, height);

        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glPixelStorei(GL_UNPACK_ROW_LENGTH, 0);
        glTexImage2D(
            GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA,
            GL_UNSIGNED_BYTE, image
        );
        io.getFonts().setTexID(tex);
    }

    private void setupRenderState(int fbWidth, int fbHeight) {
        glPushAttrib(GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT | GL_TRANSFORM_BIT);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_STENCIL_TEST);
        glDisable(GL_LIGHTING);
        glDisable(GL_COLOR_MATERIAL);
        glEnable(GL_SCISSOR_TEST);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glEnable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glShadeModel(GL_SMOOTH);
        glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

        glViewport(0, 0, fbWidth, fbHeight);
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, fbWidth, fbHeight, 0, -1.0f, 1.0f);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
    }

    public void renderDrawData(ImDrawData drawData) {
        int fbWidth =
            (int)(drawData.getDisplaySizeX() * drawData.getFramebufferScaleX());
        int fbHeight =
            (int)(drawData.getDisplaySizeY() * drawData.getFramebufferScaleY());

        float clipoffx = drawData.getDisplayPosX();
        float clipoffy = drawData.getDisplayPosY();
        float clipscalex = drawData.getFramebufferScaleX();
        float clipscaley = drawData.getFramebufferScaleY();

        setupRenderState(fbWidth, fbHeight);
        drawData.deIndexAllBuffers();

        for (int i = 0; i < drawData.getCmdListsCount(); i++) {
            ByteBuffer vtxbuffer = drawData.getCmdListVtxBufferData(i);

            int stride = ImDrawData.sizeOfImDrawVert();
            glVertexPointer(2, GL_FLOAT, stride, vtxbuffer.position(0));
            glTexCoordPointer(2, GL_FLOAT, stride, vtxbuffer.position(8));
            glColorPointer(4, GL_UNSIGNED_BYTE, stride, vtxbuffer.position(16));

            int vtxoff = 0;
            for (int j = 0; j < drawData.getCmdListCmdBufferSize(i); j++) {
                var cr = drawData.getCmdListCmdBufferClipRect(i, j);
                float c1x = (cr.x - clipoffx) * clipscalex;
                float c1y = (cr.y - clipoffy) * clipscaley;
                float c2x = (cr.z - clipoffx) * clipscalex;
                float c2y = (cr.w - clipoffy) * clipscaley;

                if (c2x <= c1x || c2y <= c1y)
                  continue;

                glScissor(
                    (int)c1x, (int)(fbHeight - c2y),
                    (int)(c2x - c1x), (int)(c2y - c1y)
                );

                int tex = drawData.getCmdListCmdBufferTextureId(i, j);
                int vtxcount = drawData.getCmdListCmdBufferElemCount(i, j);

                glBindTexture(GL_TEXTURE_2D, tex);
                glDrawArrays(GL_TRIANGLES, vtxoff, vtxcount);

                vtxoff += vtxcount;
            }
        }

        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);

        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glPopAttrib();
    }
}
