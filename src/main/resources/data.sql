/*LOCK TABLES `user` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `user` (`id`, `name`, `mobile`, `nic`, `passport_no`, `password`, `username`) VALUES
  (1, 'Root', '0777332170', '931242610v', null, '$2a$10$A1SSTLei.FhDnRbPvQZfJ.2omWd6HHxMhVBnnJUvWbRNcMRNlKjpq', 'Root');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;*/

/*
$2a$10$A1SSTLei.FhDnRbPvQZfJ.2omWd6HHxMhVBnnJUvWbRNcMRNlKjpq ---> root

$2a$10$sfnP2Lj4eyd9jM9hO6bzhuiCAyI5GE/C1SNp74GfBapBsB46ph2J6 ---> long
*/

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
  (1, 1, 1, true);
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;
