package com.megatransact.exception;

public class CustomExceptions {
    public static class UserNotFound extends RuntimeException  {
        public UserNotFound(String message) {
            super("User does not exist " + message);
        }
    }

    public static class Unauthorized extends RuntimeException {
        public Unauthorized(String message) {
            super("Unauthorized " + message);
        }
    }

    public static class InvalidArgument extends RuntimeException {
        public InvalidArgument(String message) {
            super("Invalid argument "+ message);
        }
    }
}
