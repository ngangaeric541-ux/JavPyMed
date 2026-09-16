/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javpymed;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * @author mashmash
 */

     // =========================================================
    //                  CLASS RESPONSIBLE FOR SLOW BUT STABLE UPDATES TO UI
   //                   THE UI MIGHT LOCK WITHOUT!!!!
  // =========================================================
public class ConversionWorker extends SwingWorker<Void, String> {

    private final ProcessBuilder builder;
    private final TopPage top;

    public ConversionWorker(ProcessBuilder builder, TopPage top) {
        this.builder = builder;
        this.top = top;
    }

    @Override
    protected Void doInBackground() throws Exception {
        Process process = builder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                publish(line);
            }
        }
        process.waitFor();
        return null;
    }

    @Override
    protected void process(List<String> lines) {
        for (String line : lines) {
            top.handleLine(line);
        }
    }

}
