-- V1__Create_post_table.sql
CREATE TABLE member (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      user_name VARCHAR(255) NOT NULL,
                      created_by VARCHAR(125),
                      updated_by VARCHAR(125),
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);