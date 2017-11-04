package myide;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;

/**
 *
 * @author anisha
 */

public class AutoIndentation extends DocumentFilter
{
 
    public void insertString(DocumentFilter.FilterBypass fb, int offs, String str, AttributeSet a)
        throws BadLocationException
    {
        if ("\n".equals(str))
            str = addWhiteSpace(fb.getDocument(), offs);

        super.insertString(fb, offs, str, a);
    }

    public void replace(DocumentFilter.FilterBypass fb, int offs, int length, String str, AttributeSet a)
        throws BadLocationException
    {
        if ("\n".equals(str))
            str = addWhiteSpace(fb.getDocument(), offs);

        super.replace(fb, offs, length, str, a);
    }

    private String addWhiteSpace(Document doc, int offset)
        throws BadLocationException
    {
        StringBuilder whiteSpace = new StringBuilder("\n");
        Element rootElement = doc.getDefaultRootElement();
        int line = rootElement.getElementIndex( offset );
        int i = rootElement.getElement(line).getStartOffset();

        while (true)
        {
            String temp = doc.getText(i, 1);

            if (temp.equals(" ") || temp.equals("\t"))
            {
                whiteSpace.append(temp);
                i++;
            }
            else
                break;
        }

        return whiteSpace.toString();
    }

    
}
