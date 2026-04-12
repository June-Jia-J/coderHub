-- 将 admin 密码修正为 123456（jBCrypt 兼容的 BCrypt 哈希）
UPDATE `user` SET `password_hash` = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi' WHERE `username` = 'admin';
