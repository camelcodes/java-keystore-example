package net.camelcodes.keystores;

import static net.camelcodes.keystores.AppPropertiesReader.appProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyStoreLoader {

  private static final String KS_TYPE_PROPERTY = "keystore.type";
  private static final String KS_PATH_PROPERTY = "keystore.path";
  private static final String KS_PASSWORD_PROPERTY = "keystore.password";
  Logger log = Logger.getLogger(KeyStoreLoader.class.getName());
  private KeyStore keyStore;
  private char[] keyStorePassword;

  KeyStoreLoader() {
    loadKeyStore();
  }

  private void loadKeyStore() {
    try {
      this.keyStorePassword = appProperties.getProperty(KS_PASSWORD_PROPERTY).toCharArray();

      String keyStorePath = appProperties.getProperty(KS_PATH_PROPERTY);
      String keyStoreType = appProperties.getProperty(KS_TYPE_PROPERTY);

      keyStore = KeyStore.getInstance(keyStoreType);
      keyStore.load(new FileInputStream(keyStorePath), keyStorePassword);
    } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
      throw new IllegalStateException("Unable to load keystore", e);
    }
  }


  public KeyStore getKeyStore() {
    return keyStore;
  }

  public Optional<String> getStringKeyEntry(String alias) {
    Optional<String> password = Optional.empty();
    try {
      if (!keyStore.containsAlias(alias)) {
        log.log(Level.WARNING, "Keystore doesn't contain alias: {}", alias);
        return password;
      }
      Key apiPassword = keyStore.getKey(alias, keyStorePassword);
      password = Optional.of(new String(apiPassword.getEncoded()));
    } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
      log.log(Level.SEVERE, "Unable to get key entry", e);
    }
    return password;
  }
}
