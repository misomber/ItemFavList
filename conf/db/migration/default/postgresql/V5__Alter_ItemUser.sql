ALTER TABLE item_user ADD FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE;
ALTER TABLE item_user ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;