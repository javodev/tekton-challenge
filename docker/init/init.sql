CREATE TABLE IF NOT EXISTS call_history (
    id SERIAL PRIMARY KEY,
    call_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    endpoint VARCHAR(255) NOT NULL,
    parameters TEXT,
    response TEXT,
    error TEXT
);

INSERT INTO call_history (endpoint, parameters, response, error)
VALUES (
    '/api/test',
    '{"param1": "value1", "param2": "value2"}',
    '{"message": "Test response"}',
    NULL
);