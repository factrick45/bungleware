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
        io.setBackendRendererName("imgui_java_impl_opengl2");
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

        setupRenderState(
            fbWidth,
            fbHeight
        );

        float clipoffx = drawData.getDisplayPosX();
        float clipoffy = drawData.getDisplayPosY();
        float clipscalex = drawData.getFramebufferScaleX();
        float clipscaley = drawData.getFramebufferScaleY();

        for (int i = 0; i < drawData.getCmdListsCount(); i++) {
            ByteBuffer vtxbuffer = drawData.getCmdListVtxBufferData(i);
            ByteBuffer idxbuffer = drawData.getCmdListIdxBufferData(i);

            int stride = ImDrawData.sizeOfImDrawVert();
            glVertexPointer(2, GL_FLOAT, stride, vtxbuffer.position(0));
            glTexCoordPointer(2, GL_FLOAT, stride, vtxbuffer.position(8));
            glColorPointer(4, GL_UNSIGNED_BYTE, stride, vtxbuffer.position(16));

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

                // hightlight clipping rectangles
                /*
                glBegin(GL_TRIANGLES);
                glColor3f(1.0f, 0.0f, 0.0f);
                glVertex3f(c1x, c1y, 0.0f);
                glColor3f(0.0f, 1.0f, 0.0f);
                glVertex3f(c1x, c2y, 0.0f);
                glColor3f(0.0f, 0.0f, 1.0f);
                glVertex3f(c2x, c1y, 0.0f);

                glColor3f(0.0f, 1.0f, 1.0f);
                glVertex3f(c2x, c2y, 0.0f);
                glColor3f(0.0f, 0.0f, 1.0f);
                glVertex3f(c2x, c1y, 0.0f);
                glColor3f(0.0f, 1.0f, 0.0f);
                glVertex3f(c1x, c2y, 0.0f);
                glEnd();
                */

                int tex = drawData.getCmdListCmdBufferTextureId(i, j);
                int elemc = drawData.getCmdListCmdBufferElemCount(i, j);
                int idxoff =
                    ImDrawData.sizeOfImDrawIdx() * // might be wrong
                    drawData.getCmdListCmdBufferIdxOffset(i, j);
                int type = ImDrawData.sizeOfImDrawIdx() == 2 ?
                    GL_UNSIGNED_SHORT : GL_UNSIGNED_INT;

                /*
                idxbuffer.position(idxoff);
                for (int ii = 0; ii < elemc; ii += 2) {
                    System.out.println(idxbuffer.getShort());
                }
                ihm.chuckware.utils.Utils.breakingBad(false);
                */

                //ihm.chuckware.utils.Utils.breakingBad(false);

                glBindTexture(GL_TEXTURE_2D, tex);
                glDrawElements(
                    GL_TRIANGLES, elemc, type, idxbuffer.position(idxoff)
                );
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
