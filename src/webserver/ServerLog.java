/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.io.IOException;
import java.util.logging.*;

/**
 *
 * @author carlos
 */
public class ServerLog {
    private FileHandler handler = null;
    public static Logger logger = Logger.getLogger("WebServer.log");

    public static void error( String entrada ) {
       logger.log(Level.SEVERE, entrada);
        System.out.println( "Error: "+entrada );

//        BufferedWriter bufferW = new BufferedWriter(fileW);
//        PrintWriter printW =  new PrintWriter(bufferW);
//
//        printW.println("Error: " + entrada);
//        printW.close();
    }

    public static void peticion( String peticion ) {
        System.out.println( peticion );
        logger.log(Level.ALL, peticion);

    }
    
    public ServerLog(String pattern) {   
        try {
            handler = new FileHandler(pattern, 1000, 2);
            logger.addHandler(handler);
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(ServerLog.class.getName()).log(Level.SEVERE, null, ex);
        }
   
  }

  public void logMessage() {
    LogRecord record = new LogRecord(Level.INFO, "Error en: ");
    logger.log(record);
    handler.flush();
    handler.close();
  }
}
