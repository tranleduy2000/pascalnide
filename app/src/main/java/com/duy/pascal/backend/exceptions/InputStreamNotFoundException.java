package com.duy.pascal.backend.exceptions;

public class InputStreamNotFoundException extends Exception {
        public InputStreamNotFoundException() {
            super();
        }

        public InputStreamNotFoundException(String message) {
            super(message);
        }

        public InputStreamNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public InputStreamNotFoundException(Throwable cause) {
            super(cause);
        }
    }
