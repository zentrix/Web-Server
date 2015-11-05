/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package webserver;

import java.util.*;

/**
 * Clase de utilidades donde declaramos los tipos MIME y algunos gestores de 
 * los errores que se pueden generar en HTML.
 * @author carlos
 */
public class ManejodeRespuestas {
    final static String version = "1.0";
    final static String mime_text_plain = "text/plain";
    final static String mime_text_html = "text/html";
    final static String mine_text_css = "text/css";
    final static String mine_text_javascript = "text/javascript";
    final static String mime_image_gif = "image/gif";
    final static String mime_image_jpg = "image/jpg";
    final static String mime_app_os = "application/octet-stream";
    final static String CRLF = "\r\n";

    /** Método que convierte un objeto String en una matriz de byte. */
    public static byte aBytes( String s )[] {
        byte b[] = new byte[ s.length() ];
        
        s.getBytes( 0 , b.length, b , 0);
        
        return(b);
    }


    /** Este método concatena dos matrices de bytes. */
    public static byte concatenarBytes( byte a[],byte b[] )[] {
        byte ret[] = new byte[ a.length + b.length ];
        
        System.arraycopy( a,0,ret,0,a.length );
        System.arraycopy( b,0,ret,a.length,b.length );
        
        return( ret );
    }


    /** Este método toma un tipo de contenido y una longitud, para
      devolver la matriz de bytes que contiene el mensaje de cabecera
      MIME con formato. */
    public static byte cabMime( String ct, int tam )[] {
        return( cabMime( 200,"OK",ct,tam ) );
    }


    /** Es el mismo método anterior, pero permite un ajuste más fino del código
     * que se devuelve y el mensaje de error de HTTP. */
    public static byte cabMime(int codigo, String mensaje, String ct, int size )[]
    {
        Date d = new Date();
        
        return( aBytes( "HTTP/1.0 " + codigo + " " + mensaje + CRLF +
               "Date: " + d.toGMTString() + CRLF +
               "Server: Java/" + version + CRLF +
               "Content-type: " + ct + CRLF +
               ( size > 0 ? "Content-length: "+ size +CRLF : "" )+CRLF ) );
    }


    /** Este método construye un mensaje HTML con un formato decente
     * para presentar una condición de error y lo devuelve como
     * matriz de bytes. */
    public static byte error( int codigo, String msg, String fname)[] {
        ServerLog.error(msg);
        
        String ret = "<BODY>"+CRLF+"<H1>"+codigo +" "+msg+"</H1>"+CRLF;

        if( fname != null ) 
            ret += "Error al buscar el URL: "+fname+CRLF;

        ret += "</BODY>"+CRLF;

        byte tmp[] = cabMime( codigo,msg,mime_text_html,0 );

        return( concatenarBytes( tmp,aBytes( ret ) ) );
    }


    /** Devuelve el tipo MIME que corresponde a un nombre de archivo dado. */
    public static String solicitudDeArchivo( String fichero ) {
        
        String tipo;

        if( fichero.endsWith( ".html" ) || fichero.endsWith( ".htm" ) )
            tipo = mime_text_html;
        else if (fichero.endsWith(".css"))
            tipo = mine_text_css;
        else if (fichero.endsWith(".js"))
            tipo = mine_text_javascript;
        else if( fichero.endsWith( ".class" ) )
            tipo = mime_app_os;
        else if( fichero.endsWith( ".gif" ) )
            tipo = mime_image_gif;
        else if( fichero.endsWith( ".jpg" ) )
            tipo = mime_image_jpg;
        else
            tipo = mime_text_plain;

        return( tipo );
    }
}

