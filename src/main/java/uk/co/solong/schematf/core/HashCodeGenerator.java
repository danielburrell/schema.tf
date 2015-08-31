package uk.co.solong.schematf.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.databind.JsonNode;

public class HashCodeGenerator {
    public String getHashCode(JsonNode schema) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(schema.toString().getBytes());
            return DatatypeConverter.printHexBinary(thedigest);
            // return new String(thedigest, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
}
