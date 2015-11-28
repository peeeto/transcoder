/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.petercho;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

/**
 * @author turkish
 */
public class Encoder {
    public static final String DEFAULT_ENCODING = "UTF-8";

    private Encoder() {
    }

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
            return URLDecoder.decode(str, DEFAULT_ENCODING);
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
            return URLEncoder.encode(str, DEFAULT_ENCODING);
        } catch (Exception e) {
            return "Encoding error";
        }
    }

    public static long checksumAdler32(String text) {
        Adler32 crc = new Adler32();
        try (InputStream is = new ByteArrayInputStream(text.getBytes(DEFAULT_ENCODING))) {
            checksum(is, crc);
            return crc.getValue();
        } catch (IOException e) {
            throw new IllegalStateException("Error getting checksum, e:" + e, e);
        }
    }

    public static long checksumCRC32(String text) {
        CRC32 crc = new CRC32();
        try (InputStream is = new ByteArrayInputStream(text.getBytes(DEFAULT_ENCODING))) {
            checksum(is, crc);
        } catch (IOException e) {
            throw new IllegalStateException("Error getting checksum, e:" + e, e);
        }
        return crc.getValue();
    }

    private static Checksum checksum(InputStream is, Checksum checksum) throws IOException {
        InputStream in = new CheckedInputStream(is, checksum);
        IOUtils.copy(in, new NullOutputStream());
        return checksum;
    }

    public static byte[] getHmac(String secretKey, String payload, String hmacType) {
        final Mac mac;
        byte[] hmac;
        try {
            final byte[] secretKeyBytes;
            if (secretKey == null) {
                secretKeyBytes = new byte[]{0};
            } else {
                secretKeyBytes = secretKey.getBytes(DEFAULT_ENCODING);
            }
            if (payload == null) {
                payload = "";
            }
            SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, hmacType);
            mac = Mac.getInstance(hmacType);
            mac.init(keySpec);
            hmac = mac.doFinal(payload.getBytes(DEFAULT_ENCODING));
        } catch (NoSuchAlgorithmException e) {
            String msg = "An error occurred initializing algorithm '" + hmacType + "', e: " + e;
            throw new IllegalStateException(msg, e);
        } catch (InvalidKeyException e) {
            String msg = "An error occurred initializing key, e: " + e;
            throw new IllegalStateException(msg, e);
        } catch (UnsupportedEncodingException e) {
            String msg = "Invalid encoding, e: " + e;
            throw new IllegalStateException(msg, e);
        }
        return hmac;
    }

    public static String formatXml(String xml) throws TransformerException {
        Source xmlInput = new StreamSource(new StringReader(xml));
        StringWriter stringWriter = new StringWriter();
        StreamResult xmlOutput = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 4);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(xmlInput, xmlOutput);
        return xmlOutput.getWriter().toString();
    }

    public static String hmacHex(String pass, String selectedText, String alg) {
        return Hex.encodeHexString(getHmac(pass, selectedText, alg));
    }

    public static String formatJson(String text) {
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization().setExclusionStrategies().create();
        JsonElement el = parser.parse(text);
        return gson.toJson(el);
    }
}
