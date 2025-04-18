package com.example.odyssea.exceptions;

public class JwtTokenMalformedException extends RuntimeException {
    public JwtTokenMalformedException(String message) {
        super(message);
    }
}

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(String message) {
        super(message);
    }
}

public class JwtTokenUnsupportedException extends RuntimeException {
    public JwtTokenUnsupportedException(String message) {
        super(message);
    }
}

public class JwtTokenSignatureException extends RuntimeException {
    public JwtTokenSignatureException(String message) {
        super(message);
    }
}

public class JwtTokenMissingException extends RuntimeException {
    public JwtTokenMissingException(String message) {
        super(message);
    }
}
