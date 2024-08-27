
create type role as ENUM ('ADMIN', 'MANAGER', 'USER');

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    employee_id BIGINT UNIQUE NOT NULL,
    role role
);