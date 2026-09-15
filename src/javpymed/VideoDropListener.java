package javpymed;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;

public class VideoDropListener extends DropTargetAdapter {

    // =========================================================
    //                  COMPONENTS
    // =========================================================
    private final FileDropHandler handler;
    private TopPage top;
   
    
    public VideoDropListener(FileDropHandler handler) {
        this.handler = handler;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void drop(DropTargetDropEvent event) {
        event.acceptDrop(DnDConstants.ACTION_COPY);
        try {
            Transferable transferable = event.getTransferable();
            List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
            if (!files.isEmpty()) {
                handler.onFileDropped(files.get(0));
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Couldn't read the dropped file.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            event.dropComplete(true);
        }
    }
    
    public void textController(TopPage tp){
        this.top = tp;
    }
}