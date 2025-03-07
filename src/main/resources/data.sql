INSERT INTO communications (
    id, agent_id, client_id, client_email, crud_type, subject, message_body, status, timestamp
) VALUES (
    gen_random_uuid(),
    'agent_123', 
    'client_456', 
    'client@example.com', 
    'CREATE'::VARCHAR,  
    'Welcome Email'::VARCHAR, 
    'Hello, welcome to our service!'::TEXT,  
    'SENT'::VARCHAR,  
    NOW()
);
