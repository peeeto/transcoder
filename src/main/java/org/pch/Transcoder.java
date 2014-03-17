package org.pch;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

public class Transcoder {

    public static void main(String args[]) {
        final AtomicReference<JFrame> frameRef = new AtomicReference<JFrame>();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame tf = new TranscoderFrame();
                frameRef.set(tf);
                tf.setExtendedState(JFrame.NORMAL);
                tf.setVisible(true);
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frameRef.get(),
                        e + "",
                        "Unexpected Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        );
    }
}
