package com.megatransact.exception;

public class CustomExceptions {
    public static class UserNotFound extends RuntimeException  {
        public UserNotFound(String message) {
            super("User does not exist " + message);
        }
    }

    public static class UnAuthorized extends RuntimeException {
        public UnAuthorized(String message) {
            super("Un authorized " + message);
        }
    }

    public static class InvalidArgument extends RuntimeException {
        public InvalidArgument(String message) {
            super("In valid argument "+ message);
        }
    }
}
