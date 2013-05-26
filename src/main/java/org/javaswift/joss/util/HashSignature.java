package org.javaswift.joss.util;

import org.apache.commons.codec.binary.Hex;
import org.javaswift.joss.exception.CommandException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HashSignature {

    public static String getSignature(String key, String plainText) {
        try {
            return getHmacMD5(key, plainText);
        } catch (Exception err) {
            throw new CommandException("Unable to sign the hash", err);
        }
    }

    public static String getHmacMD5(String key, String input) throws Exception{
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        byte[] hashBytes = mac.doFinal(input.getBytes());
        return Hex.encodeHexString(hashBytes);
    }

}
