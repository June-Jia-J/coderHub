-- user 表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(64) NOT NULL,
    `password_hash` VARCHAR(255) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- blog 表
CREATE TABLE IF NOT EXISTS `blog` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `title` VARCHAR(256) NOT NULL,
    `content` TEXT NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at` DESC),
    CONSTRAINT `fk_blog_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed: 演示用户 (密码 123456 的 BCrypt 哈希)
INSERT INTO `user` (`username`, `password_hash`) VALUES
('admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW');

-- Seed: 演示博客 (admin 的 id 为 1)
INSERT INTO `blog` (`user_id`, `title`, `content`) VALUES
(1, '欢迎来到程序员博客', '这是一篇示例博客。\n\n支持 **Markdown** 书写，可以在这里分享技术心得。\n\n- 列表\n- 代码块\n- 链接等'),
(1, 'Spring Boot 快速入门', 'Spring Boot 让 Java 应用开发更简单。\n\n## 主要特性\n\n- 自动配置\n- 内嵌 Tomcat\n- 起步依赖');
