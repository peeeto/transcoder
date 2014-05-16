package org.pch;

import java.awt.Component;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

public class Transcoder {

    public static void main(String args[]) {
        final AtomicReference<Component> componentRef = new AtomicReference<Component>();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JDialog tf = new TranscoderFrame();
                componentRef.set(tf);
//                tf.setExtendedState(JFrame.NORMAL);
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
