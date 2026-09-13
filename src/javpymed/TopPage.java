/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package javpymed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.kordamp.ikonli.swing.FontIcon;

/**
 *
 * @author mashmash
 */
public class TopPage extends JFrame implements ActionListener{
    private static final String[] ALLOWED_EXTENSIONS = {"mp4", "avi", "mkv", "mov", "3gp", "wmv", "flv"};
    private File selectedFile;   
    private JLabel fileLabel; 
    private JButton chooseButton;
    
    TopPage(){
        FrontPanel();    
    }
    
    private void FrontPanel(){
        setSize(1280,600);
            setLocationRelativeTo(null);
                addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent we){
                      int option = JOptionPane.showConfirmDialog(TopPage.this,"Are you sure you want to exit?", "EXIT", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                      if(option==JOptionPane.YES_OPTION){
                          System.exit(0);
                      }else{
                          setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                          return;
                      }
                    }
                });
                
         JPanel front = new JPanel(new BorderLayout());
         front.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
         //add mext panel,clean,scaling
         front.add(BuildUI(),BorderLayout.CENTER);
         setContentPane(front);
         setVisible(true);
    }
    
    
    
    private JPanel BuildUI(){
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createDashedBorder(Color.GRAY),BorderFactory.createEmptyBorder(30, 20, 30, 20)));
    
    JLabel heading = new JLabel("  Drag video or click + button below : ");
    heading.setAlignmentX(Component.CENTER_ALIGNMENT);
    
     chooseButton = new JButton("Choose file");
     chooseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
     chooseButton.addActionListener(new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent ae){
             openFile();
         }
     });
     
     fileLabel = new JLabel("");
     if(fileLabel.getText().equals("")){
         fileLabel.setText("No file chosen");
     }
        
        System.out.println(fileLabel.getText());
     fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
     
     panel.add(heading);
    panel.add(Box.createVerticalStrut(12));
    panel.add(chooseButton);
    panel.add(Box.createVerticalStrut(12));
    panel.add(fileLabel);
 
       
       // new DropTarget(panel, new VideoDropListener());
     
    return panel;
    }
    
    
    
    private void openFile(){
        JFileChooser jf = new JFileChooser();
        jf.setFileFilter(new FileNameExtensionFilter("Video files", ALLOWED_EXTENSIONS));
        int result = jf.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            acceptFile(jf.getSelectedFile());
        }
    }
    
    
    
    private void acceptFile(File file){
        String name = file.getName().toLowerCase();
        boolean valid = false;
        
        for(String extension : ALLOWED_EXTENSIONS){
            if(name.endsWith( "." + extension)){
                valid = true;
                break;
            }
        }
        
        if(!valid){
            JOptionPane.showConfirmDialog(this, "Unsupported format", "ERROR", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        selectedFile = file;
        
        double sizeMb = file.length() / (1024 * 1024);//1 MB = 1024 KB,1 GB = 1024 MB -> returns in bytes convert to mb
        fileLabel.setText(String.format("%s (%.1f MB)", file.getName(), sizeMb));
        
    }
    
    

    public static void main(String[] args) {
        new TopPage();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae){
        
    }
}
