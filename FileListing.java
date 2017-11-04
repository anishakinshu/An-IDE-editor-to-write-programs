package myide;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
public class FileListing implements Runnable{
 String str;
 File folder;
 String filename;
 RSyntaxTextArea textarea;
 RTextScrollPane jScrollPane1;
 JTabbedPane Projects;
 DefaultMutableTreeNode root;
 DefaultTreeModel treeModel;
 JTree tree;
 JTabbedPane jTabbedPane1;
public FileListing(JTabbedPane Projects, String filename,RSyntaxTextArea textarea,RTextScrollPane jScrollPane1,JTabbedPane jTabbedPane1){
     this.Projects=Projects;
     this.filename=filename;
     this.textarea=textarea;
     this.jTabbedPane1=jTabbedPane1;
     this.jScrollPane1=jScrollPane1;
}   
public void run() {
        File fileRoot = new File(filename);
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);
        
        CreateChildNodes ccn = new CreateChildNodes(fileRoot, root);
                
        new Thread(ccn).start();
        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        JScrollPane scrollPane = new JScrollPane(tree);
        Projects.add(scrollPane,"Projects");
        tree.addTreeSelectionListener(new TreeSelectionListener() {

@Override
public void valueChanged(TreeSelectionEvent e) {
   //DefaultMutableTreeNode selectedNode = 
     //  (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();  
            
          TreePath tp = e.getNewLeadSelectionPath();  
          if (tp != null) {
             DefaultMutableTreeNode   pathForNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
            str=filename+"/"+String.valueOf(pathForNode);
          }
          open();
} 

});
 }
 public void open()
 {
     Myide myide=new Myide();
     String file4=filename+"/";
     myide.setName(str,file4);
        /*FileDialog fileDialog=new FileDialog(myide,"open File",FileDialog.LOAD);
       fileDialog.setVisible(true);
       if(fileDialog.getFile()!=null)
       {
           DirectoryPath=fileDialog.getDirectory();
           filename=DirectoryPath+fileDialog.getFile
           setTitle(filename);
           System.out.println(filename);
       }*/
       try
       {
           File file=new File(str);
           BufferedReader reader=new BufferedReader(new FileReader(file));
           StringBuilder sb=new StringBuilder();
           String line=null;
           while((line=reader.readLine())!=null)
           {
              sb.append(line+"\n");
              textarea.setText(sb.toString());
           }
           reader.close();
            String file1[]=str.split("/");
           int l=file1.length;
           jTabbedPane1.setTitleAt(0,file1[l-1]);
           TextLineNumber lineNumberingTextArea = new TextLineNumber(textarea);
        jScrollPane1.setRowHeaderView(lineNumberingTextArea);
        //createAndShowUI();
       }
       catch(IOException e)
       {
           System.out.println("file not found");
       } 

 }
public class CreateChildNodes implements Runnable {

        private DefaultMutableTreeNode root;

        private File fileRoot;

        public CreateChildNodes(File fileRoot, 
                DefaultMutableTreeNode root) {
            this.fileRoot = fileRoot;
            this.root = root;
        }

        @Override
        public void run() {
            createChildren(fileRoot, root);
        }
         private void createChildren(File fileRoot, 
            DefaultMutableTreeNode node) {
            File[] files = fileRoot.listFiles();
            if (files == null) return;

            for (File file : files) {
                DefaultMutableTreeNode childNode = 
                        new DefaultMutableTreeNode(new FileNode(file));
                node.add(childNode);
                if (file.isDirectory()) {
                    createChildren(file, childNode);
                }
            }
         }

}
public class FileNode {

        private File file3;

        public FileNode(File file) {
            this.file3 = file;
        }

        @Override
        public String toString() {
            String name = file3.getName();
            if (name.equals("")) {
                return file3.getAbsolutePath();
            } else {
                return name;
            }
        }
    }
}