package com.infy.utility;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
//import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;


public class APIUtility {

	private static final Logger logger = LoggerFactory.getLogger(APIUtility.class);
	
	public static String sendGetRequestAndFetchKey(String baseUrl, String endpoint, String accountId, String key) {
		// Build the URL dynamically by adding the accountID
		String url = baseUrl + endpoint + accountId;
		System.out.println(url);
		// Send the GET request and get the response
		Response response = given().header("Accept", "application/json") // Setting the Accept header to application/json
				.header("Content-Type", "application/json") // Setting the Content-Type header to application/json
				.when().get(url) // Sending the GET request to the dynamic URL
				.then().statusCode(200) // Ensure the status code is 200
				.extract().response(); // Extract the response

	    System.out.println("Response: ");
	    response.prettyPrint();

	    // Fetch the data for the specific key from the JSON response
	    String value = response.jsonPath().getString(key);
	    return value; 
	    
	}
	
	
	// Method for sending GET requests
    public JSONObject sendGetRequest(String endpoint, Map<String, String> headers) throws IOException, URISyntaxException {
        HttpURLConnection connection = (HttpURLConnection) new URI(endpoint).toURL().openConnection();
        connection.setRequestMethod("GET");

        // Set headers if any
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return handleResponse(connection);
    }

    // Method for sending POST requests
    public JSONObject sendPostRequest(String endpoint, JSONObject payload, Map<String, String> headers) throws IOException, URISyntaxException {
        HttpURLConnection connection = (HttpURLConnection) new URI(endpoint).toURL().openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        // Set headers if any
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        // Write payload
        try (var os = connection.getOutputStream()) {
            byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return handleResponse(connection);
    }

    // Common method to handle responses
    private JSONObject handleResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        logger.info("Response Code: {}", responseCode);

        try (InputStream inputStream = responseCode < 400 ? connection.getInputStream() : connection.getErrorStream()) {
            String response = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return new JSONObject(response);
        } finally {
            connection.disconnect();
        }
    }
	
	
	public static void sendPostRequestAndVerifyResponse(String baseUrl, String accountId, String amount) {
        // Build the URL for deposit
        String url = baseUrl + "/services/bank/deposit";

        // Expected success message based on the provided accountID and amount
        String expectedMessage = "Successfully deposited $" + amount + " to account #" + accountId;

        // Send the POST request with accountId and amount as query parameters
        Response response = given()
                .header("Accept", "application/json") // Setting the Accept header
                .header("Content-Type", "application/json") // Setting the Content-Type header
                .queryParam("accountId", accountId) // Adding accountId as a query parameter
                .queryParam("amount", amount) // Adding amount as a query parameter
                .when()
                .post(url) // Sending POST request
                .then()
                .statusCode(200) // Ensure the status code is 200 (OK)
                .body(containsString(expectedMessage)) // Verifying the response contains the expected message
                .extract()
                .response(); // Extracting the response
        
        // Print the full response for logging or debugging purposes
        System.out.println("Response: " + response.asString());
    }	
	
	
	 // Function to make a GET request with baseURL and endpoint
    public static String sendGETRequest(String baseURL, String endpoint) {
        // Set RestAssured base URL
        RestAssured.baseURI = baseURL;

        // Perform GET request with the Content-Type set to application/json
        Response response = RestAssured
                                .given()
                                .header("Content-Type", "application/json")  // Set Content-Type to application/json
                                .when()
                                .get(endpoint);  // Perform GET request to the endpoint

        // Check if the response is successful (HTTP status 200)
        if (response.getStatusCode() == 200) {
            return response.getBody().asString(); // Return the response body as a string
        } else {
            return "GET request failed. Response code: " + response.getStatusCode();
        }
    }
    
    public static Response getRequest(String url, Map<String, String> headers, Map<String, String> queryParams,
    	      ContentType contentType) {
    	    return given()
    	    		.relaxedHTTPSValidation()
    	    		.urlEncodingEnabled(false)
    	    		.headers(headers)
    	    		.queryParams(queryParams)
    	    		.contentType(contentType)
    	        .when()
    	        	.log().all().get(url)
    	        .then()
    	        	.log().all().extract().response();
    	  }
}
