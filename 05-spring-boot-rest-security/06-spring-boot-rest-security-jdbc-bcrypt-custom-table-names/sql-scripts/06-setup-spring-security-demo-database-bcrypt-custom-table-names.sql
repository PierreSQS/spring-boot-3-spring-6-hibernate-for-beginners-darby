USE `user_directory`;

DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `members`;

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `user_id` varchar(50) NOT NULL,
  `pw` char(70) NOT NULL,
  `active` tinyint NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `members`
--
-- NOTE: The passwords are encrypted using BCrypt
--
-- A generation tool is avail at: https://www.luv2code.com/generate-bcrypt-password
--
-- Default passwords here are: fun123
--

INSERT INTO `members`
VALUES
('John','{bcrypt}$2a$10$OfpRdhmoBePWN1YHrM21huQBHsRUAFRmsh/zMQOjy3RW1bIpovL0u',1),
('Mary','{bcrypt}$2a$10$nWOkg2fC2RVMqB0kCVxtkuDbqa9jBSC5dcgnIikdohNC1vR/D3YuK',1),
('Susan','{bcrypt}$2a$10$5SP.QChkUnPqnNR.bMHop.ByM/a3MKlNTB5Y3Ies362lFE3wlJEoC',1);


--
-- Table structure for table `authorities`
--

CREATE TABLE `roles` (
  `user_id` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `authorities5_idx_1` (`user_id`,`role`),
  CONSTRAINT `authorities5_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `members` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Inserting data for table `roles`
--

INSERT INTO `roles`
VALUES
('John','ROLE_EMPLOYEE'),
('Mary','ROLE_EMPLOYEE'),
('Mary','ROLE_MANAGER'),
('Susan','ROLE_EMPLOYEE'),
('Susan','ROLE_MANAGER'),
('Susan','ROLE_ADMIN');
