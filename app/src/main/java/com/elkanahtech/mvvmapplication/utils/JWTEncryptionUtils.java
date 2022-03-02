package com.elkanahtech.mvvmapplication.utils;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.PasswordBasedDecrypter;
import com.nimbusds.jose.crypto.PasswordBasedEncrypter;
import com.nimbusds.jwt.EncryptedJWT;

public class JWTEncryptionUtils {
   public static String encrypt(String payloadBody, String terminalID, String encryptedKey){
      try {
         byte[] sharedKey = encryptedKey.getBytes();
         JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.PBES2_HS256_A128KW, EncryptionMethod.A256CBC_HS512).
                 contentType("text/plain").
                 customParam("AppID", terminalID).
                 build();
         JWEObject jweObject = new JWEObject(jweHeader, new Payload(payloadBody));
         jweObject.encrypt(new PasswordBasedEncrypter(sharedKey,sharedKey.length,1000));
         return jweObject.serialize();
      }catch (Exception e){
         return null;
      }
   }

   public static String decrypt(String encryptedStr, String encryptedAppID){
      try {
         EncryptedJWT encryptedJWT = EncryptedJWT.parse(encryptedStr);
         encryptedJWT.decrypt(new PasswordBasedDecrypter(encryptedAppID));
         Payload payload = encryptedJWT.getPayload();
         return payload.toString();
      }catch (Exception e){
         return null;
      }
   }
}
