Create TABLE CATEGORIES_SQL(
    id int(10) UNSIGNED NOT NULL,
    name varchar(255) NOT NULL,
    category_id int(11) NOT NULL,
    index_position int(10) DEFAULT 0,
    update_at timestamp NULL DEFAULT NULL
);

ALTER TABLE `CATEGORIES_SQL` ADD PRIMARY KEY (`id`);

ALTER TABLE `CATEGORIES_SQL` MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0;

INSERT INTO CATEGORIES_SQL (name, category_id, index_position, update_at) VALUES("TEST CATEGORY", 1, 1, "2017-01-10 00:00:00")