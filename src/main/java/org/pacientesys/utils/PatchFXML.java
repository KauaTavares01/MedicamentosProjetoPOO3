package org.pacientesys.utils;


import java.nio.file.Paths;

public class PatchFXML {
    public static String pathBase() {
        return Paths.get("src/main/java/org/pacientesys/view").toAbsolutePath().toString();
    }
}