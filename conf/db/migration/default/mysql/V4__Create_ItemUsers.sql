CREATE TABLE `item_user` (
 `id`        BIGINT               AUTO_INCREMENT,
 `item_id`   BIGINT      NOT NULL,
 `user_id`   BIGINT      NOT NULL,
 `type`      VARCHAR(64) NOT NULL,
 `create_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `update_at` TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (`id`),
 FOREIGN KEY (`item_id`) REFERENCES `items` (`id`),
 FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
 UNIQUE (`item_id`, `user_id`, `type`)
)
 ENGINE = InnoDB;