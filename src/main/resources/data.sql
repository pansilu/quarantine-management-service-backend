LOCK TABLES `country` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `country` (`id`, `name`) VALUES
  (1, 'ITALY'),
  (2, 'USA'),
  (3, 'Spain'),
  (4, 'Germany'),
  (5, 'France'),
  (6, 'China'),
  (7, 'Iran'),
  (8, 'UK'),
  (9, 'Turkey'),
  (10, 'Switzerland'),
  (11, 'Belgium'),
  (12, 'Netherlands	'),
  (13, 'Canada'),
  (14, 'Austria'),
  (15, 'Portugal'),
  (16, 'S. Korea'),
  (17, 'Brazil'),
  (18, 'Israel'),
  (19, 'Sweden'),
  (20, 'Australia'),
  (21, 'Norway'),
  (22, 'Russia'),
  (23, 'Ireland'),
  (24, 'Czechia'),
  (25, 'Chile'),
  (26, 'Denmark'),
  (27, 'Poland'),
  (28, 'Romania'),
  (29, 'Malaysia'),
  (30, 'Ecuador'),
  (31, 'Philippines'),
  (32, 'India'),
  (33, 'Japan'),
  (34, 'Pakistan'),
  (35, 'Luxembourg'),
  (36, 'Saudi Arabia'),
  (37, 'Indonesia'),
  (38, 'Thailand'),
  (39, 'Finland'),
  (40, 'Mexico'),
  (41, 'Greece'),
  (42, 'Panama'),
  (43, 'Serbia'),
  (44, 'Peru'),
  (45, 'South Africa'),
  (46, 'UAE'),
  (47, 'Dominican Republic'),
  (48, 'Iceland'),
  (49, 'Argentina'),
  (50, 'Qatar'),
  (51, 'Colombia'),
  (52, 'Algeria'),
  (53, 'Singapore'),
  (54, 'Croatia'),
  (55, 'Ukraine'),
  (56, 'Estonia'),
  (57, 'Egypt'),
  (58, 'Slovenia'),
  (59, 'New Zealand'),
  (60, 'Iraq'),
  (61, 'Hong Kong'),
  (62, 'Morocco'),
  (63, 'Lithuania'),
  (64, 'Bahrain'),
  (65, 'Lebanon'),
  (66, 'Cyprus'),
  (67, 'Taiwan'),
  (68, 'Oman'),
  (68, 'Bangladesh'),
  (69, 'Maldives');
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
  (1, 'MountLavinia', 1),
  (2, 'Moratuwa', 1),
  (3, 'Dehiwala', 1),
  (4, 'Kohuwala', 1),
  (5, 'Piliyandala', 1),
  (6, 'Kahathuduwa', 1),
  (7, 'Moratumulla', 1),
  (8, 'Angulana', 1),
  (9, 'Egoda Uyana', 1),
  (10, 'Maththegoda', 1);
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

/*LOCK TABLES `grama_sewa_division` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `grama_sewa_division` (`id`, `name`, `station_id`, `code`) VALUES
  (1, 'Molpe 551A', 7, '551A'),
  (2, 'Kuduwamulla 551B', 7, '551B'),
  (3, 'Moratumulla North 551C', 7, '551C'),
  (4, 'Moratumulla East 558', 7, '558'),
  (5, 'Kadalana 558A', 7, '558A'),
  (6, 'Moratumulla West 558B', 7, '558B'),
  (7, 'Indibedda West 559', 7, '559'),
  (8, 'Indibedda East 559A', 7, '559A'),
  (9, 'Willorawaththa East 560/61', 7, '560/61'),
  (10, 'Willorawaththa West 560/61A', 7, '560/61A'),
  (11, 'Kawudana West ', 1, ''),
  (12, 'Kawudana East ', 1, ''),
  (13, 'Mount Lavinia ', 1, ''),
  (14, 'Aththidiya North ', 1, ''),
  (15, 'Aththidiya South ', 1, ''),
  (16, 'Kandawala ', 1, ''),
  (17, 'Watarappala ', 1, ''),
  (18, 'Wathumulla ', 1, ''),
  (19, 'Piriwena ', 1, ''),
  (20, 'Katukurunduwaththa ', 1, ''),
  (21, 'Rathmalana West ', 1, ''),
  (22, 'Rathmalana East ', 1, ''),
  (23, 'Wedikanda ', 1, ''),
  (24, 'Wihara ', 1, ''),
  (25, 'Soysapura North ', 1, ''),
  (26, 'Soysapura South ', 1, ''),
  (27, 'Thelawala North ', 1, ''),
  (28, 'Thelawala South ', 1, ''),
  (29, 'Kaldemulla ', 1, ''),
  (30, 'Borupana ', 1, ''),
  (31, 'Dahampura ', 1, ''),
  (32, 'Lakshapathiya 550', 2, '550'),
  (33, 'Lakshapathiya North	550A', 2, '550A'),
  (34, 'Lakshapathiya Central 550B', 2, '550B'),
  (35, 'Katubedda 551', 2, '551'),
  (36, 'Uyana North 552B', 2, '552B'),
  (37, 'Uyana South 552A', 2, '552A'),
  (38, 'Rawathawaththa East	557', 2, '557'),
  (39, 'Rawathawaththa South 557A', 2, '557A'),
  (40, 'Idama 552', 2, '552'),
  (41, 'Moratuwella North 553A', 2, '553A'),
  (42, 'Uswaththa 553C', 2, '553C'),
  (43, 'Moratuwella South 553', 2, '553'),
  (44, 'Moratuwella West ', 2, ''),
  (45, 'Koralawella East 554', 2, '554'),
  (46, 'Koralawella North 554B', 2, '554B'),
  (47, 'Dehiwala West 540A', 3, '540A'),
  (48, 'Karagampitiya 539/42', 3, '539/42'),
  (49, 'Nedimala 536', 3, '536'),
  (50, 'Malwaththa 539/42/A', 3, '539/42/A'),
  (51, 'Dehiwala East 540', 3, '540'),
  (52, 'Udyanaya 536A', 3, '536A'),
  (53, 'Kawudana East 539/42/B', 3, '539/42/B'),
  (54, 'Katukurunduwaththa 545A', 3, '545A'),
  (55, 'Jayathilaka	540B', 3, '540B'),
  (56, 'Galwala	538A', 3, '538A'),
  (57, 'Kawudana West 539/42/C', 3, '539/42/C'),
  (58, 'Mount Lavinia 541', 3, '541'),
  (59, 'Jambugasmulla 526A', 4, '526A'),
  (60, 'Gangodawila North 526', 4, '526'),
  (61, 'Nandimala 536', 4, '536'),
  (62, 'Wilawila 537', 4, '537'),
  (63, 'Dutugemunu Vidiya 537A', 4, '537A'),
  (64, 'Kohuwela 538B', 4, '538B'),
  (65, 'Kalubowila 538', 4, '538'),
  (66, 'Hathbodiya 538B', 4, '538B'),
  (67, 'Saranankara 538C', 4, '538C'),
  (68, 'Wewala East 562', 5, '562'),
  (69, 'Wewala West	562B', 5, '562B'),
  (70, 'Suwarapola East 562A', 5, '562A'),
  (71, 'Suwarapola West	562C', 5, '562C'),
  (72, 'Hedigama 563', 5, '563'),
  (73, 'Delthara West 564', 5, '564'),
  (74, 'Delthara East 564A', 5, '564A'),
  (75, 'Batakeththara North	565', 5, '565'),
  (76, 'Batakeththara South	565A', 5, '565A'),
  (77, 'Dampe 566', 5, '566'),
  (78, 'Madapatha 567', 5, '567'),
  (79, 'Moreda 568', 5, '568'),
  (80, 'Niwungama 568A', 5, '568A'),
  (81, 'Makandana East 569', 5, '569'),
  (82, 'Makandana West 569A', 5, '569A'),
  (83, 'Halpita	570', 5, '570'),
  (84, 'Horathuduwa	571A', 5, '571A'),
  (85, 'Kesbewa North 572', 5, '572'),
  (86, 'Kesbewa South 572A', 5, '572A'),
  (87, 'Kesbewa East 572B', 5, '572B'),
  (88, 'Kolamunna 573', 5, '573'),
  (89, 'Mampe West 574', 5, '574'),
  (90, 'Mampe North 574A', 5, '574A'),
  (91, 'Wishwakalawa 574B', 5, '574B'),
  (92, 'Mampe South	574C', 5, '574C'),
  (93, 'Mampe East 574D', 5, '574D'),
  (94, 'Bokundara 575', 5, '575'),
  (95, 'Thumbowila West	576', 5, '576'),
  (96, 'Thumbowila North 576A', 5, '576A'),
  (97, 'Thumbowila South 576B', 5, '576B'),
  (98, 'Kaliyammahara 580', 5, '580'),
  (99, 'Niwanthidiya 580A', 5, '580A'),
  (100, 'Pelanwaththa North 582', 5, '582'),
  (101, 'Pelanwaththa West 582A', 5, '582A'),
  (102, 'Pelanwaththa East 582B', 5, '582B'),
  (103, 'Makuluduwa 583', 5, '583'),
  (104, 'Paligedara 583A', 5, '583A'),
  (105, 'Gorakapitiya 584', 5, '584'),
  (106, 'Nampamunuwa	584A', 5, '584A'),
  (107, 'Honnanthara North 585', 5, '585'),
  (108, 'Honnanthara South 585A', 5, '585A'),
  (109, 'Mawiththara South 586', 5, '586'),
  (110, 'Mawiththara North 586A', 5, '586A'),
  (111, 'Siddhamulla North 591', 5, '591'),
  (112, 'Siddhamulla South 591B', 5, '591B'),
  (113, 'Sangharama 591D', 5, '591D'),
  (114, 'Batuwandara North 596', 5, '596'),
  (115, 'Batuwandara South 596A', 5, '596A'),
  (116, 'Jamburuliya	597', 5, '597'),
  (117, 'Kahapola 598', 5, '598'),
  (118, 'Regidalewaththa	598A', 5, '598A'),
  (119, 'Polhena	598B', 5, '598B'),
  (120, 'Kahathuduwa West 602B', 6, '602B'),
  (121, 'Kahathuduwa North 602', 6, '602'),
  (122, 'Kahathuduwa South 602A', 6, '602A'),
  (123, 'Kahathuduwa East 602C', 6, '602C'),
  (124, 'Undurugoda 600', 6, '600'),
  (125, 'Wethara 593', 6, '593'),
  (126, 'Ambalangoda 594', 6, '594'),
  (127, 'Heraliyawala 595', 6, '595'),
  (128, 'Pahalagama 599', 6, '599'),
  (129, 'Siyambalagoda North 592', 6, '592'),
  (130, 'Siyambalagoda South 592A', 6, '592A'),
  (131, 'Weniwalkola 601A', 6, '601A'),
  (132, 'Rilawala 593A', 6, '593A'),
  (133, 'Diyagama West 589A', 6, '589A'),
  (134, 'Diyagama East 589', 6, '589'),
  (135, 'Magammana West 590A', 6, '590A'),
  (136, 'Magammana East 590', 6, '590'),
  (137, 'Kiriwaththuduwa North 603', 6, '603'),
  (138, 'Kiriwaththuduwa South 603', 6, '603'),
  (139, 'Munamale Yakahaluwa 603C', 6, '603C'),
  (140, 'Kithulawila 603B', 6, '603B'),
  (141, 'Angulana North	547', 8, '547'),
  (142, 'Angulana South	547A', 8, '547A'),
  (143, 'Egodauyana South 556B', 9, '556B'),
  (144, 'Egodauyana Central	556A', 9, '556A'),
  (145, 'Egodauyana North 556', 9, '556'),
  (146, 'Katukurunda South 555A', 9, '555A'),
  (147, 'Katukurunda North 555', 9, '555'),
  (148, 'Koralawella South 554A', 9, '554A'),
  (149, 'Koralawella West 555C', 9, '555C'),
  (150, 'Maththegoda West 587', 10, '587'),
  (151, 'Maththegoda Central 587A', 10, '587A'),
  (152, ' 587B', 10, '587B'),
  (153, 'Maththegoda East 587C', 10, '587C'),
  (154, 'Kirigampamunuwa 588', 10, '588'),
  (155, 'Dipangoda 590B', 10, '590B'),
  (156, 'Kudamaduwa	591A', 10, '591A'),
  (157, 'Kithulahena 591C', 10, '591C');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;*/

LOCK TABLES `user` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `user` (`id`, `name`, `mobile`, `nic`, `passport_no`, `password`, `username`) VALUES
  (1, 'Root', '0777332170', '931242610v', null, '$2a$10$7ICST1HMjRUmA8gwfyA9KO5VeC6C8cgjH1twFjOkmxn9u2lFrLL4y', 'Root');
  /*(2, 'Udara Silva', '0777334170', '651242610v', null, '$2a$10$3yq6B81IbAIK.UNi1a1fcea2LYhnhzS8tmiC5ADOheO6X0hij6SYa', '0777334170'),
  (3, 'Persingha', '0777334175', '591242610v', null, '$2a$10$R.K5aRDeAeX0aiDOYJpj/O8kraMEAvY.VW4/zNKdCl9MEPeSWRv7m', '0777334175'),
  (4, 'Aravinda', '0777334176', '691242610v', null, '$2a$10$hkWuA44g4/NtnDVQoI74NOUAE1GuFmp3UQxoW5/oZxcX23t35YnQi', '0777334176');*/
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;

/*LOCK TABLES `report_user` WRITE;
SET FOREIGN_KEY_CHECKS=0;
REPLACE INTO `report_user` (`id`, `office_id`, `rank`, `showing_name`) VALUES
  (2, '15016', 'ASP', 'Udara Silva 15016'),
  (3, '23456', 'SSP', 'Persingha'),
  (4, '81989', 'SARJANT', 'Aravinda 81989');
SET FOREIGN_KEY_CHECKS=1;
UNLOCK TABLES;*/

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
  (1, 1, 1, true);
  /*(2, 2, 1, true);*/
  /*(3, 2, 2, true),
  (4, 2, 3, true),
  (5, 2, 4, false);*/
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