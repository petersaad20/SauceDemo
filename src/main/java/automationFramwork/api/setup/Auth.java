package automationFramwork.api.setup;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;

public abstract class Auth {

    public static String token = "";
    public static Cookie cookie;
    public static String requestId;

    public static String getToken(String username, String password) throws Exception {

        RestAssured.useRelaxedHTTPSValidation();
        Response tokenResponse = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("username", username)
                .formParam("password", password)
                .post("tokenUrl")
                .andReturn();

        if (tokenResponse.statusCode() == 200) {
            token = tokenResponse.getBody().jsonPath().getJsonObject("access_token");
        } else {
            throw new Exception("Unable to retrieve token");
        }

        return token;
    }




}


