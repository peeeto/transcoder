package org.pch;

import java.awt.Component;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

public class Transcoder {

    private static final AtomicReference<Component> componentRef = new AtomicReference<Component>();

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
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(componentRef.get(),
                        e + "",
                        "Unexpected Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        );
    }
}
