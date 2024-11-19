-- 用户表
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- 邮件表
CREATE TABLE emails (
    email_id INT AUTO_INCREMENT PRIMARY KEY,
    sender VARCHAR(100) NOT NULL,
    receiver VARCHAR(100) NOT NULL,
    subject VARCHAR(255),
    body TEXT,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE
);

-- 为了更好地关联用户表，可以增加外键约束（假设用户表中`username`是唯一标识）
ALTER TABLE emails
ADD CONSTRAINT fk_receiver FOREIGN KEY (receiver) REFERENCES users(email);
ALTER TABLE emails
ADD CONSTRAINT fk_sender FOREIGN KEY (sender) REFERENCES users(email);
