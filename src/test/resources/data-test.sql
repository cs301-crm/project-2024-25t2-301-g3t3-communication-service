INSERT INTO communications (
    id, agent_id, client_id, client_email, crud_type, subject, message_body, status, timestamp
) VALUES (
    gen_random_uuid(),
    'agent_123_test', 
    'client_456_test', 
    'client_test@example.com', 
    'CREATE'::VARCHAR,  
    'Welcome Email Test'::VARCHAR, 
    'Hello, welcome to our test service!'::TEXT,  
    'SENT'::VARCHAR,  
    NOW()
);
