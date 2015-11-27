package com.petercho;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class Transcoder {

    private static final AtomicReference<Component> componentRef = new AtomicReference<>();

    public static Component getInstance() {
        return componentRef.get();
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TranscoderFrame tf = new TranscoderFrame();
                componentRef.set(tf);
                tf.pack();
                tf.setVisible(true);
            }
        });
        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
            e.printStackTrace();
            JOptionPane.showMessageDialog(componentRef.get(),
                    e + "",
                    "Unexpected Error",
                    JOptionPane.ERROR_MESSAGE
            );
        });
    }
}
