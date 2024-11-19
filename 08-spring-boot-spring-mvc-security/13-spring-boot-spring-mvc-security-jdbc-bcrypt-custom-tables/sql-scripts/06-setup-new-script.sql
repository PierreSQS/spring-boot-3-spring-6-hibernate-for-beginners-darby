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
('John','{bcrypt}$2a$10$c9O0xw8IuBDYqQPesZh6xOBlxqsC/E/f0wAf2FS5mhgkUYFXNyAPG',1),
('Mary','{bcrypt}$2a$10$SimYWWWIMX/e4PUfiCMVH.x23XMkkJbXNyDGLMXPIotfBo0zPos1W',1),
('Susan','{bcrypt}$2a$10$/MolWFiF8w5DVypImC40tuOttgN2UqUv/X6UxmF9rs/74VDMTgB3K',1);


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
