package com.petercho;

import com.google.common.base.MoreObjects;
import com.google.common.primitives.Longs;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;

public class TranscoderFrame extends javax.swing.JDialog implements ClipboardOwner {

    private static final long serialVersionUID = 5712516501903313109L;
    private UndoManager undo = new UndoManager();
    private UndoAction undoAction;
    private RedoAction redoAction;
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
    private static Image createImage(String path, String description) {
        URL imageURL = Thread.currentThread().getContextClassLoader().getResource(path);
        return (new ImageIcon(imageURL, description)).getImage();
    }

    private void initTrayComponents() {
        try {
            final SystemTray tray = SystemTray.getSystemTray();
            final Image image = createImage("images/bulb.gif", "transcoder");
            ActionListener exitListener = e -> {
                System.out.println("Exiting....");
                System.exit(0);
            };
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            defaultItem = new MenuItem("Open");
            final ActionListener setVisible = e -> {
                setVisible(!isVisible());
//          setExtendedState(JFrame.NORMAL);
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
        addWindowStateListener(e -> {
//        setVisible(!isVisible());
//        setExtendedState(NORMAL);
        });
        setVisible(true);
        setSize(new java.awt.Dimension(740, 256));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    //The following two methods allow us to find an
    //action provided by the editor kit by its name.
    private void createActionTable(JTextComponent textComponent) {
        Action[] actionsArray = textComponent.getActions();
        for (Action a : actionsArray) {
            actions.put(a.getValue(Action.NAME), a);
        }
    }

    private Action getActionByName(String name) {
        return actions.get(name);
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
        uuidButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        bcryptButton = new javax.swing.JButton();
        strengthComboBox = new javax.swing.JComboBox();
        bcryptTextField = new javax.swing.JTextField();
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
        jPanelMd5 = new javax.swing.JPanel();
        md5hashBase64Button = new javax.swing.JButton();
        md5hashButton = new javax.swing.JButton();
        jPanelSha1 = new javax.swing.JPanel();
        sha1hashBase64Button = new javax.swing.JButton();
        sha1hashButton = new javax.swing.JButton();
        jPanelSha256 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        sha256Button = new javax.swing.JButton();
        jPanelSha384 = new javax.swing.JPanel();
        sha384Base64Button = new javax.swing.JButton();
        sha384Button = new javax.swing.JButton();
        jPanelSha512 = new javax.swing.JPanel();
        sha512Base64Button = new javax.swing.JButton();
        sha512Button = new javax.swing.JButton();
        jPanelCRC32 = new javax.swing.JPanel();
        crc32Base64Button = new javax.swing.JButton();
        crc32Button = new javax.swing.JButton();
        jPanelAdler32 = new javax.swing.JPanel();
        adler32Base64Button = new javax.swing.JButton();
        Adler32Button = new javax.swing.JButton();
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
        jPanel1.setLayout(new java.awt.GridLayout(23, 1));

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

        uuidButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        uuidButton.setText("UUID");
        uuidButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uuidButtonActionPerformed(evt);
            }
        });
        jPanel1.add(uuidButton);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        bcryptButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        bcryptButton.setText("BCrypt");
        bcryptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcryptButtonActionPerformed(evt);
            }
        });
        jPanel3.add(bcryptButton);

        strengthComboBox.setEditable(true);
        strengthComboBox.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        strengthComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        strengthComboBox.setToolTipText("Strength [4..31");
        jPanel3.add(strengthComboBox);

        bcryptTextField.setFont(new java.awt.Font("Andale Mono", 0, 10)); // NOI18N
        bcryptTextField.setToolTipText("Plain Text Password - empty if should be generated");
        bcryptTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bcryptTextFieldMouseClicked(evt);
            }
        });
        jPanel3.add(bcryptTextField);

        jPanel1.add(jPanel3);

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

        jPanelMd5.setLayout(new java.awt.GridLayout(1, 0));

        md5hashBase64Button.setFont(new java.awt.Font("DialogInput", 0, 10)); // NOI18N
        md5hashBase64Button.setText("MD5 Base64");
        md5hashBase64Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                md5hashBase64ButtonActionPerformed(evt);
            }
        });
        jPanelMd5.add(md5hashBase64Button);

        md5hashButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        md5hashButton.setText("MD5");
        md5hashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                md5hashButtonActionPerformed(evt);
            }
        });
        jPanelMd5.add(md5hashButton);

        jPanel1.add(jPanelMd5);

        jPanelSha1.setLayout(new java.awt.GridLayout(1, 0));

        sha1hashBase64Button.setFont(new java.awt.Font("DialogInput", 0, 10)); // NOI18N
        sha1hashBase64Button.setText("SHA-1 Base64");
        sha1hashBase64Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha1hashBase64ButtonActionPerformed(evt);
            }
        });
        jPanelSha1.add(sha1hashBase64Button);

        sha1hashButton.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        sha1hashButton.setText("SHA-1");
        sha1hashButton.setName(""); // NOI18N
        sha1hashButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha1hashButtonActionPerformed(evt);
            }
        });
        jPanelSha1.add(sha1hashButton);

        jPanel1.add(jPanelSha1);

        jPanelSha256.setLayout(new java.awt.GridLayout(1, 0));

        jButton3.setFont(new java.awt.Font("DialogInput", 0, 10)); // NOI18N
        jButton3.setText("SHA-256 Base64");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanelSha256.add(jButton3);

        sha256Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        sha256Button.setText("SHA-256");
        sha256Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha256ButtonActionPerformed(evt);
            }
        });
        jPanelSha256.add(sha256Button);

        jPanel1.add(jPanelSha256);

        jPanelSha384.setLayout(new java.awt.GridLayout(1, 0));

        sha384Base64Button.setFont(new java.awt.Font("DialogInput", 0, 10)); // NOI18N
        sha384Base64Button.setText("SHA-384 Base64");
        sha384Base64Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha384Base64ButtonActionPerformed(evt);
            }
        });
        jPanelSha384.add(sha384Base64Button);

        sha384Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        sha384Button.setText("SHA-384");
        sha384Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha384ButtonActionPerformed(evt);
            }
        });
        jPanelSha384.add(sha384Button);

        jPanel1.add(jPanelSha384);

        jPanelSha512.setLayout(new java.awt.GridLayout(1, 0));

        sha512Base64Button.setFont(new java.awt.Font("DialogInput", 0, 10)); // NOI18N
        sha512Base64Button.setText("SHA-512 Base64");
        sha512Base64Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha512Base64ButtonActionPerformed(evt);
            }
        });
        jPanelSha512.add(sha512Base64Button);

        sha512Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        sha512Button.setText("SHA-512");
        sha512Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sha512ButtonActionPerformed(evt);
            }
        });
        jPanelSha512.add(sha512Button);

        jPanel1.add(jPanelSha512);

        jPanelCRC32.setLayout(new java.awt.GridLayout(1, 0));

        crc32Base64Button.setFont(new java.awt.Font("DialogInput", 0, 10)); // NOI18N
        crc32Base64Button.setText("CRC32 Base64");
        crc32Base64Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crc32Base64ButtonActionPerformed(evt);
            }
        });
        jPanelCRC32.add(crc32Base64Button);

        crc32Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        crc32Button.setText("CRC32");
        crc32Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crc32ButtonActionPerformed(evt);
            }
        });
        jPanelCRC32.add(crc32Button);

        jPanel1.add(jPanelCRC32);

        jPanelAdler32.setLayout(new java.awt.GridLayout(1, 0));

        adler32Base64Button.setFont(new java.awt.Font("DialogInput", 0, 10)); // NOI18N
        adler32Base64Button.setText("Adler32 Base64");
        adler32Base64Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adler32Base64ButtonActionPerformed(evt);
            }
        });
        jPanelAdler32.add(adler32Base64Button);

        Adler32Button.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        Adler32Button.setText("Adler32");
        Adler32Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Adler32ButtonActionPerformed(evt);
            }
        });
        jPanelAdler32.add(Adler32Button);

        jPanel1.add(jPanelAdler32);

        transcoderPanel.add(jPanel1, java.awt.BorderLayout.WEST);

        jTabbedPane.addTab("transcoder", transcoderPanel);

        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        editMenu.setText("Edit");
        jMenuBar1.add(editMenu);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(836, 587));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sha1hashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha1hashButtonActionPerformed
        replaceSelectAndFocus(prepareText(DigestUtils.sha1Hex(getSelectedText())));
    }//GEN-LAST:event_sha1hashButtonActionPerformed

    private String getSelectedText() {
        selectAllIfNotSelectedYet(textPane);
        return MoreObjects.firstNonNull(textPane.getSelectedText(), "");
    }

    private void md5hashButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_md5hashButtonActionPerformed
        replaceSelectAndFocus(prepareText(DigestUtils.md5Hex(getSelectedText())));
    }//GEN-LAST:event_md5hashButtonActionPerformed

    private void replaceSelectAndFocus(String text) {
        textPane.replaceSelection(text);
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
            replaceSelectAndFocus(new String(Base64.decodeBase64(getSelectedText())));
        } catch (Exception t) {
            Runtime.getRuntime().gc();
            t.printStackTrace();
            textPane.setText("Exception! " + t.toString());
        }
    }//GEN-LAST:event_base64DecodeButtonActionPerformed

    private void base64EncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_base64EncodeButtonActionPerformed
        replaceSelectAndFocus(Base64.encodeBase64String(getSelectedText().getBytes()));
    }//GEN-LAST:event_base64EncodeButtonActionPerformed

    private void urlDecodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlDecodeButtonActionPerformed
        replaceSelectAndFocus(Encoder.urlDecode(getSelectedText()));
    }//GEN-LAST:event_urlDecodeButtonActionPerformed

    private String prepareText(String text) {
        return StringUtils.upperCase(text);
    }

    private void urlEncodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_urlEncodeButtonActionPerformed
        replaceSelectAndFocus(Encoder.urlEncode(getSelectedText()));
    }//GEN-LAST:event_urlEncodeButtonActionPerformed

    private void Adler32ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Adler32ButtonActionPerformed
        replaceSelectAndFocus(prepareText(Long.toHexString(Encoder.checksumAdler32(getSelectedText()))));
    }//GEN-LAST:event_Adler32ButtonActionPerformed

    private void sha256ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha256ButtonActionPerformed
        try {
            replaceSelectAndFocus(prepareText(DigestUtils.sha256Hex(getSelectedText().getBytes())));
        } catch (Exception t) {
            t.printStackTrace();
            textPane.setText("Exception! " + t.toString());
        }
    }//GEN-LAST:event_sha256ButtonActionPerformed

    private void sha384ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha384ButtonActionPerformed
        try {
            replaceSelectAndFocus(prepareText(DigestUtils.sha384Hex(getSelectedText().getBytes())));
        } catch (Exception t) {
            t.printStackTrace();
            textPane.setText("Exception! " + t.toString());
        }
    }//GEN-LAST:event_sha384ButtonActionPerformed

    private void sha512ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha512ButtonActionPerformed
        try {
            replaceSelectAndFocus(prepareText(DigestUtils.sha512Hex(getSelectedText().getBytes())));
        } catch (Exception t) {
            t.printStackTrace();
            textPane.setText("Exception! " + t.toString());
        }
    }//GEN-LAST:event_sha512ButtonActionPerformed

    private void crc32ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crc32ButtonActionPerformed
        try {
            replaceSelectAndFocus(prepareText(Long.toHexString(Encoder.checksumCRC32(getSelectedText())).toUpperCase()));
        } catch (Exception t) {
            t.printStackTrace();
            textPane.setText("Exception! " + t.toString());
        }
    }//GEN-LAST:event_crc32ButtonActionPerformed

    private void uuidButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uuidButtonActionPerformed
        replaceSelectAndFocus(java.util.UUID.randomUUID().toString());
    }//GEN-LAST:event_uuidButtonActionPerformed

    private void encodeTimestampHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_encodeTimestampHandler
        replaceSelectAndFocus(getDate(getSelectedText()).getTime() + "");
    }//GEN-LAST:event_encodeTimestampHandler

    private Date getDate(String text) {
        Date parsedDate;
        try {
            if (getSelectedText().trim().isEmpty()) {
                text = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z").print(DateTime.now().toDate().getTime());
            }
            try {
                parsedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").parse(text);
            } catch (ParseException pe) {
                parsedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(text);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException("invalid timestamp format", e);
        }
        return parsedDate;
    }

    private void decodeTimestampHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_decodeTimestampHandler
        replaceSelectAndFocus(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").format(Long.valueOf(prepareTimestamp(getSelectedText()))));
    }//GEN-LAST:event_decodeTimestampHandler

    private String prepareTimestamp(String text) {
        if (text.trim().isEmpty()) {
            text = String.valueOf(DateTime.now().toDate().getTime());
        }
        return text;
    }

    private void xmlFormatAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xmlFormatAction
        try {
            String content = Encoder.formatXml(getSelectedText());
            replaceSelectAndFocus(content);
        } catch (Exception e) {
            throw new IllegalStateException("error reading xml");
        }
    }//GEN-LAST:event_xmlFormatAction

    private void jsonFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jsonFormatActionPerformed
        final String text = getSelectedText();
        String jsonString = Encoder.formatJson(text);
        replaceSelectAndFocus(jsonString);
    }//GEN-LAST:event_jsonFormatActionPerformed

    private void textPaneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPaneKeyPressed
        if (evt.isControlDown()) {
            boolean zOrY = Character.toLowerCase(evt.getKeyCode()) == 'z' || Character.toLowerCase(evt.getKeyCode()) == 'y';
            boolean r = Character.toLowerCase(evt.getKeyCode()) == 'r';
            if (r || (evt.isShiftDown() && zOrY)) {
                redoAction.actionPerformed(null);
            } else {
                if (zOrY) {
                    undoAction.actionPerformed(null);
                }
            }
        }
    }//GEN-LAST:event_textPaneKeyPressed

    private void jButtonHmacMd5HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHmacMd5HexActionPerformed
        try {
            String hmac = Encoder.hmacHex(jTextFieldHmacMd5Hex.getText(), getSelectedText(), "HmacMD5");
            replaceSelectAndFocus(hmac);
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }
    }//GEN-LAST:event_jButtonHmacMd5HexActionPerformed

    private void jButtonHmacSha512HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHmacSha512HexActionPerformed
        try {
            String hmac = Encoder.hmacHex(jTextFieldHmacSha512Hex.getText(), getSelectedText(), "HmacSHA512");
            replaceSelectAndFocus(hmac);
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }
    }//GEN-LAST:event_jButtonHmacSha512HexActionPerformed

    private void jTextFieldHmacSha1HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldHmacSha1HexActionPerformed
        try {
            String hmac = Encoder.hmacHex(jTextFieldHmacSha1Hex.getText(), getSelectedText(), "HmacSHA1");
            replaceSelectAndFocus(hmac);
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }
    }//GEN-LAST:event_jTextFieldHmacSha1HexActionPerformed

    private void jButtonHmacSha256HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHmacSha256HexActionPerformed
        try {
            String hmac = Encoder.hmacHex(jTextFieldHmacSha256Hex.getText(), getSelectedText(), "HmacSha256");
            replaceSelectAndFocus(hmac);
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }
    }//GEN-LAST:event_jButtonHmacSha256HexActionPerformed

    private void jButtonHmacSha1HexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHmacSha1HexActionPerformed
        try {
            String hmac = Encoder.hmacHex(jTextFieldHmacSha1Hex.getText(), getSelectedText(), "HmacSha1");
            replaceSelectAndFocus(hmac);
        } catch (Exception e) {
            throw new IllegalStateException("error calculating hmac", e);
        }
    }//GEN-LAST:event_jButtonHmacSha1HexActionPerformed

    private void jButtonToUpperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToUpperActionPerformed
        try {
            replaceSelectAndFocus(StringUtils.upperCase(getSelectedText()));
        } catch (Exception e) {
            throw new IllegalStateException("error upperCase", e);
        }
    }//GEN-LAST:event_jButtonToUpperActionPerformed

    private void jButtonToLowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonToLowerActionPerformed
        try {
            replaceSelectAndFocus(StringUtils.lowerCase(getSelectedText()));
        } catch (Exception e) {
            throw new IllegalStateException("error lowerCase", e);
        }
    }//GEN-LAST:event_jButtonToLowerActionPerformed

    private void md5hashBase64ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_md5hashBase64ButtonActionPerformed
        replaceSelectAndFocus(Base64.encodeBase64String(DigestUtils.md5(getSelectedText())));
    }//GEN-LAST:event_md5hashBase64ButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        replaceSelectAndFocus(Base64.encodeBase64String(DigestUtils.sha256(getSelectedText())));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void sha1hashBase64ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha1hashBase64ButtonActionPerformed
        replaceSelectAndFocus(Base64.encodeBase64String(DigestUtils.sha1(getSelectedText())));
    }//GEN-LAST:event_sha1hashBase64ButtonActionPerformed

    private void sha384Base64ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha384Base64ButtonActionPerformed
        replaceSelectAndFocus(Base64.encodeBase64String(DigestUtils.sha384(getSelectedText())));
    }//GEN-LAST:event_sha384Base64ButtonActionPerformed

    private void sha512Base64ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sha512Base64ButtonActionPerformed
        replaceSelectAndFocus(Base64.encodeBase64String(DigestUtils.sha512(getSelectedText())));
    }//GEN-LAST:event_sha512Base64ButtonActionPerformed

    private void crc32Base64ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crc32Base64ButtonActionPerformed
        try {
            replaceSelectAndFocus(new String(
                    Base64.encodeBase64(Arrays.copyOfRange(Longs.toByteArray(Encoder.checksumCRC32(getSelectedText())), 4, 8)
                    ), Encoder.DEFAULT_ENCODING));
        } catch (IOException ex) {
            throw new IllegalStateException("error crc32 base64", ex);
        }
    }//GEN-LAST:event_crc32Base64ButtonActionPerformed

    private void adler32Base64ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adler32Base64ButtonActionPerformed
        try {
            replaceSelectAndFocus(new String(
                    Base64.encodeBase64(Arrays.copyOfRange(Longs.toByteArray(Encoder.checksumAdler32(getSelectedText())), 4, 8)
                    ), Encoder.DEFAULT_ENCODING));
        } catch (IOException ex) {
            throw new IllegalStateException("error adler32 base64", ex);
        }
    }//GEN-LAST:event_adler32Base64ButtonActionPerformed

    private void bcryptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcryptButtonActionPerformed
        try {
            selectAllIfNotSelectedYet(this.textPane);
            int strength = Integer.valueOf(String.valueOf(strengthComboBox.getSelectedItem()));
            PasswordEncoder bcrypt = new BCryptPasswordEncoder(strength);
            final String plainTextPassword = bcryptTextField.getText();
            final String encryptedPassword = getSelectedText();

            if (StringUtils.isNotBlank(plainTextPassword)) {
                if (bcrypt.matches(plainTextPassword, encryptedPassword)) {
                    JOptionPane.showMessageDialog(Transcoder.getInstance(),
                            "Passwords match."
                                    + "\r\nplainTextPassword: " + plainTextPassword
                                    + "\r\nencryptedPassword: " + encryptedPassword,
                            "Passwords Match",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(Transcoder.getInstance(),
                            "Passwords do not match."
                                    + "\r\nplainTextPassword: " + plainTextPassword
                                    + "\r\nencryptedPassword: " + encryptedPassword,
                            "Passwords Do Not Match",
                            JOptionPane.WARNING_MESSAGE
                    );

                }
                return;
            }
            replaceSelectAndFocus(bcrypt.encode(getSelectedText()));
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid strength value!");
        }
    }//GEN-LAST:event_bcryptButtonActionPerformed

    private void bcryptTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bcryptTextFieldMouseClicked
        bcryptTextField.selectAll();
    }//GEN-LAST:event_bcryptTextFieldMouseClicked

    /**
     * Notifies this object that it is no longer the owner of the contents of
     * the clipboard.
     *
     * @param clipboard the clipboard that is no longer owned
     * @param contents  the contents which this owner had placed on the clipboard
     */
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Adler32Button;
    private javax.swing.JButton adler32Base64Button;
    private javax.swing.JButton base64DecodeButton;
    private javax.swing.JButton base64EncodeButton;
    private javax.swing.JButton bcryptButton;
    private javax.swing.JTextField bcryptTextField;
    private javax.swing.JPanel beanshellPanel;
    private javax.swing.JLabel countLabel;
    private javax.swing.JButton crc32Base64Button;
    private javax.swing.JButton crc32Button;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPanel groovyPanel;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonHmacMd5Hex;
    private javax.swing.JButton jButtonHmacSha1Hex;
    private javax.swing.JButton jButtonHmacSha256Hex;
    private javax.swing.JButton jButtonHmacSha512Hex;
    private javax.swing.JButton jButtonToLower;
    private javax.swing.JButton jButtonToUpper;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelAdler32;
    private javax.swing.JPanel jPanelCRC32;
    private javax.swing.JPanel jPanelHmacMd5Hex;
    private javax.swing.JPanel jPanelHmacSha1Hex;
    private javax.swing.JPanel jPanelHmacSha256Hex;
    private javax.swing.JPanel jPanelHmacSha512Hex;
    private javax.swing.JPanel jPanelMd5;
    private javax.swing.JPanel jPanelSha1;
    private javax.swing.JPanel jPanelSha256;
    private javax.swing.JPanel jPanelSha384;
    private javax.swing.JPanel jPanelSha512;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTextField jTextFieldHmacMd5Hex;
    private javax.swing.JTextField jTextFieldHmacSha1Hex;
    private javax.swing.JTextField jTextFieldHmacSha256Hex;
    private javax.swing.JTextField jTextFieldHmacSha512Hex;
    private javax.swing.JButton jsonFormat;
    private javax.swing.JButton md5hashBase64Button;
    private javax.swing.JButton md5hashButton;
    private javax.swing.JButton sha1hashBase64Button;
    private javax.swing.JButton sha1hashButton;
    private javax.swing.JButton sha256Button;
    private javax.swing.JButton sha384Base64Button;
    private javax.swing.JButton sha384Button;
    private javax.swing.JButton sha512Base64Button;
    private javax.swing.JButton sha512Button;
    private javax.swing.JComboBox strengthComboBox;
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
                if (undo.canUndo()) undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            redoAction.updateRedoState();
        }

        private void updateUndoState() {
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

        private static final long serialVersionUID = -2044519935735823050L;

        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                if (undo.canRedo()) undo.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            undoAction.updateUndoState();
        }

        private void updateRedoState() {
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
    private class MyUndoableEditListener
            implements UndoableEditListener {

        public void undoableEditHappened(UndoableEditEvent e) {
            //Remember the edit and update the menus.
            undo.addEdit(e.getEdit());
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }
}
