package com.springboot.webflux.products;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import reactor.core.publisher.Mono;

/**
 *
 * @author Mario Ruiz Rojo
 * Application function tools
 */
public class Utils {
    /**
     * It returns an empty generic list
     * @return empty list
     */
    public static List emptyList(){
        return new ArrayList<>();
    }

    /**
     * Mono<Void>
     * @return stream of type void
     */
    public static Mono<Void> monoVoid(){
        return Mono.just("").then();
    }

    /**
     * It returns the name formatted to fit the file system format
     * @param formatLessName
     * @return formattedFileName
     */
    private static String fileFormatName(String formatLessName) {
        return formatLessName.replace(" ", "-") //no spaces
                .replace(":", "")//no colon
                .replace("\\", "");//no backslash
    }

    /**
     * Generates random id and concatenates it to the file name
     * @param notUniqueName
     * @return unique file name
     */
    public static String generateUniqueName(String notUniqueName) {
        return UUID.randomUUID()+"-"+fileFormatName(notUniqueName);
    }
}
