package mymacro;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Main implements NativeKeyListener {

    private boolean didCTRL = false;

    public static void main(String[] args) {
        new Main(args);
    }

    public Main(String[] args) {
        System.out.println("initialize complete please wait...");

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));

        if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
            }
        } else if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.CTRL_MASK && !this.didCTRL) {
            this.didCTRL = true;
        } else if (nativeKeyEvent.getKeyCode() == 41 && this.didCTRL) {
            mediaVolumeUP();
        } else if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_TAB && this.didCTRL) {
            mediaVolumeDOWN();
        } else if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_CAPS_LOCK && this.didCTRL) {
            mediaPause();
        } else if (nativeKeyEvent.getKeyCode() == NativeInputEvent.SHIFT_L_MASK && this.didCTRL) {
            mediaFastForward();
        } else if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.CTRL_MASK) {
            mediaRewind();
        }

        this.didCTRL = false;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }

    public void mediaVolumeUP() {
        GlobalScreen.postNativeEvent(new NativeKeyEvent(2401, 0, 175, 57392, NativeKeyEvent.CHAR_UNDEFINED));
    }

    public void mediaVolumeDOWN() {
        GlobalScreen.postNativeEvent(new NativeKeyEvent(2401, 0, 174, 57390, NativeKeyEvent.CHAR_UNDEFINED));
    }

    public void mediaPause() {
        GlobalScreen.postNativeEvent(new NativeKeyEvent(2401, 0, 179, 57378, NativeKeyEvent.CHAR_UNDEFINED));
    }

    public void mediaFastForward() {
        GlobalScreen.postNativeEvent(new NativeKeyEvent(2401, 0, 176, 57369, NativeKeyEvent.CHAR_UNDEFINED));
    }

    public void mediaRewind() {
        GlobalScreen.postNativeEvent(new NativeKeyEvent(2401, 0, 177, 57360, NativeKeyEvent.CHAR_UNDEFINED));
    }

    public void openFrameID(NativeKeyEvent nativeKeyEvent) {
        String findData = nativeKeyEvent.getID() + "," + nativeKeyEvent.getModifiers() + "," + nativeKeyEvent.getRawCode() + "," + nativeKeyEvent.getKeyCode();

        Frame frame = new Frame("Find DATA");
        Component text = new TextArea(findData);
        frame.add(text, BorderLayout.NORTH);
        int width = 300;
        int height = 300;
        frame.setSize(width, height);
        frame.setVisible(true);

        StringSelection stringSelection = new StringSelection(findData);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }
}
