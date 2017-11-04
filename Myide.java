/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myide;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import static java.awt.Font.PLAIN;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_SPACE;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.text.StyleConstants;
import org.fife.ui.rsyntaxtextarea.CodeTemplateManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
import org.fife.ui.rtextarea.RTextScrollPane;
/**
 *
 * @author anisha
 */






    public class Myide extends javax.swing.JFrame {
        RSyntaxTextArea jTextArea1;
        RTextScrollPane jScrollPane1;
        String filename, location;
        String DirectoryPath;
        int k;
        Font font1;
        String ffamily;
        int fsize;
        long startTime;
        long stopTime;
    /**
     * Creates new form Myide
     */
    public Myide() {
        initComponents();
        ffamily="Arial";
        fsize=15;
        jTextArea1 = new RSyntaxTextArea(20, 60);
        jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
        jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        jTextArea1.setCodeFoldingEnabled(true);
        PerformUndoAction obj=new PerformUndoAction ();
        obj.initialize();
        TextLineNumber lineNumberingTextArea = new TextLineNumber(jTextArea1);
        jScrollPane1 = new RTextScrollPane(jTextArea1);
        jTabbedPane1.add("Untitled",jScrollPane1);
        jScrollPane1.setRowHeaderView(lineNumberingTextArea);
        AbstractDocument doc = (AbstractDocument)jTextArea1.getDocument();
        doc.setDocumentFilter( new AutoIndentation() );
        
        
        jTextArea1.addCaretListener(new javax.swing.event.CaretListener() {
                public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTextArea1CaretUpdate(evt);
                Popup popup = new Popup();
                popup.add(jTextArea1);
                jTextArea1.setComponentPopupMenu(popup);
            }
        });
        
        autoupdate();
        location = "/home/anisha/NetBeansProjects/MyIDE/src/myide";
        // File currdir =  new File(location); 
        FileListing filelisting = new FileListing(jTabbedPane3,location,jTextArea1,jScrollPane1,jTabbedPane1);
        SwingUtilities.invokeLater(filelisting);
    }
    public void autoupdate()
    {
        AutoUpdate auto=new AutoUpdate(jTextArea1);
        jTextArea1.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (auto.suggestion != null) {
                        if (auto.suggestion.insertSelection()) {
                            e.consume();
                            final int position = jTextArea1.getCaretPosition();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        jTextArea1.getDocument().remove(position - 1, 1);
                                    } catch (BadLocationException e) {
                                        e.printStackTrace();
                                      }
                                }
                            });
                        }
                    }
                }
            }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN && auto.suggestion != null) {
                auto.suggestion.moveDown();
            } else if (e.getKeyCode() == KeyEvent.VK_UP && auto.suggestion != null) {
                auto.suggestion.moveUp();
              } else if (Character.isLetterOrDigit(e.getKeyChar())) {
                    auto.showSuggestionLater();
               } else if (Character.isWhitespace(e.getKeyChar())) {
                    auto.hideSuggestion();
               }
         }

        @Override
        public void keyPressed(KeyEvent e) {

        }
        });

    }
    /*public void Filelisting()
    {
        Test obj2=new Test();
        
    }*/
    private void jTextArea1CaretUpdate(javax.swing.event.CaretEvent evt) {                                       
        // TODO add your handling code here:
        int lineNumber=0, column=0, pos=0;

        try
        {
            pos=jTextArea1.getCaretPosition();
            lineNumber=jTextArea1.getLineOfOffset(pos);
            column=pos-jTextArea1.getLineStartOffset(lineNumber);
        }catch(Exception excp){}
        if(jTextArea1.getText().length()==0){lineNumber=0; column=0;}
            jLabel2.setText("||       Ln "+(lineNumber+1)+", Col "+(column+1));

    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        jfind = new javax.swing.JLabel();
        t1 = new javax.swing.JTextField();
        jreplace = new javax.swing.JLabel();
        t2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jgo = new javax.swing.JButton();
        jsearch = new javax.swing.JLabel();
        t3 = new javax.swing.JTextField();
        jgo1 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jstdout = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jstdin = new javax.swing.JTextArea();
        release = new javax.swing.JButton();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        showCompileTime = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jnew = new javax.swing.JMenuItem();
        jopen = new javax.swing.JMenuItem();
        jsave = new javax.swing.JMenuItem();
        jexit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jcut = new javax.swing.JMenuItem();
        jcopy = new javax.swing.JMenuItem();
        jpaste = new javax.swing.JMenuItem();
        jUndo = new javax.swing.JMenuItem();
        jredo = new javax.swing.JMenuItem();
        jtools = new javax.swing.JMenu();
        jstyle = new javax.swing.JMenu();
        plain = new javax.swing.JMenuItem();
        bold = new javax.swing.JMenuItem();
        italic = new javax.swing.JMenuItem();
        jsize = new javax.swing.JMenu();
        size8 = new javax.swing.JMenuItem();
        size1 = new javax.swing.JMenuItem();
        size2 = new javax.swing.JMenuItem();
        size3 = new javax.swing.JMenuItem();
        size4 = new javax.swing.JMenuItem();
        size5 = new javax.swing.JMenuItem();
        size6 = new javax.swing.JMenuItem();
        size7 = new javax.swing.JMenuItem();
        jfamily = new javax.swing.JMenu();
        agency = new javax.swing.JMenuItem();
        antiqua = new javax.swing.JMenuItem();
        calibiri = new javax.swing.JMenuItem();
        arial = new javax.swing.JMenuItem();
        comic = new javax.swing.JMenuItem();
        courier = new javax.swing.JMenuItem();
        cursive = new javax.swing.JMenuItem();
        serif = new javax.swing.JMenuItem();
        noto = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jccompiler = new javax.swing.JMenuItem();
        jcrun = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jcppcompile = new javax.swing.JMenuItem();
        jcpprun = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        javacompile = new javax.swing.JMenuItem();
        javarun = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MYIDE");
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        jTabbedPane1.setBackground(new java.awt.Color(242, 241, 240));
        jTabbedPane1.setForeground(new java.awt.Color(60, 60, 60));
        jTabbedPane1.setOpaque(true);

        jLabel1.setText("jLabel1");

        jfind.setText("FIND");
        jfind.setOpaque(true);

        t1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1ActionPerformed(evt);
            }
        });

        jreplace.setText("REPLACE");
        jreplace.setOpaque(true);

        t2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t2ActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(239, 142, 68));
        jLabel2.setForeground(javax.swing.UIManager.getDefaults().getColor("Label.foreground"));
        jLabel2.setOpaque(true);

        jgo.setBackground(new java.awt.Color(60, 60, 60));
        jgo.setText("GO");
        jgo.setOpaque(true);
        jgo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jgoActionPerformed(evt);
            }
        });

        jsearch.setText("SEARCH");
        jsearch.setOpaque(true);

        t3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t3ActionPerformed(evt);
            }
        });

        jgo1.setBackground(new java.awt.Color(60, 60, 60));
        jgo1.setText("SEARCH");
        jgo1.setOpaque(true);
        jgo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jgo1ActionPerformed(evt);
            }
        });

        jstdout.setColumns(20);
        jstdout.setRows(5);
        jScrollPane3.setViewportView(jstdout);

        jTabbedPane2.addTab("STDOUT", jScrollPane3);

        jstdin.setColumns(20);
        jstdin.setRows(5);
        jScrollPane2.setViewportView(jstdin);

        jTabbedPane2.addTab("STDIN", jScrollPane2);

        release.setBackground(new java.awt.Color(60, 60, 60));
        release.setText("RELEASE");
        release.setOpaque(true);
        release.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                releaseActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jnew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jnew.setText("NEW");
        jnew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jnewActionPerformed(evt);
            }
        });
        jMenu1.add(jnew);

        jopen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jopen.setText("OPEN");
        jopen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jopenActionPerformed(evt);
            }
        });
        jMenu1.add(jopen);

        jsave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jsave.setText("SAVE");
        jsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jsaveActionPerformed(evt);
            }
        });
        jMenu1.add(jsave);

        jexit.setText("EXIT");
        jexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jexitActionPerformed(evt);
            }
        });
        jMenu1.add(jexit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jcut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jcut.setText("CUT");
        jcut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcutActionPerformed(evt);
            }
        });
        jMenu2.add(jcut);

        jcopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jcopy.setText("COPY");
        jcopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcopyActionPerformed(evt);
            }
        });
        jMenu2.add(jcopy);

        jpaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jpaste.setText("PASTE");
        jpaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jpasteActionPerformed(evt);
            }
        });
        jMenu2.add(jpaste);

        jUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jUndo.setText("UNDO");
        jUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUndoActionPerformed(evt);
            }
        });
        jMenu2.add(jUndo);

        jredo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jredo.setText("REDO");
        jredo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jredoActionPerformed(evt);
            }
        });
        jMenu2.add(jredo);

        jMenuBar1.add(jMenu2);

        jtools.setText("TOOLS");

        jstyle.setText("STYLE");
        jstyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jstyleActionPerformed(evt);
            }
        });

        plain.setText("PLAIN");
        plain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plainActionPerformed(evt);
            }
        });
        jstyle.add(plain);

        bold.setText("BOLD");
        bold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boldActionPerformed(evt);
            }
        });
        jstyle.add(bold);

        italic.setText("ITALLIC");
        italic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                italicActionPerformed(evt);
            }
        });
        jstyle.add(italic);

        jtools.add(jstyle);

        jsize.setText("SIZE");

        size8.setText("15");
        size8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size8ActionPerformed(evt);
            }
        });
        jsize.add(size8);

        size1.setText("20");
        size1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size1ActionPerformed(evt);
            }
        });
        jsize.add(size1);

        size2.setText("30");
        size2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size2ActionPerformed(evt);
            }
        });
        jsize.add(size2);

        size3.setText("40");
        size3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size3ActionPerformed(evt);
            }
        });
        jsize.add(size3);

        size4.setText("50");
        size4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size4ActionPerformed(evt);
            }
        });
        jsize.add(size4);

        size5.setText("60");
        size5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size5ActionPerformed(evt);
            }
        });
        jsize.add(size5);

        size6.setText("70");
        size6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size6ActionPerformed(evt);
            }
        });
        jsize.add(size6);

        size7.setText("80");
        size7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size7ActionPerformed(evt);
            }
        });
        jsize.add(size7);

        jtools.add(jsize);

        jfamily.setText("FAMILY");

        agency.setText("AGENCY FB");
        agency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agencyActionPerformed(evt);
            }
        });
        jfamily.add(agency);

        antiqua.setText("Antiqua");
        antiqua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                antiquaActionPerformed(evt);
            }
        });
        jfamily.add(antiqua);

        calibiri.setText("Calibiri");
        calibiri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calibiriActionPerformed(evt);
            }
        });
        jfamily.add(calibiri);

        arial.setText("Arial");
        arial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arialActionPerformed(evt);
            }
        });
        jfamily.add(arial);

        comic.setText("Comic Sans");
        comic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comicActionPerformed(evt);
            }
        });
        jfamily.add(comic);

        courier.setText("Courier");
        courier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courierActionPerformed(evt);
            }
        });
        jfamily.add(courier);

        cursive.setText("Cursive");
        cursive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cursiveActionPerformed(evt);
            }
        });
        jfamily.add(cursive);

        serif.setText("Serif");
        serif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serifActionPerformed(evt);
            }
        });
        jfamily.add(serif);

        noto.setText("Noto Sans CJK SC Black");
        noto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notoActionPerformed(evt);
            }
        });
        jfamily.add(noto);

        jtools.add(jfamily);

        jMenuBar1.add(jtools);

        jMenu4.setText("LANGUAGE");

        jMenu5.setText("C");
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });

        jccompiler.setText("Compile");
        jccompiler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jccompilerActionPerformed(evt);
            }
        });
        jMenu5.add(jccompiler);

        jcrun.setText("Run");
        jcrun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcrunActionPerformed(evt);
            }
        });
        jMenu5.add(jcrun);

        jMenu4.add(jMenu5);

        jMenu6.setText("C++");

        jcppcompile.setText("Compile");
        jcppcompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcppcompileActionPerformed(evt);
            }
        });
        jMenu6.add(jcppcompile);

        jcpprun.setText("Run");
        jcpprun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcpprunActionPerformed(evt);
            }
        });
        jMenu6.add(jcpprun);

        jMenu4.add(jMenu6);

        jMenu7.setText("JAVA");

        javacompile.setText("Compile");
        javacompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javacompileActionPerformed(evt);
            }
        });
        jMenu7.add(javacompile);

        javarun.setText("Run");
        javarun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javarunActionPerformed(evt);
            }
        });
        jMenu7.add(javarun);

        jMenu4.add(jMenu7);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(372, 372, 372))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(t1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(t2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addComponent(jfind)
                                                    .addGap(62, 62, 62))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addComponent(jreplace)
                                                    .addGap(40, 40, 40))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addComponent(jgo)
                                                    .addGap(49, 49, 49)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jgo1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(release))
                                            .addComponent(t3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addComponent(jsearch)))
                                .addGap(5, 5, 5)))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(549, 549, 549)
                .addComponent(showCompileTime)
                .addContainerGap(368, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jfind)
                        .addGap(18, 18, 18)
                        .addComponent(t1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jreplace)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jgo)
                        .addGap(18, 18, 18)
                        .addComponent(jsearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jgo1)
                            .addComponent(release))
                        .addGap(6, 36, Short.MAX_VALUE))
                    .addComponent(jTabbedPane1)
                    .addComponent(jTabbedPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showCompileTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jnewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jnewActionPerformed
        jTextArea1.setText("");
        jstdin.setText("");
        jstdout.setText("");
        showCompileTime.setText("");
        PerformUndoAction obj=new PerformUndoAction ();
        obj.initialize();
        
        TextLineNumber lineNumberingTextArea = new TextLineNumber(jTextArea1);
        jScrollPane1.setRowHeaderView(lineNumberingTextArea);
        //createAndShowUI();
        
        
        
    }//GEN-LAST:event_jnewActionPerformed
    final Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
    private void jcutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcutActionPerformed
        String selection=jTextArea1.getSelectedText();
        StringSelection data=new StringSelection(selection);
        clip.setContents(data, data);
        jTextArea1.replaceRange("", jTextArea1.getSelectionStart(), jTextArea1.getSelectionEnd());
    }//GEN-LAST:event_jcutActionPerformed

    private void jexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jexitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jexitActionPerformed

    private void comicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comicActionPerformed
        ffamily="Comic Sans";
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_comicActionPerformed

    private void jsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jsaveActionPerformed
        FileDialog fileDialog=new FileDialog(Myide.this,"save File",FileDialog.SAVE);
        fileDialog.setVisible(true);
        if(fileDialog.getFile()!=null)
       {
           DirectoryPath=fileDialog.getDirectory();
           filename=DirectoryPath+fileDialog.getFile();
           //setTitle(filename);
           try
       {
           FileWriter fileWriter=new FileWriter(filename);
           fileWriter.write(jTextArea1.getText());
           //setTitle(filename);
           fileWriter.close();
           String file[]=filename.split("/");
           int l=file.length;
           jTabbedPane1.setTitleAt(0,file[l-1]);
       }
       catch(IOException e)
       {
           System.out.println("file not found");
       }
       }
    }//GEN-LAST:event_jsaveActionPerformed
    public void setName(String name,String path)
    {
        filename=name;
        DirectoryPath=path;
    }
    public boolean isAlive(Process p) {
        try {
         p.exitValue();
         return false;
        }
        catch (IllegalThreadStateException e) {
         return true;
        }
    }
   /* public class StreamConsumer extends Thread {

        private InputStream is;
        private OutputStream os;
        private IOException exp;
        private String name;
        //public StringBuilder output;
        private BufferedReader in;
        //String[] lines;
        public StreamConsumer(InputStream is,String name) {
            this.is = is;
            this.name=name;
        }
        public StreamConsumer(OutputStream os) {
            this.os = os;
        }

        @Override
        public void run() {
            String line = null;
            char str;
           
            try {
                if(is.available()>0)
                {
                InputStreamReader isr=new InputStreamReader(is);
                in = new BufferedReader(isr);
    while ((line = in.readLine()) != null) {
         //jstdout.setText("");
        jstdout.append(name + " " + line+"\n");
            }
    isr.close();
    
                }
    else if(System.in.available()>0)
            {
           BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
PrintStream ps=new PrintStream(os);
    
      do
    {
      str =(char) br.read();
      ps.println(str);
      //i++;
    } while(str!='\n');
      ps.flush();
      ps.close();
      br.close();

            }
                }catch (IOException ex) {
                ex.printStackTrace();
                exp = ex;
            }
        }
            
}*/  
    
    public class InputStreamConsumer extends Thread {

        private InputStream is;
        private OutputStream os;
        private IOException exp;
        private String name;
        //public StringBuilder output;
        private BufferedReader in;
        //String[] lines;
        public InputStreamConsumer(InputStream is,String name) {
            this.is = is;
            this.name=name;
        }
        @Override
        public void run() {
            String line = null;
            char str;
           
            try {
                if(is.available()>0)
                {
                InputStreamReader isr=new InputStreamReader(is);
                in = new BufferedReader(isr);
                while ((line = in.readLine()) != null) {
                //jstdout.setText("");
                jstdout.append(name + " " + line+"\n");
                }
                isr.close();
    
                }
                }catch (IOException ex) {
                ex.printStackTrace();
                exp = ex;
            }
        }
            
    } 


    public class ErrorStreamConsumer extends Thread {

        private InputStream is;
        private OutputStream os;
        private IOException exp;
        private String name;
        //public StringBuilder output;
        private BufferedReader in;
        //String[] lines;
        public ErrorStreamConsumer(InputStream is,String name) {
            this.is = is;
            this.name=name;
        }

        @Override
        public void run() {
            String line = null;
            char str;
           
            try {
                if(is.available()>0)
                {
                InputStreamReader isr=new InputStreamReader(is);
                in = new BufferedReader(isr);
                while ((line = in.readLine()) != null) {
                //jstdout.setText("");
                   jstdout.append(name + " " + line+"\n");
                }
                isr.close();
    
                }
                }catch (IOException ex) {
                ex.printStackTrace();
                exp = ex;
            }
        }
            
    }


    public class OutputStreamConsumer extends Thread {

        private InputStream is;
        private OutputStream os;
        private IOException exp;
        private String name;
        //public StringBuilder output;
        private BufferedReader in;
        //String[] lines;
        
        public OutputStreamConsumer(OutputStream os) {
            this.os = os;
        }

        @Override
        public void run() {
            String line = null;
            char str;
           
        try{
                
            if(System.in.available()>0)
            {
              BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
              PrintStream ps=new PrintStream(os);
    
             do
             {
                str =(char) br.read();
                ps.println(str);
    
             } while(str!='\n');
              ps.flush();
              ps.close();
              br.close();

            }
        }catch (IOException ex) {
              ex.printStackTrace();
              exp = ex;
         }
        }
            
    }                                    
    
   public void printLines(String name, InputStream ins, Process pro) throws Exception {
   //OutputStream os = pro.getOutputStream();
     //       PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
     /*String[] lines=jstdin.getText().split("\\n");
     for(String str:lines)
         System.out.println(str);*/
          InputStream is=pro.getInputStream();
          OutputStream os=pro.getOutputStream();
          InputStream es=pro.getErrorStream();
      while(isAlive(pro))
      {
          if(is.available()>0)
          {
             InputStreamConsumer consumer = new InputStreamConsumer(is,name);
             consumer.start();
         
             pro.waitFor();

             consumer.join();
          }
          else if(es.available()>0)
          {
             ErrorStreamConsumer consumer1 = new ErrorStreamConsumer(es,name);
             consumer1.start();
             pro.waitFor();
             consumer1.join();  
          }
          else if(System.in.available()>0)
          {
              jstdout.setText("");
              OutputStreamConsumer consumer_out = new OutputStreamConsumer(os);
              consumer_out.start();
         
              pro.waitFor();

              consumer_out.join();
           } 
          
      }
  }

  public int runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command, pro.getInputStream(),pro);
        printLines(command , pro.getErrorStream(),pro);
    //System.out.println(pro.exitValue());
        return pro.exitValue();
    
  }
    private void jcppcompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcppcompileActionPerformed
    try {
        startTime = System.currentTimeMillis();
        String file[]=DirectoryPath.split("/");
        int l=file.length;
        String s="";
        for(int i=1;i<(l-2);i++)
           s+="/"+file[i];
           k = runProcess("g++ -d "+s+" "+filename); 
        if(k==0)
        {
            jstdout.append("Compiled successfully\n");
            savecontent();
        }
        else{
            System.out.println("oops some error has occured");
        }
    } catch (Exception e) {
      e.printStackTrace();
    }
      stopTime = System.currentTimeMillis();
      long elapsedTime = stopTime - startTime;
      showCompileTime.setText("compile time "+elapsedTime+" msec");
    }//GEN-LAST:event_jcppcompileActionPerformed

    private void jccompilerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jccompilerActionPerformed
        try {
            startTime = System.currentTimeMillis();
            String file[]=DirectoryPath.split("/");
            int l=file.length;
            String s="";
            for(int i=1;i<(l-2);i++)
            s+="/"+file[i];
            k = runProcess("gcc -d "+s+" "+filename); 
            if(k==0)
            {
                jstdout.append("Compiled successfully\n");
                savecontent();
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        showCompileTime.setText("compile time "+elapsedTime+" msec");
    }//GEN-LAST:event_jccompilerActionPerformed

    private void jcrunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcrunActionPerformed
        try {
             startTime = System.currentTimeMillis();
             String file[]=filename.split("/");
             int l=file.length;
             String file1[]=file[l-1].split("\\.");
             if(k==0)
               k=runProcess("./a.out");
             stopTime = System.currentTimeMillis();
             long elapsedTime = stopTime - startTime;
             showCompileTime.setText("run time "+elapsedTime+" msec");
        } catch (Exception e) {
             e.printStackTrace();
        }
    }//GEN-LAST:event_jcrunActionPerformed

    private void jcpprunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcpprunActionPerformed
         try {
           startTime = System.currentTimeMillis();
           String file[]=filename.split("/");
           int l=file.length;
           String file1[]=file[l-1].split("\\.");
           if(k==0)
               k=runProcess("./a.out");
           stopTime = System.currentTimeMillis();
           long elapsedTime = stopTime - startTime;
           showCompileTime.setText("run time "+elapsedTime+" msec");
        } catch (Exception e) {
              e.printStackTrace();
        }
         
    }//GEN-LAST:event_jcpprunActionPerformed
    public void savecontent()
    {
 
      String s=jstdin.getText();
    
      try {
            File newTextFile = new File("/home/anisha/NetBeansProjects/MyIDE/src/myide/f.txt");

            FileWriter fw = new FileWriter(newTextFile);
            fw.write(s);
            fw.close();

      } catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        }
    }
    private void javacompileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javacompileActionPerformed
    try {
         startTime = System.currentTimeMillis();
         String file[]=DirectoryPath.split("/");
         int l=file.length;
         String s="";
         for(int i=1;i<(l-2);i++)
           s+="/"+file[i];
          k = runProcess("javac -d "+s+" "+filename); 
             if(k==0)
             {
                 jstdout.append("Compiled successfully\n");
       
                 savecontent();
        
             }
    } catch (Exception e) {
      e.printStackTrace();
    }        
    stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
     showCompileTime.setText("compile time "+elapsedTime+" msec");
    }//GEN-LAST:event_javacompileActionPerformed

    private void javarunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javarunActionPerformed
    try {
        startTime = System.currentTimeMillis();
        String file[]=filename.split("/");
        int l=file.length;
            
        String file1[]=file[l-1].split("\\.");
        if (k==0)
         k=runProcess("java "+file1[0]);
         stopTime = System.currentTimeMillis();
         long elapsedTime = stopTime - startTime;
         showCompileTime.setText("run time "+elapsedTime+" msec");
    } catch (Exception e) {
      e.printStackTrace();
    } 

    }//GEN-LAST:event_javarunActionPerformed
    UndoManager undoManager=new UndoManager();
    public class PerformUndoAction 
    {
        public void initialize()
        {
           font1=new Font(ffamily,Font.PLAIN,fsize);
           jTextArea1.setFont(font1);
           jTextArea1.getDocument().addUndoableEditListener(
           new UndoableEditListener() {
                public void undoableEditHappened(UndoableEditEvent e) {
                       undoManager.addEdit(e.getEdit());
                }
           });
        }
    }
    private void jopenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jopenActionPerformed
        PerformUndoAction obj=new PerformUndoAction ();
        obj.initialize();
        FileDialog fileDialog=new FileDialog(Myide.this,"open File",FileDialog.LOAD);
        fileDialog.setVisible(true);
        if(fileDialog.getFile()!=null)
        {
           DirectoryPath=fileDialog.getDirectory();
           filename=DirectoryPath+fileDialog.getFile();
          
       }
       try
       {
           BufferedReader reader=new BufferedReader(new FileReader(filename));
           StringBuilder sb=new StringBuilder();
           String line=null;
           while((line=reader.readLine())!=null)
           {
              sb.append(line+"\n");
              jTextArea1.setText(sb.toString());
           }
            reader.close();
            String file[]=filename.split("/");
           int l=file.length;
           jTabbedPane1.setTitleAt(0,file[l-1]);
           TextLineNumber lineNumberingTextArea = new TextLineNumber(jTextArea1);
           jScrollPane1.setRowHeaderView(lineNumberingTextArea);
        //createAndShowUI();
       }
       catch(IOException e)
       {
           System.out.println("file not found");
       } 
    }//GEN-LAST:event_jopenActionPerformed

    private void jgoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jgoActionPerformed
         String a = t1.getText();
         String b = t2.getText();
         String c = jTextArea1.getText();
         String d = c.replaceAll(a, b);
         jTextArea1.setText(d);
         t2.setText("");
         t1.setText("");
    }//GEN-LAST:event_jgoActionPerformed
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter{
          public MyHighlightPainter(Color color){
             super(color);
          }
    }
    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red); 
    public void removeHighlights(javax.swing.JTextArea jTextArea1)
    {
         Highlighter highlighter = jTextArea1.getHighlighter();
         Highlighter.Highlight[] highlighters=highlighter.getHighlights();
         for(int i=0;i<highlighters.length;i++)
         {
             if(highlighters[i].getPainter() instanceof MyHighlightPainter)
             {
                 highlighter.removeHighlight(highlighters[i]);
             }
         }
    }
    public void highlight(javax.swing.JTextArea jTextArea1,String a){
    try{
        removeHighlights(jTextArea1);
        Highlighter highlighter = jTextArea1.getHighlighter();
        Document doc=jTextArea1.getDocument();
        String text=doc.getText(0,doc.getLength());
        int pos=0;
        while((pos=text.toUpperCase().indexOf(a.toUpperCase(),pos))>=0)
        {
           highlighter.addHighlight(pos, pos+a.length(), myHighlightPainter); 
           pos += a.length();
        }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    }
    
    
    private void jgo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jgo1ActionPerformed

        String a=t3.getText();
        if(!a.isEmpty())
        highlight(jTextArea1,a);
    }//GEN-LAST:event_jgo1ActionPerformed

    private void t1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t1ActionPerformed

    private void t3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t3ActionPerformed
    private void jcopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcopyActionPerformed
               String selection = jTextArea1.getSelectedText();
               if (selection == null)
                  return;
               StringSelection clipString =new StringSelection(selection);
               clip.setContents(clipString,clipString); 
    }//GEN-LAST:event_jcopyActionPerformed

    private void jpasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jpasteActionPerformed
                 Transferable clipData = clip.getContents(Myide.this);
                     try
                    {
                         String clipString = (String)clipData.getTransferData(DataFlavor.stringFlavor);
                         jTextArea1.replaceRange(clipString,jTextArea1.getSelectionStart(),jTextArea1.getSelectionEnd());
                    }    catch(Exception ex){
                              System.err.println("Not Working");
                        }
    }//GEN-LAST:event_jpasteActionPerformed

        
    private void jUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUndoActionPerformed

            try
            {
                undoManager.undo();
            }
            catch (CannotUndoException e)
            {
                Toolkit.getDefaultToolkit().beep();
            }
    }//GEN-LAST:event_jUndoActionPerformed

    private void jredoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jredoActionPerformed
        try
            {
                undoManager.redo();
            }
            catch (CannotRedoException e)
            {
                Toolkit.getDefaultToolkit().beep();
            }
    }//GEN-LAST:event_jredoActionPerformed

    private void jstyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jstyleActionPerformed
       
    }//GEN-LAST:event_jstyleActionPerformed

    private void plainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plainActionPerformed
        font1=new Font(ffamily,Font.PLAIN,fsize);
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_plainActionPerformed

    private void boldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boldActionPerformed
        font1=new Font(ffamily,Font.BOLD,fsize);
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_boldActionPerformed

    private void italicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_italicActionPerformed
        font1=new Font(ffamily,Font.ITALIC,fsize);
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_italicActionPerformed
    public void changefont()
   {
     if(font1.isBold())
        font1=new Font(ffamily,Font.BOLD,fsize);
     else if(font1.isItalic())
        font1=new Font(ffamily,Font.ITALIC,fsize);
     else if(font1.isPlain())
         font1=new Font(ffamily,Font.PLAIN,fsize);
   }
    private void size1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size1ActionPerformed
        fsize=20;
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_size1ActionPerformed

    private void size2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size2ActionPerformed
        fsize=30;
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_size2ActionPerformed

    private void size3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size3ActionPerformed
        fsize=40;
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_size3ActionPerformed

    private void size4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size4ActionPerformed
       fsize=50;
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_size4ActionPerformed

    private void size5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size5ActionPerformed
        fsize=60;
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_size5ActionPerformed

    private void size6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size6ActionPerformed
        fsize=70;
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_size6ActionPerformed

    private void size7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size7ActionPerformed
      fsize=80;
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_size7ActionPerformed

    private void agencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agencyActionPerformed
       ffamily="Agency FB";
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_agencyActionPerformed

    private void antiquaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_antiquaActionPerformed
        ffamily="Antiqua";
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_antiquaActionPerformed

    private void calibiriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calibiriActionPerformed
        ffamily="Calibri";
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_calibiriActionPerformed

    private void arialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arialActionPerformed
        ffamily="Arial";
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_arialActionPerformed

    private void courierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courierActionPerformed
        ffamily="Courier";
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_courierActionPerformed

    private void cursiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cursiveActionPerformed
       ffamily="Cursive";
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_cursiveActionPerformed

    private void serifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serifActionPerformed
        ffamily="Serif";
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_serifActionPerformed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        
    }//GEN-LAST:event_formMouseReleased

    private void releaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_releaseActionPerformed
        removeHighlights(jTextArea1);
        t3.setText("");
    }//GEN-LAST:event_releaseActionPerformed

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu5ActionPerformed

    private void notoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notoActionPerformed
        ffamily="Noto Sans CJK SC Black";
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_notoActionPerformed

    private void size8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size8ActionPerformed
        fsize=15;
        changefont();
        jTextArea1.setFont(font1);
    }//GEN-LAST:event_size8ActionPerformed

    private void t2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t2ActionPerformed

    /*private void createAndShowUI()
    {
        AbstractDocument doc = (AbstractDocument)jTextArea1.getDocument();
        doc.setDocumentFilter( new NewLineFilter() );

    }*/

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Myide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Myide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Myide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Myide.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Myide().setVisible(true);
               
            }
        });
       
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem agency;
    private javax.swing.JMenuItem antiqua;
    private javax.swing.JMenuItem arial;
    private javax.swing.JMenuItem bold;
    private javax.swing.JMenuItem calibiri;
    private javax.swing.JMenuItem comic;
    private javax.swing.JMenuItem courier;
    private javax.swing.JMenuItem cursive;
    private javax.swing.JMenuItem italic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JMenuItem jUndo;
    private javax.swing.JMenuItem javacompile;
    private javax.swing.JMenuItem javarun;
    private javax.swing.JMenuItem jccompiler;
    private javax.swing.JMenuItem jcopy;
    private javax.swing.JMenuItem jcppcompile;
    private javax.swing.JMenuItem jcpprun;
    private javax.swing.JMenuItem jcrun;
    private javax.swing.JMenuItem jcut;
    private javax.swing.JMenuItem jexit;
    private javax.swing.JMenu jfamily;
    private javax.swing.JLabel jfind;
    private javax.swing.JButton jgo;
    private javax.swing.JButton jgo1;
    private javax.swing.JMenuItem jnew;
    private javax.swing.JMenuItem jopen;
    private javax.swing.JMenuItem jpaste;
    private javax.swing.JMenuItem jredo;
    private javax.swing.JLabel jreplace;
    private javax.swing.JMenuItem jsave;
    private javax.swing.JLabel jsearch;
    private javax.swing.JMenu jsize;
    private javax.swing.JTextArea jstdin;
    private javax.swing.JTextArea jstdout;
    private javax.swing.JMenu jstyle;
    private javax.swing.JMenu jtools;
    private javax.swing.JMenuItem noto;
    private javax.swing.JMenuItem plain;
    private javax.swing.JButton release;
    private javax.swing.JMenuItem serif;
    private javax.swing.JLabel showCompileTime;
    private javax.swing.JMenuItem size1;
    private javax.swing.JMenuItem size2;
    private javax.swing.JMenuItem size3;
    private javax.swing.JMenuItem size4;
    private javax.swing.JMenuItem size5;
    private javax.swing.JMenuItem size6;
    private javax.swing.JMenuItem size7;
    private javax.swing.JMenuItem size8;
    private javax.swing.JTextField t1;
    private javax.swing.JTextField t2;
    private javax.swing.JTextField t3;
    // End of variables declaration//GEN-END:variables
}



