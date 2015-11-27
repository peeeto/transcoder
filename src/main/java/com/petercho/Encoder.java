/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.petercho;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 * @author turkish
 */
public class Encoder {

    /**
     * Description of the Method
     *
     * @param str Description of the Parameter
     * @return Description of the Return Value
     */
    /*
    public static String unicodeDecode( String str ) {
    // FIXME: TOTALLY EXPERIMENTAL
    try {
    ByteBuffer bbuf = ByteBuffer.allocate( str.length() );
    bbuf.put( str.getBytes() );
    Charset charset = Charset.forName( "ISO-8859-1" );
    CharsetDecoder decoder = charset.newDecoder();
    CharBuffer cbuf = decoder.decode( bbuf );
    return ( cbuf.toString() );
    }
    catch ( Exception e ) {
    return ( "Encoding problem" );
    }
    }
     */
    /**
     * Description of the Method
     *
     * @param str Description of the Parameter
     * @return Description of the Return Value
     */
    /*
    public static String unicodeEncode( String str ) {
    // FIXME: TOTALLY EXPERIMENTAL
    try {
    Charset charset = Charset.forName( "ISO-8859-1" );
    CharsetEncoder encoder = charset.newEncoder();
    ByteBuffer bbuf = encoder.encode( CharBuffer.wrap( str ) );
    return ( new String( bbuf.array() ) );
    }
    catch ( Exception e ) {
    return ( "Encoding problem" );
    }
    }
     */
    /**
     * Description of the Method
     *
     * @param str Description of the Parameter
     * @return Description of the Return Value
     */
    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, TranscoderFrame.DEFAULT_ENCODING);
        } catch (Exception e) {
            return "Decoding error";
        }
    }

    /**
     * Description of the Method
     *
     * @param str Description of the Parameter
     * @return Description of the Return Value
     * //TODO curl -G -v -s --data-urlencode query@1.json 'https://www.googleapis.com/freebase/v1/mqlread'
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, TranscoderFrame.DEFAULT_ENCODING);
        } catch (Exception e) {
            return "Encoding error";
        }
    }
    
}
