package org.pch;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Hashtable;

public class TranscoderFrame extends javax.swing.JFrame implements ClipboardOwner {

  /**
   *
   */
  private static final long serialVersionUID = 5712516501903313109L;
  protected UndoManager undo = new UndoManager();
  protected UndoAction undoAction;
  protected RedoAction redoAction;
  private Hashtable<Object, Action> actions;

  /**
   * Creates new form TranscoderFrame
   */
  public TranscoderFrame() {
    initComponents();
    createActionTable(textPane);

    undo.setLimit(10);
    undoAction = new UndoAction();
    editMenu.add(undoAction);
    redoAction = new RedoAction();
    editMenu.add(redoAction);

    editMenu.addSeparator();

    //These actions come from the default editor kit.
    //Get the ones we want and stick them in the menu.
    editMenu.add(getActionByName(DefaultEditorKit.cutAction));
    editMenu.add(getActionByName(DefaultEditorKit.copyAction));
    editMenu.add(getActionByName(DefaultEditorKit.pasteAction));

    editMenu.addSeparator();

    editMenu.add(getActionByName(DefaultEditorKit.selectAllAction));

    final Document d = textPane.getDocument();
    d.addUndoableEditListener(new MyUndoableEditListener());
    d.addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent evt) {
        update();
      }

      public void insertUpdate(DocumentEvent evt) {
        update();
      }

      public void removeUpdate(DocumentEvent evt) {
        update();
      }

      private void update() {
        countLabel.setText("Characters: " + d.getLength());
      }
    });

    initTrayComponents();
  }

  //Obtain the images URL
  protected static Image createImage(String path, String description) {
    URL imageURL = TranscoderFrame.class.getClassLoader().getResource(path);
    return (new ImageIcon(imageURL, description)).getImage();
  }

  private void initTrayComponents() {
    final SystemTray tray = SystemTray.getSystemTray();
    final Image image = createImage("images/bulb.gif", "transcoder");
    ActionListener exitListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("Exiting....");
        System.exit(0);
      }
    };
    PopupMenu popup = new PopupMenu();
    MenuItem defaultItem = new MenuItem("Exit");
    defaultItem.addActionListener(exitListener);
    popup.add(defaultItem);
    defaultItem = new MenuItem("Open");
    final ActionListener setVisible = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setVisible(true);
        setExtendedState(JFrame.NORMAL);
      }
    };
    defaultItem.addActionListener(setVisible);
    popup.add(defaultItem);
    final TrayIcon trayIcon = new TrayIcon(image, "transcoder", popup);
    trayIcon.setImageAutoSize(true);
    trayIcon.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setVisible(!isVisible());
        setExtendedState(JFrame.NORMAL);
      }
    });
    addWindowStateListener(new WindowStateListener() {
      public void windowStateChanged(WindowEvent e) {
        if (e.getNewState() == ICONIFIED) {
          try {
            tray.add(trayIcon);
            setVisible(false);
            System.out.println("added to SystemTray");
          } catch (AWTException ex) {
            System.out.println("unable to add to tray");
          }
        }
        if (e.getNewState() == 7) {
          try {
            tray.add(trayIcon);
            setVisible(false);
            System.out.println("added to SystemTray");
          } catch (AWTException ex) {
            System.out.println("unable to add to system tray");
          }
        }
        if (e.getNewState() == MAXIMIZED_BOTH) {
          tray.remove(trayIcon);
          setVisible(true);
          System.out.println("Tray icon removed");
        }
        if (e.getNewState() == NORMAL) {
          tray.remove(trayIcon);
          setVisible(true);
          System.out.println("Tray icon removed");
        }
      }
    });
    setIconImage(Toolkit.getDefaultToolkit().getImage("Duke256.png"));
    setVisible(true);
    setSize(300, 200);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }

  //The following two methods allow us to find an
  //action provided by the editor kit by its name.
  private void createActionTable(JTextComponent textComponent) {
    actions = new Hashtable<Object, Action>();
    Action[] actionsArray = textComponent.getActions();
    for (int i = 0; i < actionsArray.length; i++) {
      Action a = actionsArray[i];
      actions.put(a.getValue(Action.NAME), a);
    }
  }

  private Action getActionByName(String name) {
    return (Action) (actions.get(name));
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jScrollPane1 = new javax.swing.JScrollPane();
    textPane = new javax.swing.JTextPane();
    jPanel1 = new javax.swing.JPanel();
    urlEncodeButton = new javax.swing.JButton();
    urlDecodeButton = new javax.swing.JButton();
    base64EncodeButton = new javax.swing.JButton();
    base64DecodeButton = new javax.swing.JButton();
    md5hashButton = new javax.swing.JButton();
    sha1hashButton = new javax.swing.JButton();
    countLabel = new javax.swing.JLabel();
    jMenuBar1 = new javax.swing.JMenuBar();
    editMenu = new javax.swing.JMenu();

    setTitle("Transcoder");

    textPane.setFont(new java.awt.Font("Monospaced", 0, 12));
    jScrollPane1.setViewportView(textPane);

    getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

    jPanel1.setLayout(new java.awt.GridBagLayout());

    urlEncodeButton.setText("URL encode");
    urlEncodeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlEncodeButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 1.0;
    jPanel1.add(urlEncodeButton, gridBagConstraints);

    urlDecodeButton.setText("URL decode");
    urlDecodeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        urlDecodeButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 1.0;
    jPanel1.add(urlDecodeButton, gridBagConstraints);

    base64EncodeButton.setText("Base64 encode");
    base64EncodeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        base64EncodeButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 1.0;
    jPanel1.add(base64EncodeButton, gridBagConstraints);

    base64DecodeButton.setText("Base64 decode");
    base64DecodeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        base64DecodeButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.weightx = 1.0;
    jPanel1.add(base64DecodeButton, gridBagConstraints);

    md5hashButton.setText("MD5 hash");
    md5hashButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        md5hashButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 1.0;
    jPanel1.add(md5hashButton, gridBagConstraints);

    sha1hashButton.setText("SHA-1 hash");
    sha1hashButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sha1hashButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 1.0;
    jPanel1.add(sha1hashButton, gridBagConstraints);

    getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

    countLabel.setText("Characters: 0");
    getContentPane().add(countLabel, java.awt.BorderLayout.NORTH);

    editMenu.setText("Edit");
    jMenuBar1.add(editMenu);

    setJMenuBar(jMenuBar1);

    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((screenSize.width - 600) / 2, (screenSize.height - 400) / 2, 600, 400);
  }// </editor-fold>//GEN-END:initComponents

  private void sha1hashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha1hashButtonActionPerformed
    if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
      textPane.select(0, textPane.getText().length());
    }
    if (textPane.getSelectedText() != null) {
      textPane.replaceSelection(DigestUtils.sha1Hex(textPane.getSelectedText()));
    }
  }//GEN-LAST:event_sha1hashButtonActionPerformed

  private void md5hashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_md5hashButtonActionPerformed
    if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
      textPane.select(0, textPane.getText().length());
    }
    if (textPane.getSelectedText() != null) {
      textPane.replaceSelection(DigestUtils.md5Hex(textPane.getSelectedText()));
    }
  }//GEN-LAST:event_md5hashButtonActionPerformed

  private void base64DecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_base64DecodeButtonActionPerformed
    try {
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
        textPane.select(0, textPane.getText().length());
      }
      if (textPane.getSelectedText() != null) {
        textPane.replaceSelection(new String(Base64.decodeBase64(textPane.getSelectedText())));
      }
    } catch (Throwable t) {
      Runtime.getRuntime().gc();
      t.printStackTrace();
      textPane.setText("Exception! " + t.toString());
    }
  }//GEN-LAST:event_base64DecodeButtonActionPerformed

  private void urlDecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlDecodeButtonActionPerformed
    if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
      textPane.select(0, textPane.getText().length());
    }
    textPane.replaceSelection(urlDecode(textPane.getSelectedText()));
  }//GEN-LAST:event_urlDecodeButtonActionPerformed

  private void urlEncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlEncodeButtonActionPerformed
    if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
      textPane.select(0, textPane.getText().length());
    }
    textPane.replaceSelection(urlEncode(textPane.getSelectedText()));
  }//GEN-LAST:event_urlEncodeButtonActionPerformed

  private void base64EncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_base64EncodeButtonActionPerformed
    try {
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
        textPane.select(0, textPane.getText().length());
      }
      textPane.replaceSelection(Base64.encodeBase64String(textPane.getSelectedText().getBytes()));
    } catch (Throwable t) {
      Runtime.getRuntime().gc();
      t.printStackTrace();
      textPane.setText("Exception! " + t.toString());
    }
  }//GEN-LAST:event_base64EncodeButtonActionPerformed

  /**
   * Notifies this object that it is no longer the owner of the contents of the clipboard.
   *
   * @param clipboard the clipboard that is no longer owned
   * @param contents  the contents which this owner had placed on the clipboard
   */
  public void lostOwnership(Clipboard clipboard, Transferable contents) {
  }


  /**
   *  Description of the Method
   *
   *@param  str  Description of the Parameter
   *@return Description of the Return Value
   */
    /* 
    public static String unicodeDecode( String str ) {
        // FIXME: TOTALLY EXPERIMENTAL
        try {
            ByteBuffer bbuf = ByteBuffer.allocate( str.length() );
            bbuf.put( str.getBytes() );
            Charset charset = Charset.forName( "ISO-8859-1" );
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer cbuf = decoder.decode( bbuf );
            return ( cbuf.toString() );
        }
        catch ( Exception e ) {
            return ( "Encoding problem" );
        }
    }
     */


  /**
   *  Description of the Method
   *
   *@param  str  Description of the Parameter
   *@return Description of the Return Value
   */
    /*
    public static String unicodeEncode( String str ) {
        // FIXME: TOTALLY EXPERIMENTAL
        try {
            Charset charset = Charset.forName( "ISO-8859-1" );
            CharsetEncoder encoder = charset.newEncoder();
            ByteBuffer bbuf = encoder.encode( CharBuffer.wrap( str ) );
            return ( new String( bbuf.array() ) );
        }
        catch ( Exception e ) {
            return ( "Encoding problem" );
        }
    }
     */


  /**
   * Description of the Method
   *
   * @param str Description of the Parameter
   *
   * @return Description of the Return Value
   */
  public static String urlDecode(String str) {
    try {
      return (URLDecoder.decode(str, "utf-8"));
    } catch (Exception e) {
      return ("Decoding error");
    }
  }


  /**
   * Description of the Method
   *
   * @param str Description of the Parameter
   *
   * @return Description of the Return Value
   */
  public static String urlEncode(String str) {
    try {
      return (URLEncoder.encode(str, "utf-8"));
    } catch (Exception e) {
      return ("Encoding error");
    }
  }


  /** Exit the Application */
  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    TranscoderFrame tf = new TranscoderFrame();
    tf.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        System.exit(0);
      }
    });
    tf.setVisible(true);
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton base64DecodeButton;
  private javax.swing.JButton base64EncodeButton;
  private javax.swing.JLabel countLabel;
  private javax.swing.JMenu editMenu;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JButton md5hashButton;
  private javax.swing.JButton sha1hashButton;
  private javax.swing.JTextPane textPane;
  private javax.swing.JButton urlDecodeButton;
  private javax.swing.JButton urlEncodeButton;
// End of variables declaration//GEN-END:variables

  class UndoAction extends AbstractAction {
    /**
     *
     */
    private static final long serialVersionUID = -137321944593284589L;

    public UndoAction() {
      super("Undo");
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      try {
        undo.undo();
      } catch (CannotUndoException ex) {
        System.out.println("Unable to undo: " + ex);
        ex.printStackTrace();
      }
      updateUndoState();
      redoAction.updateRedoState();
    }

    protected void updateUndoState() {
      if (undo.canUndo()) {
        setEnabled(true);
        putValue(Action.NAME, undo.getUndoPresentationName());
      } else {
        setEnabled(false);
        putValue(Action.NAME, "Undo");
      }
    }
  }


  class RedoAction extends AbstractAction {
    /**
     *
     */
    private static final long serialVersionUID = -2044519935735823050L;

    public RedoAction() {
      super("Redo");
      setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
      try {
        undo.redo();
      } catch (CannotRedoException ex) {
        System.out.println("Unable to redo: " + ex);
        ex.printStackTrace();
      }
      updateRedoState();
      undoAction.updateUndoState();
    }

    protected void updateRedoState() {
      if (undo.canRedo()) {
        setEnabled(true);
        putValue(Action.NAME, undo.getRedoPresentationName());
      } else {
        setEnabled(false);
        putValue(Action.NAME, "Redo");
      }
    }
  }


  //This one listens for edits that can be undone.
  protected class MyUndoableEditListener
      implements UndoableEditListener {
    public void undoableEditHappened(UndoableEditEvent e) {
      //Remember the edit and update the menus.
      undo.addEdit(e.getEdit());
      undoAction.updateUndoState();
      redoAction.updateRedoState();
    }
  }
}
