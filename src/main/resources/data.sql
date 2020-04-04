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
  (2, 'Udara Silva', '0777334170', '651242610v', null, '$2a$10$3yq6B81IbAIK.UNi1a1fcea2LYhnhzS8tmiC5ADOheO6X0hij6SYa', '0777334170'),
  (3, 'Persingha', '0777334175', '591242610v', null, '$2a$10$R.K5aRDeAeX0aiDOYJpj/O8kraMEAvY.VW4/zNKdCl9MEPeSWRv7m', '0777334175'),
  (4, 'Aravinda', '0777334176', '691242610v', null, '$2a$10$hkWuA44g4/NtnDVQoI74NOUAE1GuFmp3UQxoW5/oZxcX23t35YnQi', '0777334176');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

LOCK TABLES `report_user` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `report_user` (`id`, `office_id`, `rank`, `showing_name`) VALUES
  (2, '15016', 'ASP', 'Udara Silva 15016'),
  (3, '23456', 'SSP', 'Persingha'),
  (4, '81989', 'SARJANT', 'Aravinda 81989');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

/*LOCK TABLES `user_station` WRITE;
SET FOREIGN_KEY_CHECKS=0;
INSERT IGNORE INTO `user_station` (`user_id`, `station_id`) VALUES
  (2, 1),
  (2, 2),
  (3, 1),
  (3, 2),
  (3, 3),
  (4, 1);
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;*/

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

LOCK TABLES `point` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `point` (`id`, `name`, `code`, `value`, `is_fixed`) VALUES
  (1, 'Cough', 'COUGH', 1, false),
  (2, 'Cold', 'COLD', 1, false),
  (3, 'Diarrhea', 'DIARRHEA', 1, false),
  (4, 'Sore Throat', 'THROAT', 1, false),
  (5, 'MYALGIA or Body Aches', 'MYALGIA', 1, false),
  (6, 'Headache', 'HEADACHE', 1, false),
  (7, 'Fever', 'FEVER', 1, false),
  (8, 'Difficulty Breathing', 'BREATH', 2, false),
  (9, 'Fatigue', 'FATIGUE', 2, false),
  (10, 'Travel Recently', 'TRAVEL', 3, true),
  (11, 'Travel in Infected Area', 'AREA', 3, true),
  (12, 'Direct Contact With Covid-19 Patient', 'CONTACT', 3, true);
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;