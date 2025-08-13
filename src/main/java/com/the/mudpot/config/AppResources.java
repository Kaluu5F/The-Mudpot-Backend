package com.the.mudpot.config;

import org.apache.commons.codec.Resources;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class AppResources {

    public static Optional<String> asString(String resource) {
        String string = null;
        try {
            string = IOUtils.toString(Resources.getInputStream(resource), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(String.format("file retrieving error for resource = %s", resource));
        }

        return Optional.of(string);
    }

}
