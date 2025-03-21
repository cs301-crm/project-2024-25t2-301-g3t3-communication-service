INSERT INTO client_communications (
    id, agent_id, client_id, client_email, crud_type, subject, status, timestamp
) VALUES (
    gen_random_uuid(),
    'agent_123', 
    'client_456', 
    'client@example.com', 
    'CREATE'::VARCHAR,  
    'Welcome Email'::VARCHAR,  
    'SENT'::VARCHAR,  
    NOW()
);

INSERT INTO user_communications (
    id, username, user_role, user_email, temp_password, subject, status, timestamp
) VALUES (
    gen_random_uuid(),
    'agent_1234', 
    'AGENT', 
    'agent@example.com', 
    '12i4yn1uxg1ui338'::VARCHAR,  
    'Welcome Email'::VARCHAR,  
    'SENT'::VARCHAR,  
    NOW()
);
