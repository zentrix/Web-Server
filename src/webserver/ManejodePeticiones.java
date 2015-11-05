/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import java.io.*;

/**
 *
 * @author carlos
 */
public class ManejodePeticiones {
    
    public static final int RT_GET=1;
    public static final int RT_UNSUP=2;
    public static final int RT_END=4;
    
    /** Indica que la petición no está soportada, por ejemplo POST y HEAD. */
    public static void ctrlNoSoportadas(String peticion, OutputStream sOutput) {
        ServerLog.error( "Peticion no soportada: "+ peticion );
    }


    /** Este método analiza gramaticalmente la solicitud enviada con el
      GET y la descompone en sus partes para extraer el nombre del
      archivo que se está solicitando, Entonces lee el fichero que se pide. */
    public static void ctrlGet( String peticion,OutputStream sOutput ) {
        int fsp = peticion.indexOf( ' ' );
        int nsp = peticion.indexOf( ' ',fsp+1 );
        String fich = peticion.substring( fsp+1,nsp );

        fich = Config.docRaiz+fich+( fich.endsWith("/") ? Config.fichIndice : "" );
        try {
            File f = new File( fich );
            if( !f.exists() ) {
                sOutput.write( CtrlErrores.error( 404,
                    "No Encontrado",fich ) );
                return;
            }

            if( !f.canRead() ) {
                sOutput.write( CtrlErrores.error( 404,
                    "Permiso Denegado",fich ) );
                return;
            }

            try ( // Ahora lee el fichero que se ha solicitado
                InputStream sInput = new FileInputStream( f )) {
                String cabmime = ManejodeRespuestas.solicitudDeArchivo( fich );
                int n = sInput.available();
                
                sOutput.write( ManejodeRespuestas.cabMime( cabmime,n ) );
                
                byte buf[] = new byte[Config.buffer];
                while( ( n = sInput.read( buf ) ) >= 0 )
                    sOutput.write( buf,0,n );
            }
        } catch( IOException e ) {
            ServerLog.error( "Excepcion: " + e );
        }
    }


    /** Devuelve la cabecera de la solicitud completa del cliente al
      método main de nuestro servidor. */
    public static String getPeticion( InputStream sInput ) {
        try {
            byte buf[] = new byte[Config.buffer];
            boolean esCR = false;
            int pos = 0;
            int c;

            while( ( c = sInput.read() ) != -1 ) {
                switch( c ) {
                    case '\r':
                        break;
                    case '\n': 
                        if( esCR )
                            return( new String( buf, 0, 0, pos ) );
                        esCR = true;
                    // Continúa, se ha puesto el primer \n en la cadena
                    default:
                        if( c != '\n' ) 
                            esCR = false;
                        buf[pos++] = (byte)c;
                    }
                }
        } catch( IOException e ) {
            ServerLog.error( "Error de Recepcion" );
        }

        return( null );
    }

    /**
     *  Sirve para conocer el tipo de peticion.
     */
    public static int tipoPeticion( String peticion ) {
        return( peticion.regionMatches( true,0,"get ",0,4 ) ? 
            RT_GET : RT_UNSUP );
    }

}
