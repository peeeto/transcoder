
package org.pch;

import javax.swing.*;

public class Transcoder {

  public static void main(String args[]) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        TranscoderFrame tf = new TranscoderFrame();
        tf.addWindowListener(new java.awt.event.WindowAdapter() {
          public void windowClosing(java.awt.event.WindowEvent evt) {
            System.exit(0);
          }
        });
        tf.setVisible(true);
      }
    });
  }
}
