package myide;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

class Popup extends JPopupMenu
{
    final static long serialVersionUID = 0;
    
    Clipboard clipboard;
    JMenuItem jmenuItem_cut;
    JMenuItem jmenuItem_copy;
    JMenuItem jmenuItem_paste;
    JMenuItem jmenuItem_delete;
    JMenuItem jmenuItem_selectAll;
    JMenuItem jmenuItem_find;
    JMenuItem jmenuItem_search;
    JMenuItem jmenuItem_release;
    JTextComponent jtextComponent;
    Myide myide;
    public Popup()
    {

        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        jmenuItem_cut = new JMenuItem("cut");
        jmenuItem_cut.setEnabled(false);
        jmenuItem_cut.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                jtextComponent.cut();
            }
        });

        this.add(jmenuItem_cut);

        jmenuItem_copy = new JMenuItem("copy");
        jmenuItem_copy.setEnabled(false);
        jmenuItem_copy.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                jtextComponent.copy();
            }
        });

        this.add(jmenuItem_copy);

        jmenuItem_paste = new JMenuItem("paste");
        jmenuItem_paste.setEnabled(false);
        jmenuItem_paste.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                jtextComponent.paste();
            }
        });

        this.add(jmenuItem_paste);

        jmenuItem_delete = new JMenuItem("delete");
        jmenuItem_delete.setEnabled(false);
        jmenuItem_delete.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                jtextComponent.replaceSelection("");
            }
        });

        this.add(jmenuItem_delete);

        this.add(new JSeparator());

        jmenuItem_selectAll = new JMenuItem("select all");
        jmenuItem_selectAll.setEnabled(false);
        jmenuItem_selectAll.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                jtextComponent.selectAll();
            }
        });

        this.add(jmenuItem_selectAll);
        this.add(new JSeparator());
        jmenuItem_find = new JMenuItem("Find_Replace");
        jmenuItem_find.setEnabled(true);
        jmenuItem_find.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
               String find=JOptionPane.showInputDialog("enter word to be find");
               String replace=JOptionPane.showInputDialog("enter word to be replace");
               String c = jtextComponent.getText();
               String d = c.replaceAll(find, replace);
               jtextComponent.setText(d);
            }
        });

        this.add(jmenuItem_find);

        this.add(new JSeparator());
         jmenuItem_search = new JMenuItem("Search");
         jmenuItem_search.setEnabled(true);
         jmenuItem_search.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
              String a=JOptionPane.showInputDialog("enter word");
            if(a != null)
              myide.highlight((JTextArea) jtextComponent,a);
            }
        });

         this.add(jmenuItem_search);
         this.add(new JSeparator());
         jmenuItem_release = new JMenuItem("Release");
         jmenuItem_release.setEnabled(true);
         jmenuItem_release.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
              myide.removeHighlights((JTextArea) jtextComponent);
            }
        });

        this.add(jmenuItem_release);
    }
  

    public void add(JTextComponent jtextComponent)
    {
        myide=new Myide();
        jtextComponent.addMouseListener(new MouseAdapter()
        {
            public void mouseReleased(MouseEvent event)
            {
                if (event.getButton() == 3)
                {
                    processClick(event);
                }
            }
        });
    }

    private void processClick(MouseEvent event)
    {
        jtextComponent = (JTextComponent)event.getSource();
        boolean enableCut = false;
        boolean enableCopy = false;
        boolean enablePaste = false;
        boolean enableDelete = false;
        boolean enableSelectAll = false;

        String selectedText = jtextComponent.getSelectedText();
        String text = jtextComponent.getText();

        if (text != null)
        {
            if (text.length() > 0)
            {
                enableSelectAll = true;
            }
        }

        if (selectedText != null)
        {
            if (selectedText.length() > 0)
            {
                enableCut = true;
                enableCopy = true;
                enableDelete = true;
            }
        }

        try
        {
            if (clipboard.getData(DataFlavor.stringFlavor) != null)
            {
                enablePaste = true;
            }
        }
        catch (Exception exception)
        {
            System.out.println(exception);
        }
        jmenuItem_cut.setEnabled(enableCut);
        jmenuItem_copy.setEnabled(enableCopy);
        jmenuItem_paste.setEnabled(enablePaste);
        jmenuItem_delete.setEnabled(enableDelete);
        jmenuItem_selectAll.setEnabled(enableSelectAll);
        jmenuItem_find.setEnabled(true);
        jmenuItem_search.setEnabled(true);
        jmenuItem_release.setEnabled(true);
        this.show(jtextComponent,event.getX(),event.getY());
    }
}