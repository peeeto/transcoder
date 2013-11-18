



package org.pch;

import javax.swing.*;

public class Transcoder {

  public static void main(String args[]) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        TranscoderFrame tf = new TranscoderFrame();
        tf.setVisible(true);
        tf.setExtendedState(JFrame.NORMAL);
      }
    });
  }
}
