-- V1__Create_post_table.sql
CREATE TABLE post (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      title VARCHAR(255) NOT NULL,
                      content TEXT NOT NULL,
                      author_id BIGINT NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);