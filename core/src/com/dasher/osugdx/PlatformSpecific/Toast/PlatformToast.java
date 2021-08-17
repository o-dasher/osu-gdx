package com.dasher.osugdx.PlatformSpecific.Toast;

public interface PlatformToast {
    default void log(String text) {
        System.out.println(text);
    }
}
