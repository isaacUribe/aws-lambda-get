package dev.pragma.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import dev.pragma.model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

public class UserLambdaHandler implements RequestStreamHandler {
    private String DYNAMO_TABLE = "users";
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONParser parser = new JSONParser();
        JSONObject responseObject = new JSONObject();
        JSONObject responseBody = new JSONObject();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDB = new DynamoDB(client);
        int id;
        Item resItem = null;
        try {
            JSONObject reqObject = (JSONObject) parser.parse(reader);
            if (reqObject.get("pathParameters")!=null){
                JSONObject pps = (JSONObject) reqObject.get("pathParameters");
                if (pps.get("id") != null){
                    id = Integer.parseInt((String)pps.get("id"));
                    resItem = dynamoDB.getTable(DYNAMO_TABLE).getItem("id",id);
                }
            }
            if (resItem!= null){
                User user = new User(resItem.toJSON());
                responseBody.put("user", user);
                responseObject.put("statusCode", 200);
            }else {
                responseBody.put("message", "No Items Found");
                responseObject.put("statusCode", 404);
            }
            responseObject.put("body", responseBody.toString());
        }catch (Exception e){
            context.getLogger().log("Error: " + e.getMessage());
        }
        writer.write(responseObject.toString());
        reader.close();
        writer.close();
    }
}
