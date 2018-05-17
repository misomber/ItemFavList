CREATE TABLE `items` (
 `id`        BIGINT                 AUTO_INCREMENT,
 `code`      VARCHAR(255)  NOT NULL,
 `name`      VARCHAR(1024) NOT NULL,
 `url`       VARCHAR(255)  NOT NULL,
 `image_url` VARCHAR(255)  NOT NULL,
 `price`     INT           NOT NULL,
 `create_at` TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `update_at` TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (`id`)
)
 ENGINE = InnoDB;

-- codeで検索するためインデックスを割り当てておく
CREATE INDEX `items_code_idx` ON `items` (`code`);