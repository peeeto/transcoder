package org.pch;

import com.google.common.base.Objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang.StringUtils;

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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

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

        undo.setLimit(50);
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
        try {
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
                    setVisible(!isVisible());
//          setExtendedState(JFrame.NORMAL);
                }
            };
            defaultItem.addActionListener(setVisible);
            popup.add(defaultItem);
            final TrayIcon trayIcon = new TrayIcon(image, "transcoder", popup);
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setVisible(!isVisible());
//          setExtendedState(JFrame.NORMAL);
                }
            });
            setIconImage(Toolkit.getDefaultToolkit().getImage("Duke256.png"));
        } catch (java.lang.UnsupportedOperationException e) {
            e.printStackTrace();
        }

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                setVisible(!isVisible());
//        setExtendedState(NORMAL);
            }
        });
        addWindowStateListener(new WindowStateListener() {

            public void windowStateChanged(WindowEvent e) {
//        setVisible(!isVisible());
//        setExtendedState(NORMAL);
            }
        });
        setVisible(true);
        setSize(new java.awt.Dimension(740, 256));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        urlEncodeButton = new javax.swing.JButton();
        urlDecodeButton = new javax.swing.JButton();
        base64EncodeButton = new javax.swing.JButton();
        base64DecodeButton = new javax.swing.JButton();
        md5hashButton = new javax.swing.JButton();
        sha1hashButton = new javax.swing.JButton();
        sha256Button = new javax.swing.JButton();
        sha384Button = new javax.swing.JButton();
        sha512Button = new javax.swing.JButton();
        cdc32Button = new javax.swing.JButton();
        Adrel32Button = new javax.swing.JButton();
        uuidButton = new javax.swing.JButton();
        timestampEncode = new javax.swing.JButton();
        timestampDecode = new javax.swing.JButton();
        jsonFormat = new javax.swing.JButton();
        xmlFormat = new javax.swing.JButton();
        countLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        editMenu = new javax.swing.JMenu();

        setTitle("Transcoder");

        jPanel1.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(230, 368));
        jPanel1.setLayout(new java.awt.GridLayout(16, 1));

        urlEncodeButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        urlEncodeButton.setText("URL encode");
        urlEncodeButton.setName(""); // NOI18N
        urlEncodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urlEncodeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(urlEncodeButton);

        urlDecodeButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        urlDecodeButton.setText("URL decode");
        urlDecodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                urlDecodeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(urlDecodeButton);

        base64EncodeButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        base64EncodeButton.setText("Base64 encode");
        base64EncodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                base64EncodeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(base64EncodeButton);

        base64DecodeButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        base64DecodeButton.setText("Base64 decode");
        base64DecodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                base64DecodeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(base64DecodeButton);

        md5hashButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        md5hashButton.setText("MD5");
        md5hashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                md5hashButtonActionPerformed(evt);
            }
        });
        jPanel1.add(md5hashButton);

        sha1hashButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        sha1hashButton.setText("SHA-1");
        sha1hashButton.setName(""); // NOI18N
        sha1hashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha1hashButtonActionPerformed(evt);
            }
        });
        jPanel1.add(sha1hashButton);

        sha256Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        sha256Button.setText("SHA-256");
        sha256Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha256ButtonActionPerformed(evt);
            }
        });
        jPanel1.add(sha256Button);

        sha384Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        sha384Button.setText("SHA-384");
        sha384Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha384ButtonActionPerformed(evt);
            }
        });
        jPanel1.add(sha384Button);

        sha512Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        sha512Button.setText("SHA-512");
        sha512Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha512ButtonActionPerformed(evt);
            }
        });
        jPanel1.add(sha512Button);

        cdc32Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cdc32Button.setText("CRC32");
        cdc32Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cdc32ButtonActionPerformed(evt);
            }
        });
        jPanel1.add(cdc32Button);

        Adrel32Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        Adrel32Button.setText("Adler32");
        Adrel32Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Adrel32ButtonActionPerformed(evt);
            }
        });
        jPanel1.add(Adrel32Button);

        uuidButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        uuidButton.setText("UUID");
        uuidButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uuidButtonActionPerformed(evt);
            }
        });
        jPanel1.add(uuidButton);

        timestampEncode.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        timestampEncode.setText("yyyy-MM-dd HH:mm:ss.SSS Z -> 1234L");
        timestampEncode.setToolTipText("ENCODE yyyy-MM-dd HH:mm:ss.SSS Z");
        timestampEncode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                encodeTimestampHandler(evt);
            }
        });
        jPanel1.add(timestampEncode);

        timestampDecode.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        timestampDecode.setText("1234L -> yyyy-MM-dd HH:mm:ss.SSS Z");
        timestampDecode.setToolTipText("DECODE yyyy-MM-dd HH:mm:ss.SSS Z");
        timestampDecode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                decodeTimestampHandler(evt);
            }
        });
        jPanel1.add(timestampDecode);

        jsonFormat.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jsonFormat.setText("JSON Format");
        jsonFormat.setToolTipText("JSON Format");
        jsonFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jsonFormatActionPerformed(evt);
            }
        });
        jPanel1.add(jsonFormat);

        xmlFormat.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        xmlFormat.setText("XML Format");
        xmlFormat.setToolTipText("XML Format");
        xmlFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xmlFormatAction(evt);
            }
        });
        jPanel1.add(xmlFormat);

        getContentPane().add(jPanel1, java.awt.BorderLayout.WEST);

        countLabel.setText("Characters: 0");
        getContentPane().add(countLabel, java.awt.BorderLayout.NORTH);

        jPanel2.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        textPane.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(textPane);

        jPanel2.add(jScrollPane1);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(740, 506));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

  private void sha1hashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha1hashButtonActionPerformed
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, textPane.getText().length());
      }
      textPane.replaceSelection(prepareText(DigestUtils.sha1Hex(getSelectedText())));
      textPane.selectAll();
      textPane.requestFocusInWindow();
  }//GEN-LAST:event_sha1hashButtonActionPerformed

    private String getSelectedText() {
        return Objects.firstNonNull(textPane.getSelectedText(), "");
    }

  private void md5hashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_md5hashButtonActionPerformed
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, textPane.getText().length());
      }
      textPane.replaceSelection(prepareText(DigestUtils.md5Hex(getSelectedText())));
      textPane.selectAll();
      textPane.requestFocusInWindow();
  }//GEN-LAST:event_md5hashButtonActionPerformed

  private void base64DecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_base64DecodeButtonActionPerformed
      try {
          if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
              textPane.select(0, textPane.getText().length());
          }
          textPane.replaceSelection(new String(Base64.decodeBase64(getSelectedText())));
          textPane.selectAll();
          textPane.requestFocusInWindow();
      } catch (Throwable t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_base64DecodeButtonActionPerformed

  private void base64EncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_base64EncodeButtonActionPerformed
      try {
          if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
              textPane.select(0, textPane.getText().length());
          }
          textPane.replaceSelection(Base64.encodeBase64String(getSelectedText().getBytes()));
          textPane.selectAll();
          textPane.requestFocusInWindow();
      } catch (Throwable t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_base64EncodeButtonActionPerformed

  private void urlDecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlDecodeButtonActionPerformed
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, textPane.getText().length());
      }
      textPane.replaceSelection(urlDecode(getSelectedText()));
      textPane.selectAll();
      textPane.requestFocusInWindow();
  }//GEN-LAST:event_urlDecodeButtonActionPerformed

    private String prepareText(String text) {
        return StringUtils.upperCase(text);
    }

  private void urlEncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlEncodeButtonActionPerformed
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, textPane.getText().length());
      }
      textPane.replaceSelection(urlEncode(getSelectedText()));
      textPane.selectAll();
      textPane.requestFocusInWindow();
  }//GEN-LAST:event_urlEncodeButtonActionPerformed


  private void Adrel32ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Adrel32ButtonActionPerformed
      try {
          if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
              textPane.select(0, textPane.getText().length());
          }
          textPane.replaceSelection(prepareText(checksumAdler32Hex(new ByteArrayInputStream(getSelectedText().getBytes("UTF-8")))));

          textPane.selectAll();
          textPane.requestFocusInWindow();
      } catch (Throwable t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_Adrel32ButtonActionPerformed

  private void sha256ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha256ButtonActionPerformed
      try {
          if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
              textPane.select(0, textPane.getText().length());
          }
          textPane.replaceSelection(prepareText(DigestUtils.sha256Hex(getSelectedText().getBytes())));
          textPane.selectAll();
          textPane.requestFocusInWindow();
      } catch (Throwable t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_sha256ButtonActionPerformed

  private void sha384ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha384ButtonActionPerformed
      try {
          if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
              textPane.select(0, textPane.getText().length());
          }
          textPane.replaceSelection(prepareText(DigestUtils.sha384Hex(getSelectedText().getBytes())));
          textPane.selectAll();
          textPane.requestFocusInWindow();
      } catch (Throwable t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_sha384ButtonActionPerformed

  private void sha512ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha512ButtonActionPerformed
      try {
          if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
              textPane.select(0, textPane.getText().length());
          }
          textPane.replaceSelection(prepareText(DigestUtils.sha512Hex(getSelectedText().getBytes())));
          textPane.selectAll();
          textPane.requestFocusInWindow();
      } catch (Throwable t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_sha512ButtonActionPerformed

  private void cdc32ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cdc32ButtonActionPerformed
      try {
          if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
              textPane.select(0, textPane.getText().length());
          }
          textPane.replaceSelection(prepareText(checksumCRC32Hex(new ByteArrayInputStream(getSelectedText().getBytes("UTF-8")))));
          textPane.selectAll();
          textPane.requestFocusInWindow();
      } catch (Throwable t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_cdc32ButtonActionPerformed

  private void uuidButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uuidButtonActionPerformed
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, textPane.getText().length());
      }
      textPane.replaceSelection(java.util.UUID.randomUUID().toString());
      textPane.selectAll();
      textPane.requestFocusInWindow();
  }//GEN-LAST:event_uuidButtonActionPerformed

  private void encodeTimestampHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodeTimestampHandler
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, textPane.getText().length());
      }
      try {
          textPane.replaceSelection(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").parse(textPane.getText()).getTime() + "");
      } catch (ParseException e) {
          e.printStackTrace();
      }
      textPane.selectAll();
      textPane.requestFocusInWindow();
  }//GEN-LAST:event_encodeTimestampHandler

  private void decodeTimestampHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decodeTimestampHandler
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, textPane.getText().length());
      }
      textPane.replaceSelection(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").format(Long.valueOf(textPane.getText())));
      textPane.selectAll();
      textPane.requestFocusInWindow();
  }//GEN-LAST:event_decodeTimestampHandler

  private void xmlFormatAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xmlFormatAction
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, textPane.getText().length());
      }
      try {
          Source xmlInput = new StreamSource(new StringReader(textPane.getText()));
          StringWriter stringWriter = new StringWriter();
          StreamResult xmlOutput = new StreamResult(stringWriter);
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          transformerFactory.setAttribute("indent-number", 4);
          Transformer transformer = transformerFactory.newTransformer();
          transformer.setOutputProperty(OutputKeys.INDENT, "yes");
          transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
          transformer.transform(xmlInput, xmlOutput);
          textPane.replaceSelection(xmlOutput.getWriter().toString());
          textPane.selectAll();
          textPane.requestFocusInWindow();
      } catch (Exception e) {
          throw new IllegalStateException("error reading xml");
      }
  }//GEN-LAST:event_xmlFormatAction

  private void jsonFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jsonFormatActionPerformed
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, textPane.getText().length());
      }
      JsonParser parser = new JsonParser();
      Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization().setExclusionStrategies().create();

      JsonElement el = parser.parse(textPane.getText());
      String jsonString = gson.toJson(el); // done

      textPane.replaceSelection(jsonString);
      textPane.selectAll();
      textPane.requestFocusInWindow();
  }//GEN-LAST:event_jsonFormatActionPerformed

    /**
     * Notifies this object that it is no longer the owner of the contents of
     * the clipboard.
     *
     * @param clipboard the clipboard that is no longer owned
     * @param contents the contents which this owner had placed on the clipboard
     */
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    /**
     * Description of the Method
     *
     * @param str Description of the Parameter
     * @return Description of the Return Value
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
     * Description of the Method
     *
     * @param str Description of the Parameter
     * @return Description of the Return Value
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
            return (URLDecoder.decode(str, "UTF-8"));
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
            return (URLEncoder.encode(str, "UTF-8"));
        } catch (Exception e) {
            return ("Encoding error");
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Adrel32Button;
    private javax.swing.JButton base64DecodeButton;
    private javax.swing.JButton base64EncodeButton;
    private javax.swing.JButton cdc32Button;
    private javax.swing.JLabel countLabel;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jsonFormat;
    private javax.swing.JButton md5hashButton;
    private javax.swing.JButton sha1hashButton;
    private javax.swing.JButton sha256Button;
    private javax.swing.JButton sha384Button;
    private javax.swing.JButton sha512Button;
    private javax.swing.JTextPane textPane;
    private javax.swing.JButton timestampDecode;
    private javax.swing.JButton timestampEncode;
    private javax.swing.JButton urlDecodeButton;
    private javax.swing.JButton urlEncodeButton;
    private javax.swing.JButton uuidButton;
    private javax.swing.JButton xmlFormat;
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

    public static long checksumCRC32(InputStream is) throws IOException {
        CRC32 crc = new CRC32();
        checksum(is, crc);
        return crc.getValue();
    }

    public static String checksumCRC32Hex(InputStream is) throws IOException {
        CRC32 crc = new CRC32();
        checksum(is, crc);
        return Long.toHexString(crc.getValue()).toUpperCase();
    }

    public static String checksumAdler32Hex(InputStream is) throws IOException {
        Adler32 crc = new Adler32();
        checksum(is, crc);
        return Long.toHexString(crc.getValue()).toUpperCase();
    }

    private static Checksum checksum(InputStream is, Checksum checksum) throws IOException {
        InputStream in;
        in = new CheckedInputStream(is, checksum);
        IOUtils.copy(in, new NullOutputStream());
        return checksum;
    }

}
