package net.plastoid501.throwitems.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class KeyCodeUtil {
    private static final Map<String, Integer> keyNameToCodeMap = new HashMap<>();
    private static final Map<Integer, String> keyCodeToNameMap = new HashMap<>();
    private static final List<Integer> pressedKeys = new ArrayList<>();
    public static List<Integer> lastPressedKeys = new ArrayList<>();

    static {
        keyNameToCodeMap.put("BUTTON_LEFT", GLFW.GLFW_MOUSE_BUTTON_LEFT);
        keyNameToCodeMap.put("BUTTON_RIGHT", GLFW.GLFW_MOUSE_BUTTON_RIGHT);
        keyNameToCodeMap.put("BUTTON_MIDDLE", GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
        keyNameToCodeMap.put("BUTTON_4", GLFW.GLFW_MOUSE_BUTTON_4);
        keyNameToCodeMap.put("BUTTON_5", GLFW.GLFW_MOUSE_BUTTON_5);
        keyNameToCodeMap.put("BUTTON_6", GLFW.GLFW_MOUSE_BUTTON_6);
        keyNameToCodeMap.put("BUTTON_7", GLFW.GLFW_MOUSE_BUTTON_7);
        keyNameToCodeMap.put("BUTTON_8", GLFW.GLFW_MOUSE_BUTTON_8);
        keyNameToCodeMap.put("0", GLFW.GLFW_KEY_0);
        keyNameToCodeMap.put("1", GLFW.GLFW_KEY_1);
        keyNameToCodeMap.put("2", GLFW.GLFW_KEY_2);
        keyNameToCodeMap.put("3", GLFW.GLFW_KEY_3);
        keyNameToCodeMap.put("4", GLFW.GLFW_KEY_4);
        keyNameToCodeMap.put("5", GLFW.GLFW_KEY_5);
        keyNameToCodeMap.put("6", GLFW.GLFW_KEY_6);
        keyNameToCodeMap.put("7", GLFW.GLFW_KEY_7);
        keyNameToCodeMap.put("8", GLFW.GLFW_KEY_8);
        keyNameToCodeMap.put("9", GLFW.GLFW_KEY_9);
        keyNameToCodeMap.put("A", GLFW.GLFW_KEY_A);
        keyNameToCodeMap.put("B", GLFW.GLFW_KEY_B);
        keyNameToCodeMap.put("C", GLFW.GLFW_KEY_C);
        keyNameToCodeMap.put("D", GLFW.GLFW_KEY_D);
        keyNameToCodeMap.put("E", GLFW.GLFW_KEY_E);
        keyNameToCodeMap.put("F", GLFW.GLFW_KEY_F);
        keyNameToCodeMap.put("G", GLFW.GLFW_KEY_G);
        keyNameToCodeMap.put("H", GLFW.GLFW_KEY_H);
        keyNameToCodeMap.put("I", GLFW.GLFW_KEY_I);
        keyNameToCodeMap.put("J", GLFW.GLFW_KEY_J);
        keyNameToCodeMap.put("K", GLFW.GLFW_KEY_K);
        keyNameToCodeMap.put("L", GLFW.GLFW_KEY_L);
        keyNameToCodeMap.put("M", GLFW.GLFW_KEY_M);
        keyNameToCodeMap.put("N", GLFW.GLFW_KEY_N);
        keyNameToCodeMap.put("O", GLFW.GLFW_KEY_O);
        keyNameToCodeMap.put("P", GLFW.GLFW_KEY_P);
        keyNameToCodeMap.put("Q", GLFW.GLFW_KEY_Q);
        keyNameToCodeMap.put("R", GLFW.GLFW_KEY_R);
        keyNameToCodeMap.put("S", GLFW.GLFW_KEY_S);
        keyNameToCodeMap.put("T", GLFW.GLFW_KEY_T);
        keyNameToCodeMap.put("U", GLFW.GLFW_KEY_U);
        keyNameToCodeMap.put("V", GLFW.GLFW_KEY_V);
        keyNameToCodeMap.put("W", GLFW.GLFW_KEY_W);
        keyNameToCodeMap.put("X", GLFW.GLFW_KEY_X);
        keyNameToCodeMap.put("Y", GLFW.GLFW_KEY_Y);
        keyNameToCodeMap.put("Z", GLFW.GLFW_KEY_Z);
        keyNameToCodeMap.put("F1", GLFW.GLFW_KEY_F1);
        keyNameToCodeMap.put("F2", GLFW.GLFW_KEY_F2);
        keyNameToCodeMap.put("F3", GLFW.GLFW_KEY_F3);
        keyNameToCodeMap.put("F4", GLFW.GLFW_KEY_F4);
        keyNameToCodeMap.put("F5", GLFW.GLFW_KEY_F5);
        keyNameToCodeMap.put("F6", GLFW.GLFW_KEY_F6);
        keyNameToCodeMap.put("F7", GLFW.GLFW_KEY_F7);
        keyNameToCodeMap.put("F8", GLFW.GLFW_KEY_F8);
        keyNameToCodeMap.put("F9", GLFW.GLFW_KEY_F9);
        keyNameToCodeMap.put("F10", GLFW.GLFW_KEY_F10);
        keyNameToCodeMap.put("F11", GLFW.GLFW_KEY_F11);
        keyNameToCodeMap.put("F12", GLFW.GLFW_KEY_F12);
        keyNameToCodeMap.put("F13", GLFW.GLFW_KEY_F13);
        keyNameToCodeMap.put("F14", GLFW.GLFW_KEY_F14);
        keyNameToCodeMap.put("F15", GLFW.GLFW_KEY_F15);
        keyNameToCodeMap.put("F16", GLFW.GLFW_KEY_F16);
        keyNameToCodeMap.put("F17", GLFW.GLFW_KEY_F17);
        keyNameToCodeMap.put("F18", GLFW.GLFW_KEY_F18);
        keyNameToCodeMap.put("F19", GLFW.GLFW_KEY_F19);
        keyNameToCodeMap.put("F20", GLFW.GLFW_KEY_F20);
        keyNameToCodeMap.put("F21", GLFW.GLFW_KEY_F21);
        keyNameToCodeMap.put("F22", GLFW.GLFW_KEY_F22);
        keyNameToCodeMap.put("F23", GLFW.GLFW_KEY_F23);
        keyNameToCodeMap.put("F24", GLFW.GLFW_KEY_F24);
        keyNameToCodeMap.put("F25", GLFW.GLFW_KEY_F25);
        keyNameToCodeMap.put("NUM_LOCK", GLFW.GLFW_KEY_NUM_LOCK);
        keyNameToCodeMap.put("KP_0", GLFW.GLFW_KEY_KP_0);
        keyNameToCodeMap.put("KP_1", GLFW.GLFW_KEY_KP_1);
        keyNameToCodeMap.put("KP_2", GLFW.GLFW_KEY_KP_2);
        keyNameToCodeMap.put("KP_3", GLFW.GLFW_KEY_KP_3);
        keyNameToCodeMap.put("KP_4", GLFW.GLFW_KEY_KP_4);
        keyNameToCodeMap.put("KP_5", GLFW.GLFW_KEY_KP_5);
        keyNameToCodeMap.put("KP_6", GLFW.GLFW_KEY_KP_6);
        keyNameToCodeMap.put("KP_7", GLFW.GLFW_KEY_KP_7);
        keyNameToCodeMap.put("KP_8", GLFW.GLFW_KEY_KP_8);
        keyNameToCodeMap.put("KP_9", GLFW.GLFW_KEY_KP_9);
        keyNameToCodeMap.put("KP_ADD", GLFW.GLFW_KEY_KP_ADD);
        keyNameToCodeMap.put("KP_DECIMAL", GLFW.GLFW_KEY_KP_DECIMAL);
        keyNameToCodeMap.put("KP_ENTER", GLFW.GLFW_KEY_KP_ENTER);
        keyNameToCodeMap.put("KP_EQUAL", GLFW.GLFW_KEY_KP_EQUAL);
        keyNameToCodeMap.put("KP_MULTIPLY", GLFW.GLFW_KEY_KP_MULTIPLY);
        keyNameToCodeMap.put("KP_DIVIDE", GLFW.GLFW_KEY_KP_DIVIDE);
        keyNameToCodeMap.put("KP_SUBTRACT", GLFW.GLFW_KEY_KP_SUBTRACT);
        keyNameToCodeMap.put("DOWN", GLFW.GLFW_KEY_DOWN);
        keyNameToCodeMap.put("LEFT", GLFW.GLFW_KEY_LEFT);
        keyNameToCodeMap.put("RIGHT", GLFW.GLFW_KEY_RIGHT);
        keyNameToCodeMap.put("UP", GLFW.GLFW_KEY_UP);
        keyNameToCodeMap.put("APOSTROPHE", GLFW.GLFW_KEY_APOSTROPHE);
        keyNameToCodeMap.put("BACKSLASH", GLFW.GLFW_KEY_BACKSLASH);
        keyNameToCodeMap.put("COMMA", GLFW.GLFW_KEY_COMMA);
        keyNameToCodeMap.put("EQUAL", GLFW.GLFW_KEY_EQUAL);
        keyNameToCodeMap.put("GRAVE_ACCENT", GLFW.GLFW_KEY_GRAVE_ACCENT);
        keyNameToCodeMap.put("LEFT_BRACKET", GLFW.GLFW_KEY_LEFT_BRACKET);
        keyNameToCodeMap.put("MINUS", GLFW.GLFW_KEY_MINUS);
        keyNameToCodeMap.put("PERIOD", GLFW.GLFW_KEY_PERIOD);
        keyNameToCodeMap.put("RIGHT_BRACKET", GLFW.GLFW_KEY_RIGHT_BRACKET);
        keyNameToCodeMap.put("SEMICOLON", GLFW.GLFW_KEY_SEMICOLON);
        keyNameToCodeMap.put("SLASH", GLFW.GLFW_KEY_SLASH);
        keyNameToCodeMap.put("SPACE", GLFW.GLFW_KEY_SPACE);
        keyNameToCodeMap.put("TAB", GLFW.GLFW_KEY_TAB);
        keyNameToCodeMap.put("LEFT_ALT", GLFW.GLFW_KEY_LEFT_ALT);
        keyNameToCodeMap.put("LEFT_CONTROL", GLFW.GLFW_KEY_LEFT_CONTROL);
        keyNameToCodeMap.put("LEFT_SHIFT", GLFW.GLFW_KEY_LEFT_SHIFT);
        keyNameToCodeMap.put("LEFT_WIN", 343);
        keyNameToCodeMap.put("RIGHT_ALT", GLFW.GLFW_KEY_RIGHT_ALT);
        keyNameToCodeMap.put("RIGHT_CONTROL", GLFW.GLFW_KEY_RIGHT_CONTROL);
        keyNameToCodeMap.put("RIGHT_SHIFT", GLFW.GLFW_KEY_RIGHT_SHIFT);
        keyNameToCodeMap.put("RIGHT_WIN", 347);
        keyNameToCodeMap.put("ENTER", GLFW.GLFW_KEY_ENTER);
        keyNameToCodeMap.put("ESCAPE", GLFW.GLFW_KEY_ESCAPE);
        keyNameToCodeMap.put("BACKSPACE", GLFW.GLFW_KEY_BACKSPACE);
        keyNameToCodeMap.put("DELETE", GLFW.GLFW_KEY_DELETE);
        keyNameToCodeMap.put("END", GLFW.GLFW_KEY_END);
        keyNameToCodeMap.put("HOME", GLFW.GLFW_KEY_HOME);
        keyNameToCodeMap.put("INSERT", GLFW.GLFW_KEY_INSERT);
        keyNameToCodeMap.put("PAGE_DOWN", GLFW.GLFW_KEY_PAGE_DOWN);
        keyNameToCodeMap.put("PAGE_UP", GLFW.GLFW_KEY_PAGE_UP);
        keyNameToCodeMap.put("CAPS_LOCK", GLFW.GLFW_KEY_CAPS_LOCK);
        keyNameToCodeMap.put("PAUSE", GLFW.GLFW_KEY_PAUSE);
        keyNameToCodeMap.put("SCROLL_LOCK", GLFW.GLFW_KEY_SCROLL_LOCK);
        keyNameToCodeMap.put("MENU", GLFW.GLFW_KEY_MENU);
        keyNameToCodeMap.put("PRINT_SCREEN", GLFW.GLFW_KEY_PRINT_SCREEN);
        keyNameToCodeMap.put("WORLD_1", GLFW.GLFW_KEY_WORLD_1);
        keyNameToCodeMap.put("WORLD_2", GLFW.GLFW_KEY_WORLD_2);

        for (Map.Entry<String, Integer> entry : keyNameToCodeMap.entrySet()) {
            keyCodeToNameMap.put(entry.getValue(), entry.getKey());
        }
    }

    public static List<Integer> getCodeForKey(List<String> keyNames) {
        List<Integer> keyCodes = new ArrayList<>();
        for (String keyName : keyNames){
            keyCodes.add(keyNameToCodeMap.get(keyName));
        }
        return keyCodes;
    }

    public static String getKeyForCode(int keyCode) {
        return keyCodeToNameMap.get(keyCode);
    }

    public static List<String> getKeyForCode(List<Integer> keyCodes) {
        List<String> keyNames = new ArrayList<>();
        for (Integer keyCode : keyCodes){
            keyNames.add(keyCodeToNameMap.get(keyCode));
        }
        return keyNames;
    }

    public static List<Integer> getAllKeyCode(){
        return new ArrayList<>(keyNameToCodeMap.values());
    }

    public static boolean updateKeyStatus() {
        lastPressedKeys = new ArrayList<>(pressedKeys);
        boolean isChange = false;
        for (Integer keyCode : lastPressedKeys) {
            if (keyCode <= 7) {
                if (!isMousePressed(keyCode)) {
                    pressedKeys.remove(keyCode);
                    isChange = true;
                }
            } else {
                if (!isKeyPressed(keyCode)) {
                    pressedKeys.remove(keyCode);
                    isChange = true;
                }
            }
        }
        return isChange;
    }

    public static void add(int value) {
        lastPressedKeys = new ArrayList<>(pressedKeys);
        if (!pressedKeys.contains(value)) {
            pressedKeys.add(value);
        }
    }

    public static void remove(int value) {
        lastPressedKeys = new ArrayList<>(pressedKeys);
        pressedKeys.remove((Object) value);
    }

    public static boolean isKeyPressed(int keyCode) {
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), keyCode);
    }

    public static boolean isMousePressed(int button) {
        long windowHandle = MinecraftClient.getInstance().getWindow().getHandle();
        GLFW.glfwMakeContextCurrent(windowHandle);
        return GLFW.glfwGetMouseButton(windowHandle, button) == GLFW.GLFW_PRESS;
    }

    public static List<Integer> getPressedKeys() {
        return pressedKeys;
    }

    public static List<Integer> getLastPressedKeys() {
        return lastPressedKeys;
    }

    public static boolean matchKeyCodes(List<Integer> keyCodes) {
        return !keyCodes.isEmpty() && KeyCodeUtil.getPressedKeys().size() == keyCodes.size() && new HashSet<>(KeyCodeUtil.getPressedKeys()).containsAll(keyCodes);
    }

}
