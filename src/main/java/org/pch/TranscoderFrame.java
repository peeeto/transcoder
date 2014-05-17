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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class TranscoderFrame extends javax.swing.JDialog implements ClipboardOwner {

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

        countLabel = new javax.swing.JLabel();
        jTabbedPane = new javax.swing.JTabbedPane();
        transcoderPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
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
        jPanelHmacMd5Hex = new javax.swing.JPanel();
        jButtonHmacMd5Hex = new javax.swing.JButton();
        jTextFieldHmacMd5Hex = new javax.swing.JTextField();
        jPanelHmacSha1Hex = new javax.swing.JPanel();
        jButtonHmacSha1Hex = new javax.swing.JButton();
        jTextFieldHmacSha1Hex = new javax.swing.JTextField();
        jPanelHmacSha256Hex = new javax.swing.JPanel();
        jButtonHmacSha256Hex = new javax.swing.JButton();
        jTextFieldHmacSha256Hex = new javax.swing.JTextField();
        jPanelHmacSha512Hex = new javax.swing.JPanel();
        jButtonHmacSha512Hex = new javax.swing.JButton();
        jTextFieldHmacSha512Hex = new javax.swing.JTextField();
        jButtonToUpper = new javax.swing.JButton();
        jButtonToLower = new javax.swing.JButton();
        groovyPanel = new javax.swing.JPanel();
        beanshellPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        editMenu = new javax.swing.JMenu();

        setTitle("Transcoder");

        countLabel.setText("Characters: 0");
        getContentPane().add(countLabel, java.awt.BorderLayout.NORTH);

        jTabbedPane.setPreferredSize(new java.awt.Dimension(800, 500));

        transcoderPanel.setLayout(new java.awt.BorderLayout());

        jPanel2.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        textPane.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        textPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textPaneKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(textPane);

        jPanel2.add(jScrollPane1);

        transcoderPanel.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(230, 368));
        jPanel1.setLayout(new java.awt.GridLayout(22, 1));

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

        jPanelHmacMd5Hex.setLayout(new java.awt.GridLayout(1, 2));

        jButtonHmacMd5Hex.setFont(jButtonHmacMd5Hex.getFont().deriveFont(jButtonHmacMd5Hex.getFont().getStyle() & ~java.awt.Font.BOLD, jButtonHmacMd5Hex.getFont().getSize()-2));
        jButtonHmacMd5Hex.setText("HmacMd5Hex");
        jButtonHmacMd5Hex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHmacMd5HexActionPerformed(evt);
            }
        });
        jPanelHmacMd5Hex.add(jButtonHmacMd5Hex);

        jTextFieldHmacMd5Hex.setFont(new java.awt.Font("Andale Mono", 0, 10)); // NOI18N
        jPanelHmacMd5Hex.add(jTextFieldHmacMd5Hex);

        jPanel1.add(jPanelHmacMd5Hex);

        jPanelHmacSha1Hex.setLayout(new java.awt.GridLayout(1, 0));

        jButtonHmacSha1Hex.setFont(jButtonHmacSha1Hex.getFont().deriveFont(jButtonHmacSha1Hex.getFont().getStyle() & ~java.awt.Font.BOLD, jButtonHmacSha1Hex.getFont().getSize()-2));
        jButtonHmacSha1Hex.setText("HmacSha1Hex");
        jButtonHmacSha1Hex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHmacSha1HexActionPerformed(evt);
            }
        });
        jPanelHmacSha1Hex.add(jButtonHmacSha1Hex);

        jTextFieldHmacSha1Hex.setFont(new java.awt.Font("Andale Mono", 0, 10)); // NOI18N
        jTextFieldHmacSha1Hex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldHmacSha1HexActionPerformed(evt);
            }
        });
        jPanelHmacSha1Hex.add(jTextFieldHmacSha1Hex);

        jPanel1.add(jPanelHmacSha1Hex);

        jPanelHmacSha256Hex.setLayout(new java.awt.GridLayout(1, 0));

        jButtonHmacSha256Hex.setFont(jButtonHmacSha256Hex.getFont().deriveFont(jButtonHmacSha256Hex.getFont().getStyle() & ~java.awt.Font.BOLD, jButtonHmacSha256Hex.getFont().getSize()-2));
        jButtonHmacSha256Hex.setText("HmacSha256Hex");
        jButtonHmacSha256Hex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHmacSha256HexActionPerformed(evt);
            }
        });
        jPanelHmacSha256Hex.add(jButtonHmacSha256Hex);

        jTextFieldHmacSha256Hex.setFont(new java.awt.Font("Andale Mono", 0, 10)); // NOI18N
        jPanelHmacSha256Hex.add(jTextFieldHmacSha256Hex);

        jPanel1.add(jPanelHmacSha256Hex);

        jPanelHmacSha512Hex.setLayout(new java.awt.GridLayout(1, 0));

        jButtonHmacSha512Hex.setFont(jButtonHmacSha512Hex.getFont().deriveFont(jButtonHmacSha512Hex.getFont().getStyle() & ~java.awt.Font.BOLD, jButtonHmacSha512Hex.getFont().getSize()-2));
        jButtonHmacSha512Hex.setText("HmacSha512Hex");
        jButtonHmacSha512Hex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHmacSha512HexActionPerformed(evt);
            }
        });
        jPanelHmacSha512Hex.add(jButtonHmacSha512Hex);

        jTextFieldHmacSha512Hex.setFont(new java.awt.Font("Andale Mono", 0, 10)); // NOI18N
        jPanelHmacSha512Hex.add(jTextFieldHmacSha512Hex);

        jPanel1.add(jPanelHmacSha512Hex);

        jButtonToUpper.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jButtonToUpper.setText("To Upper");
        jButtonToUpper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonToUpperActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonToUpper);

        jButtonToLower.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jButtonToLower.setText("To Lower");
        jButtonToLower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonToLowerActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonToLower);

        transcoderPanel.add(jPanel1, java.awt.BorderLayout.WEST);

        jTabbedPane.addTab("transcoder", transcoderPanel);
        jTabbedPane.addTab("groovy", groovyPanel);
        jTabbedPane.addTab("beanshell", beanshellPanel);

        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(836, 543));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

  private void sha1hashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha1hashButtonActionPerformed
      selectAllIfNotSelectedYet(textPane);
      textPane.replaceSelection(prepareText(DigestUtils.sha1Hex(getSelectedText())));
      selectAllAndFocus();
  }//GEN-LAST:event_sha1hashButtonActionPerformed

    private String getSelectedText() {
        return Objects.firstNonNull(textPane.getSelectedText(), "");
    }

  private void md5hashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_md5hashButtonActionPerformed
      selectAllIfNotSelectedYet(this.textPane);
      textPane.replaceSelection(prepareText(DigestUtils.md5Hex(getSelectedText())));
      selectAllAndFocus();
  }//GEN-LAST:event_md5hashButtonActionPerformed

    private void selectAllAndFocus() {
        textPane.selectAll();
        textPane.requestFocusInWindow();
    }

    private void selectAllIfNotSelectedYet(JTextPane textPane) {
        if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
            textPane.select(0, textPane.getText().length());
        }
    }

  private void base64DecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_base64DecodeButtonActionPerformed
      try {
          selectAllIfNotSelectedYet(textPane);
          textPane.replaceSelection(new String(Base64.decodeBase64(getSelectedText())));
          selectAllAndFocus();
      } catch (Exception t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_base64DecodeButtonActionPerformed

  private void base64EncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_base64EncodeButtonActionPerformed
      try {
          selectAllIfNotSelectedYet(textPane);
          textPane.replaceSelection(Base64.encodeBase64String(getSelectedText().getBytes()));
          selectAllAndFocus();
      } catch (Exception t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_base64EncodeButtonActionPerformed

  private void urlDecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlDecodeButtonActionPerformed
      selectAllIfNotSelectedYet(textPane);
      String text = urlDecode(getSelectedText());
      textPane.replaceSelection(text);
      selectAllAndFocus();
  }//GEN-LAST:event_urlDecodeButtonActionPerformed

    private String prepareText(String text) {
        return StringUtils.upperCase(text);
    }

  private void urlEncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlEncodeButtonActionPerformed
      selectAllIfNotSelectedYet(textPane);
      textPane.replaceSelection(urlEncode(getSelectedText()));
      selectAllAndFocus();
  }//GEN-LAST:event_urlEncodeButtonActionPerformed


  private void Adrel32ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Adrel32ButtonActionPerformed
      try {
          selectAllIfNotSelectedYet(textPane);
          textPane.replaceSelection(prepareText(checksumAdler32Hex(new ByteArrayInputStream(getSelectedText().getBytes(DEFAULT_ENCODING)))));
          selectAllAndFocus();
      } catch (Exception t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_Adrel32ButtonActionPerformed

  private void sha256ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha256ButtonActionPerformed
      try {
          selectAllIfNotSelectedYet(textPane);
          textPane.replaceSelection(prepareText(DigestUtils.sha256Hex(getSelectedText().getBytes())));
          selectAllAndFocus();
      } catch (Exception t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_sha256ButtonActionPerformed

  private void sha384ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha384ButtonActionPerformed
      try {
          selectAllIfNotSelectedYet(textPane);
          textPane.replaceSelection(prepareText(DigestUtils.sha384Hex(getSelectedText().getBytes())));
          selectAllAndFocus();
      } catch (Exception t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_sha384ButtonActionPerformed

  private void sha512ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha512ButtonActionPerformed
      try {
          selectAllIfNotSelectedYet(textPane);
          textPane.replaceSelection(prepareText(DigestUtils.sha512Hex(getSelectedText().getBytes())));
          selectAllAndFocus();
      } catch (Exception t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_sha512ButtonActionPerformed

  private void cdc32ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cdc32ButtonActionPerformed
      try {
          selectAllIfNotSelectedYet(textPane);
          textPane.replaceSelection(prepareText(checksumCRC32Hex(new ByteArrayInputStream(getSelectedText().getBytes(DEFAULT_ENCODING)))));
          selectAllAndFocus();
      } catch (Exception t) {
          Runtime.getRuntime().gc();
          t.printStackTrace();
          textPane.setText("Exception! " + t.toString());
      }
  }//GEN-LAST:event_cdc32ButtonActionPerformed

  private void uuidButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uuidButtonActionPerformed
      selectAllIfNotSelectedYet(textPane);
      textPane.replaceSelection(java.util.UUID.randomUUID().toString());
      selectAllAndFocus();
  }//GEN-LAST:event_uuidButtonActionPerformed

  private void encodeTimestampHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodeTimestampHandler
      selectAllIfNotSelectedYet(textPane);
      String text = "";
      try {
          if (getSelectedText().trim().isEmpty()) {
              text = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z").print(DateTime.now().toDate().getTime());
          }
          textPane.replaceSelection(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").parse(text).getTime() + "");
      } catch (ParseException e) {
          e.printStackTrace();
      }
      selectAllAndFocus();
  }//GEN-LAST:event_encodeTimestampHandler

  private void decodeTimestampHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decodeTimestampHandler
      String text = textPane.getText();
      if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
          textPane.select(0, text.length());
      }
      if (text == null || text.trim().isEmpty()) {
          text = String.valueOf(DateTime.now().toDate().getTime());
      }
      textPane.replaceSelection(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").format(Long.valueOf(text)));
      selectAllAndFocus();
  }//GEN-LAST:event_decodeTimestampHandler

  private void xmlFormatAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xmlFormatAction
      selectAllIfNotSelectedYet(textPane);
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
          selectAllAndFocus();
      } catch (Exception e) {
          throw new IllegalStateException("error reading xml");
      }
  }//GEN-LAST:event_xmlFormatAction

  private void jsonFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jsonFormatActionPerformed
      selectAllIfNotSelectedYet(textPane);
      JsonParser parser = new JsonParser();
      Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization().setExclusionStrategies().create();

      JsonElement el = parser.parse(textPane.getText());
      String jsonString = gson.toJson(el); // done

      textPane.replaceSelection(jsonString);
      selectAllAndFocus();
  }//GEN-LAST:event_jsonFormatActionPerformed

    private void textPaneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPaneKeyPressed
        if (evt.isControlDown()) {
            if (Character.toLowerCase(evt.getKeyChar()) == 'r' || evt.isShiftDown()) {
                redoAction.actionPerformed(null);
            } else if ((Character.toLowerCase(evt.getKeyCode()) == 'z' || Character.toLowerCase(evt.getKeyCode()) == 'y')) {
                undoAction.actionPerformed(null);
            }
        }
    }//GEN-LAST:event_textPaneKeyPressed

    private void jButtonHmacMd5HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHmacMd5HexActionPerformed
        selectAllIfNotSelectedYet(textPane);
        try {
            String hmac = Hex.encodeHexString(getHmac(jTextFieldHmacMd5Hex.getText(), textPane.getSelectedText(), "HmacMD5"));
            textPane.replaceSelection(hmac);
            selectAllAndFocus();
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }    }//GEN-LAST:event_jButtonHmacMd5HexActionPerformed

    private void jButtonHmacSha512HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHmacSha512HexActionPerformed
        selectAllIfNotSelectedYet(textPane);
        try {
            String hmac = Hex.encodeHexString(getHmac(jTextFieldHmacSha512Hex.getText(), textPane.getSelectedText(), "HmacSHA512"));
            textPane.replaceSelection(hmac);
            selectAllAndFocus();
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }    }//GEN-LAST:event_jButtonHmacSha512HexActionPerformed

    private void jTextFieldHmacSha1HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldHmacSha1HexActionPerformed
        selectAllIfNotSelectedYet(textPane);
        try {
            String hmac = Hex.encodeHexString(getHmac(jTextFieldHmacSha1Hex.getText(), textPane.getSelectedText(), "HmacSHA1"));
            textPane.replaceSelection(hmac);
            selectAllAndFocus();
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }    }//GEN-LAST:event_jTextFieldHmacSha1HexActionPerformed

    private void jButtonHmacSha256HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHmacSha256HexActionPerformed
        selectAllIfNotSelectedYet(textPane);
        try {
            String hmac = Hex.encodeHexString(getHmac(jTextFieldHmacSha256Hex.getText(), textPane.getSelectedText(), "HmacSha256"));
            textPane.replaceSelection(hmac);
            selectAllAndFocus();
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }    }//GEN-LAST:event_jButtonHmacSha256HexActionPerformed

    private void jButtonHmacSha1HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHmacSha1HexActionPerformed
        selectAllIfNotSelectedYet(textPane);
        try {
            String hmac = Hex.encodeHexString(getHmac(jTextFieldHmacSha1Hex.getText(), textPane.getText(), "HmacSha1"));
            textPane.replaceSelection(hmac);
            selectAllAndFocus();
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }    }//GEN-LAST:event_jButtonHmacSha1HexActionPerformed

    private void jButtonToUpperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToUpperActionPerformed
        selectAllIfNotSelectedYet(textPane);
        try {
            textPane.replaceSelection(StringUtils.upperCase(textPane.getSelectedText()));
            selectAllAndFocus();
        } catch (Exception e) {
            throw new IllegalStateException("error upperCase", e);
        }    }//GEN-LAST:event_jButtonToUpperActionPerformed

    private void jButtonToLowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToLowerActionPerformed
        selectAllIfNotSelectedYet(textPane);
        try {
            textPane.replaceSelection(StringUtils.lowerCase(textPane.getSelectedText()));
            selectAllAndFocus();
        } catch (Exception e) {
            throw new IllegalStateException("error lowerCase", e);
        }    }//GEN-LAST:event_jButtonToLowerActionPerformed

    private static byte[] getHmac(String secretKey, String payload, String hmacType) {
        final Mac mac;
        byte[] hmac;
        try {
            final byte[] secretKeyBytes;
            if (secretKey == null) {
                secretKeyBytes = new byte[]{0};
            } else {
                secretKeyBytes = secretKey.getBytes(DEFAULT_ENCODING);
            }

            if (payload == null) {
                payload = "";
            }

            SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, hmacType.toString());
            mac = Mac.getInstance(hmacType.toString());
            mac.init(keySpec);

            hmac = mac.doFinal(payload.getBytes(DEFAULT_ENCODING));

        } catch (NoSuchAlgorithmException e) {
            String msg = "An error occurred initializing algorithm '" + hmacType + "', e: " + e;
            throw new IllegalStateException(msg, e);
        } catch (InvalidKeyException e) {
            String msg = "An error occurred initializing key, e: " + e;
            throw new IllegalStateException(msg, e);
        } catch (UnsupportedEncodingException e) {
            String msg = "Invalid encoding, e: " + e;
            throw new IllegalStateException(msg, e);
        }
        return hmac;
    }
    public static final String DEFAULT_ENCODING = "UTF-8";

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
            return (URLDecoder.decode(str, DEFAULT_ENCODING));
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
            return (URLEncoder.encode(str, DEFAULT_ENCODING));
        } catch (Exception e) {
            return ("Encoding error");
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Adrel32Button;
    private javax.swing.JButton base64DecodeButton;
    private javax.swing.JButton base64EncodeButton;
    private javax.swing.JPanel beanshellPanel;
    private javax.swing.JButton cdc32Button;
    private javax.swing.JLabel countLabel;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPanel groovyPanel;
    private javax.swing.JButton jButtonHmacMd5Hex;
    private javax.swing.JButton jButtonHmacSha1Hex;
    private javax.swing.JButton jButtonHmacSha256Hex;
    private javax.swing.JButton jButtonHmacSha512Hex;
    private javax.swing.JButton jButtonToLower;
    private javax.swing.JButton jButtonToUpper;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelHmacMd5Hex;
    private javax.swing.JPanel jPanelHmacSha1Hex;
    private javax.swing.JPanel jPanelHmacSha256Hex;
    private javax.swing.JPanel jPanelHmacSha512Hex;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTextField jTextFieldHmacMd5Hex;
    private javax.swing.JTextField jTextFieldHmacSha1Hex;
    private javax.swing.JTextField jTextFieldHmacSha256Hex;
    private javax.swing.JTextField jTextFieldHmacSha512Hex;
    private javax.swing.JButton jsonFormat;
    private javax.swing.JButton md5hashButton;
    private javax.swing.JButton sha1hashButton;
    private javax.swing.JButton sha256Button;
    private javax.swing.JButton sha384Button;
    private javax.swing.JButton sha512Button;
    private javax.swing.JTextPane textPane;
    private javax.swing.JButton timestampDecode;
    private javax.swing.JButton timestampEncode;
    private javax.swing.JPanel transcoderPanel;
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
