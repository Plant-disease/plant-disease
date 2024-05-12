package org.example.plantdisease.utils;

public interface RestConstants {

    String BASE_PATH_V1 = "/api/v1";

    String AUTHORIZATION_HEADER = "Authorization";
    String LANGUAGE_HEADER = "Language";
    String INITIAL_EXECUTING_QUERY = "";
    String TOKEN_TYPE = "Bearer ";
    String PHONE_NUMBER_REGEX = "998[0-9]{9}";

    String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=])(?=\\S+$).{8,}$";
    String PASSWORD_SIMPLE_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
    String GET_ATTACHMENT_KEY = "Secret";
    String SECRET_KEY_FOR_ATTACHMENT = "eyJhbGciOiJIUzI1NiJ9.e30.ZRrHA1JJJW8opsbCGfG_HACGpVUMN_a9IV7pAx";
    Integer CONFLICT = 3009;
    Integer ACCESS_DENIED = 3004;
    Integer NOT_FOUND = 3005;
    Integer SERVER_ERROR = 3008;
    Integer NO_ITEMS_FOUND = 3011;
    Integer REQUIRED = 400;
    int USER_NOT_ACTIVE = 3013;
    int INCORRECT_USERNAME_OR_PASSWORD = 3001;
    String REQUEST_ATTRIBUTE_CURRENT_USER = "CurrentUser";
    String REQUEST_ATTRIBUTE_CURRENT_USER_ID = "CurrentUserId";
    String ADMIN_PHONE_NUMBER = "998887040207";
    String ADMIN_EMAIL = "nomozov.doniyor02@gmail.com";
    String[] OPEN_PAGES_FOR_ALL_METHOD =
            {
//                    "/**",
                    BASE_PATH_V1 + "/auth/**",
                    BASE_PATH_V1 + "/home",
                    BASE_PATH_V1 + "/attachment/upload",
                    BASE_PATH_V1 + "/attachment/system/upload",
                    BASE_PATH_V1 + "/attachment/system/upload/all",
                    BASE_PATH_V1 + "/attachment/download/{id}",
                    BASE_PATH_V1 + "/application/download-excel",

                    BASE_PATH_V1 + "/swagger-ui.html*",
                    "/swagger-ui/**",
                    "/index.html",
                    "/v3/api-docs/**"

            };
}
