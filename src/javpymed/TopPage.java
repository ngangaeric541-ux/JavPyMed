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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.kordamp.ikonli.swing.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Dimension;

/**
 *
 * @author mashmash
 */
public class TopPage extends JFrame implements ActionListener{
    
    // =========================================================
    //                  COMPONENTS
    // =========================================================
    
    
    private static final String[] ALLOWED_EXTENSIONS = {"mp4", "avi", "mkv", "mov", "3gp", "wmv", "flv"};
    private File selectedFile;   
    private JLabel fileLabel; 
    private JButton chooseButton;
    private JComboBox<String> deviceDropdown;
    private JComboBox<String> saveLocationDropdown;
    static JButton convertButton;
    private JButton cancelButton;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JButton themeToggleButton;
    private boolean darkMode = false;
    
    
    // =========================================================
    //                  CONSTRUCTOR
    // =========================================================
    
    TopPage(){
        FrontPanel();    
    }
    
    private void FrontPanel(){
        setSize(1280,800);
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
         //add next panels,clean,scaling
         JPanel top = new JPanel();
            top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
            top.add(BuildHeaderPanel());
            top.add(BuildOptionsPanel());

            front.add(top, BorderLayout.NORTH);
            front.add(BuildUI(), BorderLayout.CENTER);
            front.add(BuildBottomPanel(), BorderLayout.SOUTH);
        setContentPane(front);
        setVisible(true);
    }
    
    
    
    // =========================================================
    //                  PANELS
    // =========================================================
    
        private JPanel BuildHeaderPanel(){
            JPanel panel = new JPanel(new BorderLayout());
            JLabel title = new JLabel("  Video Converter");

                    themeToggleButton = new JButton("  Dark Mode");
                    themeToggleButton.setIcon( FontIcon.of(FontAwesomeSolid.MOON, 20));
                    themeToggleButton.addActionListener(new ActionListener(){
                                @Override
                                public void actionPerformed(ActionEvent ae){
                                    ToggleTheme();
                                }
                            });

            panel.add(title, BorderLayout.WEST);
            panel.add(themeToggleButton, BorderLayout.EAST);
            return panel;
        }

    private JPanel BuildUI(){
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createDashedBorder(Color.GRAY),BorderFactory.createEmptyBorder(30, 20, 30, 20)));
    
        JLabel heading = new JLabel("  Drag video or click + button below : ");
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

            chooseButton = new JButton("Choose file");
            StyleButton(chooseButton);
            chooseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            chooseButton.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        openFile();
                    }
                });
     
                    fileLabel = new JLabel("No file chosen");
                    fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
     
    panel.add(heading);
    panel.add(Box.createVerticalStrut(12));
    panel.add(chooseButton);
    panel.add(Box.createVerticalStrut(12));
    panel.add(fileLabel);
 

             //VIDEODROPLISTENER LOGIC
       new DropTarget(panel, new VideoDropListener(file -> acceptFile(file)));
     
    return panel;
    }
    
    private JPanel BuildOptionsPanel(){
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            JLabel deviceLabel = new JLabel("Convert to:");
            deviceDropdown = new JComboBox<>(new String[] {"iPod Video","iPod Classic","Early Android Phone","High Resolution(1080p)","QVGA 320x240"});

                    JLabel saveLabel = new JLabel("Save to:");
                    saveLocationDropdown = new JComboBox<>(new String[] {"Same folder", "Choose folder..."});

    panel.add(deviceLabel);
    panel.add(deviceDropdown);
    panel.add(Box.createVerticalStrut(10));
    panel.add(saveLabel);
    panel.add(saveLocationDropdown);

    return panel;
}
    
private JPanel BuildActionPanel(){
    JPanel panel = new JPanel();
    
    convertButton = new JButton("Convert");
    convertButton.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent evt){
            ConvertVideo();
        }
    });
    StyleButton(convertButton);
    
        cancelButton = new JButton("Cancel");
        StyleButton(cancelButton);
    
    panel.add(convertButton);
    panel.add(cancelButton);
    return panel;
}

private JPanel BuildBottomPanel(){
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(BuildActionPanel());
    panel.add(BuildProgressPanel());
    return panel;
}

     // =========================================================
    //                  PROGRESS BAR
   // =========================================================

private JPanel BuildProgressPanel(){
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    
    progressBar = new JProgressBar(0, 100);
    progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
    
            statusLabel = new JLabel("No file selected");
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    panel.add(progressBar);
    panel.add(Box.createVerticalStrut(6));
    panel.add(statusLabel);
    return panel;
}


     // =========================================================
    //                  CHOOSE FILE LOGIC
   // =========================================================

    
    private void openFile(){
        JFileChooser jf = new JFileChooser();
            jf.setFileFilter(new FileNameExtensionFilter("Video files", ALLOWED_EXTENSIONS));
                int result = jf.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                acceptFile(jf.getSelectedFile());
            }
    }
    
    
    // =========================================================
    //                  FILE VALIDATION FOR VIDEO SPECIFIED FILES ONLY!
   // =========================================================
    
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
            JOptionPane.showConfirmDialog(this, "Unsupported format", "ERROR", JOptionPane.CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        selectedFile = file;
        
        double sizeMb = file.length() / (1024 * 1024);//1 MB = 1024 KB,1 GB = 1024 MB -> returns in bytes convert to mb
        fileLabel.setText(String.format("%s (%.1f MB)", file.getName(), sizeMb));
        statusLabel.setText("Ready");
        
    }
    
    // =========================================================
    //                  THEME CHANGING LOGIC
   // =========================================================
    
    private void ToggleTheme(){
        darkMode = !darkMode;
            try {
                if (darkMode) {
                    com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
                    themeToggleButton.setIcon(FontIcon.of(FontAwesomeSolid.SUN, 20));
                } else {
                    com.formdev.flatlaf.themes.FlatMacLightLaf.setup();
                    themeToggleButton.setIcon(FontIcon.of(FontAwesomeSolid.MOON, 20));
                }
                com.formdev.flatlaf.FlatLaf.updateUI();
                themeToggleButton.setText(darkMode ? "  Light Mode" : "  Dark Mode");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Couldn't switch theme.", "Error", JOptionPane.ERROR_MESSAGE);
            }
    }
    
    
    // =========================================================
    //                  BUTTON STYLING METHOD FOR MOST BUTTONS
   // =========================================================
    private void StyleButton(JButton button){
    button.setBackground(new Color(46, 139, 87)); 
    button.setForeground(Color.WHITE);
    button.setOpaque(true);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    }
    
    
    // =========================================================
    //                  LABEL CHANGER FOR DROPPING FROM VIDEODROPLISTENER CLASS
   // =========================================================
    public void setLblText(String Text){
        statusLabel.setText(Text);
    }
    
    // =========================================================
    //                      CONNECTION WITH PYTHON CONVERTION LOGIC
   // =========================================================
    private static final String PYTHON_SCRIPT_PATH ="src/javpymed/python/convert.py";

    // =========================================================
    //                      JAVA CONVERSION HANDLER
   // =========================================================
    private void ConvertVideo(){
        if(selectedFile == null){
            JOptionPane.showMessageDialog(this, "Choose a file first.", "No file", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String profileName = (String) deviceDropdown.getSelectedItem();

        ProcessBuilder builder = new ProcessBuilder(
            "python3", PYTHON_SCRIPT_PATH,
            "--input", selectedFile.getAbsolutePath(),
            "--profile", profileName);
        
        builder.redirectErrorStream(true); 

        convertButton.setEnabled(false);
        statusLabel.setText("Converting...");

        new ConversionWorker(builder, this).execute();
    }
    
    // =========================================================
    //                  RESET UI AND BUTTON
   // =========================================================
    void resetUI(){
        convertButton.setEnabled(true);
        progressBar.setValue(0);
        statusLabel.setText("Ready");
    }

    
    // =========================================================
    //                  PROGRESS BAR LIVE-UPDATING EVERY TIMESTAMP
   // =========================================================
    void handleLine(String line){
        if(line.startsWith("PROGRESS:")){
            int percent = Integer.parseInt(line.substring("PROGRESS:".length()).trim());
            progressBar.setValue(percent);
        } else if(line.startsWith("DONE:")){
            int accept = JOptionPane.showConfirmDialog(TopPage.this,"Conversion complete","SUCCESS",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
                if(accept == JOptionPane.YES_NO_CANCEL_OPTION){
                    return;
                }
                    progressBar.setValue(100);
                        resetUI();
        } else if(line.startsWith("ERROR:")){
            statusLabel.setText("Error");
            int error= JOptionPane.showConfirmDialog(this, line.substring("ERROR:".length()).trim(), "Conversion error", JOptionPane.ERROR_MESSAGE);
            if(error == JOptionPane.YES_NO_CANCEL_OPTION){
                    return;
                }
            resetUI();
        }
    }
    
     

    public static void main(String[] args) {
        FlatMacLightLaf.setup();
        new TopPage();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae){
        
    }
    
    
}
