LOCK TABLES `country` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `country` (`id`, `name`) VALUES
  (1, 'ITALY');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `hospital` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `hospital` (`id`, `name`) VALUES
  (1, 'Base Hospital, Kalubowila');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `division` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `division` (`id`, `name`) VALUES
  (1, 'Mt.Lavinia');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `station` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `station` (`id`, `name`, `division_id`) VALUES
  (1, 'Moratuwa', 1),
  (2, 'Ratmalana', 1),
  (3, 'Nugegoda', 1);
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `grama_sewa_division` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `grama_sewa_division` (`id`, `name`, `station_id`) VALUES
  (1, 'Piliyandala', 1),
  (2, 'Katubedda', 1),
  (3, 'Kalubowila', 2),
  (4, 'Dehiwala', 2);
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `user` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `user` (`id`, `name`, `mobile`, `nic`, `passport_no`, `password`, `username`) VALUES
  (1, 'Root', '0777332170', '931242610v', null, '$2a$10$7ICST1HMjRUmA8gwfyA9KO5VeC6C8cgjH1twFjOkmxn9u2lFrLL4y', 'Root'),
  (2, 'Udara Silva', '0777334170', '651242610v', null, '$2a$10$zGHr0X8ZQvErEBz5dscos.A5OM3FHNDRkwSf9RWYKljVnh8.NVdg6', '0777334170'),
  (3, 'Persingha', '0777334175', '591242610v', null, '$2a$10$r3QZ3ckQSTdMDkGpLZB3Zu0hVYq/v0XW68t0UsCJHEHOH67r4N2Mq', '0777334175'),
  (4, 'Aravinda', '0777334176', '691242610v', null, '$2a$10$Hnxn6hC9D5UfI5rJFEp92OSsROTAsr93rMTv5tvuf9tX4hlWbw0gu', '0777334176');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `report_user` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `report_user` (`id`, `office_id`, `rank`, `showing_name`) VALUES
  (2, '15016', 'ASP', 'Udara Silva 15016'),
  (3, null, 'SSP', 'Persingha'),
  (4, '81989', 'SERAYAN', 'Aravinda 81989');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `user_station` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `user_station` (`user_id`, `station_id`) VALUES
  (2, 1),
  (2, 2),
  (3, 1),
  (3, 2),
  (3, 3),
  (4, 1);
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `role` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `role` (`id`, `name`) VALUES
  (1, 'ROOT'),
  (2, 'ADMIN'),
  (3, 'GUARDIAN'),
  (4, 'Q_USER');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `user_role` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `user_role` (`id`, `role_id`, `user_id`, `create_user`) VALUES
  (1, 1, 1, true),
  (2, 2, 1, true),
  (3, 2, 2, true),
  (4, 2, 3, true),
  (5, 2, 4, false);
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;