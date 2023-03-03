package com.runners.exception.message;

public class ErrorMessage {

    public final static String RESOURCE_NOT_FOUND_EXCEPTION = "Resource with id %s not found";
    public final static String USER_NOT_FOUND_EXCEPTION = "Resource with email %s not found";
    public final static String USER_ID_NOT_FOUND_EXCEPTION = "User with id %s not found";
    public final static String JWT_TOKEN_ERROR_MESSAGE = "JWT Token Validation Error: %s";
    public final static String EMAIL_ALREADY_ERROR_MESSAGE = "Email already exist: %s";
    public static final String ROLE_NOT_FOUND_EXCEPTION = "Role with id %s not found";
    public final static String CURRENT_PRINCIPAL_NOT_FOUND_EXCEPTION = "Current user not found";
    public static final String NOT_PERMITTED_METHOD_MESSAGE = "You don't have any permission to change this data";
    public static final String PASSWORD_NOT_MATCHED_MESSAGE = "Your passwords are not matched";
}
