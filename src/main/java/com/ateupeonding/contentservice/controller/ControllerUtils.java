package com.ateupeonding.contentservice.controller;

import java.util.UUID;

public class ControllerUtils {

    public static UUID parseIdToUuid(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException();
        }
    }

}
