package net.camelcodes.keystores;

import static net.camelcodes.keystores.AppPropertiesReader.appProperties;

import java.util.Optional;
import java.util.logging.Logger;

public class Main {

  Logger log = Logger.getLogger(Main.class.getName());

  private static final String API_USERNAME_PROPERTY = "externalapi.username";
  private static final String API_PASSWORD_PROPERTY = "externalapi.password-alias";


  public Main() {

    callExternalApi();

  }

  /**
   * This method is simulating a web service call, in our case will just log a message.
   * <p>
   * The idea is to fetch the password from the key store and log it in the console
   */
  private void callExternalApi() {

    KeyStoreLoader ksLoader = new KeyStoreLoader();

    String apiUsername = appProperties.getProperty(API_USERNAME_PROPERTY);
    Optional<String> apiPassword = ksLoader.getStringKeyEntry(
        appProperties.getProperty(API_PASSWORD_PROPERTY));

    if (apiPassword.isEmpty()) {
      throw new IllegalStateException("Couldn't retreive api password from keystore");
    }

    log.info(String.format("[Mock Call] External api called with username: %s and password: %s",
        apiUsername,
        apiPassword.get()));
  }

  public static void main(String[] args) {
    new Main();
  }


}