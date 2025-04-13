package com.cs301.communication_service.services;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class S3Service {

    private final AWSLambda lambdaClient;
    private final ObjectMapper objectMapper;
    private final String functionName; // Set this to your deployed Lambda function name

    public S3Service() {
        // Initialize AWS Lambda client; you can configure region via system/environment variables or builder methods.
        this.lambdaClient = AWSLambdaClientBuilder.standard()
                // Optionally: .withRegion("ap-southeast-1")
                .build();
        this.objectMapper = new ObjectMapper();
        // Ideally, this should be injected via configuration.
        this.functionName = "s3-presigned-url"; 
    }
    
    /**
     * Invokes the Lambda function to get a presigned URL for an upload.
     *
     * @param fileName the file name for the upload; if null or empty, Lambda will generate a default name.
     * @return the presigned URL as a String
     */
    public String getPresignedUrl(String fileName) {
        try {
            // Build an event payload for the Lambda function.
            Map<String, Object> event = new HashMap<>();
            Map<String, String> queryParams = new HashMap<>();
            
            queryParams.put("fileName", fileName);
            
            event.put("queryStringParameters", queryParams);
            
            String payload = objectMapper.writeValueAsString(event);
            
            InvokeRequest invokeRequest = new InvokeRequest()
                    .withFunctionName(functionName)
                    .withPayload(payload);
            InvokeResult invokeResult = lambdaClient.invoke(invokeRequest);
            String resultJson = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
            
            // Parse the top-level response from Lambda.
            Map<?,?> resultMap = objectMapper.readValue(resultJson, Map.class);
            // The presigned URL is inside the body field (which is a JSON string)
            String bodyJson = (String) resultMap.get("body");
            if (bodyJson == null) {
                System.out.println("Lambda response does not contain a body");
                return null;
            }
            // Parse the body JSON to retrieve the URL.
            Map<?,?> bodyMap = objectMapper.readValue(bodyJson, Map.class);
            return (String) bodyMap.get("url");
        } catch (Exception e) {
            System.out.println("shaggggg");
            e.printStackTrace();
            return null;
        }
    }    
}
