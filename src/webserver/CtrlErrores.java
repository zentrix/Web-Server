/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import static webserver.ManejodeRespuestas.*;

/**
 *
 * @author carlos
 */
public class CtrlErrores {
    
    // Este método construye un mensaje HTML con un formato decente
    // para presentar una condición de error y lo devuelve como
    // matriz de bytes
    public static byte error( int codigo, String msg, String fname)[] {
        String ret = "<BODY>"+CRLF+"<H1>"+codigo +" "+msg+"</H1>"+CRLF;

        if( fname != null ) 
            ret += "Error al buscar el URL: "+fname+CRLF;

        ret += "</BODY>"+CRLF;

        byte tmp[] = cabMime( codigo,msg,mime_text_html,0 );

        return( concatenarBytes( tmp,aBytes( ret ) ) );
    }
}
