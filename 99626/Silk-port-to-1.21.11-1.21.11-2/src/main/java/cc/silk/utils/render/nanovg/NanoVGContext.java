package cc.silk.utils.render.nanovg;

import cc.silk.SilkClient;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;

import static org.lwjgl.nanovg.NanoVGGL3.*;

public class NanoVGContext {
    @Getter
    private static long handle = 0;
    private static boolean initialized = false;
    private static boolean nativeUnavailable = false;

    public static boolean init() {
        if (initialized && isValid()) {
            return true;
        }
        if (nativeUnavailable) {
            return false;
        }

        RenderSystem.assertOnRenderThread();

        if (handle != 0) {
            cleanup();
        }

        try {
            handle = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        } catch (LinkageError error) {
            nativeUnavailable = true;
            SilkClient.INSTANCE.getLogger().warn(
                    "NanoVG native library could not be loaded; the standard ClickGUI will be used instead.",
                    error);
            return false;
        }

        if (!isValid()) {
            throw new RuntimeException("Failed to initialize NanoVG");
        }

        initialized = true;
        return true;
    }

    public static void reinit() {
        cleanup();
        init();
    }

    public static boolean isValid() {
        return handle != 0 && handle != -1;
    }

    public static void assertValid() {
        if (!isValid()) {
            throw new IllegalStateException("Invalid NanoVG context");
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void cleanup() {
        if (handle != 0) {
            try {
                nvgDelete(handle);
            } catch (Exception ignored) {
            }
            handle = 0;
        }
        initialized = false;
    }
}
