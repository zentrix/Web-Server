/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.io.*;
import java.net.*;
import static webserver.ManejodePeticiones.*;

/**
 *
 * @author carlos
 */
public class Main {
    
    // Función principal de nuestro servidor, que se conecta al socket
    // y se embucla indefinidamente
    public static void main(String[] args) {    
        try {
            ServerSocket ss = new ServerSocket( Config.puerto );
            while( true ) {
                String peticion;
                try (
                        Socket s = ss.accept();
                        OutputStream sOut = s.getOutputStream();
                        InputStream sIn = s.getInputStream()
                    )
                {
                    if( ( peticion = ManejodePeticiones.getPeticion( sIn ) ) != null ) {
                        switch( tipoPeticion( peticion ) ) {
                            case RT_GET:
                                ctrlGet( peticion,sOut );
                                break;
                            case RT_UNSUP:
                            default:
                                ctrlNoSoportadas( peticion,sOut );
                                break;
                        }
                        ServerLog.peticion( peticion );
                    }
                }
            }
        } catch (IOException ex) {
            ServerLog.error( "Excepcion: " + ex );
        }
    }
}
