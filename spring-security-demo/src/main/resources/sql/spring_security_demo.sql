/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : spring_security_demo

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2020-03-06 11:00:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for authorities
-- ----------------------------
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `username` varchar(50) NOT NULL,
  `authority` varchar(50) NOT NULL,
  PRIMARY KEY (`username`,`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of authorities
-- ----------------------------
INSERT INTO `authorities` VALUES ('123', 'ROLE_USER');
INSERT INTO `authorities` VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO `authorities` VALUES ('admin', 'ROLE_USER');
INSERT INTO `authorities` VALUES ('user', 'ROLE_USER');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('123', '123', '0');
INSERT INTO `users` VALUES ('admin', 'admin', '1');
INSERT INTO `users` VALUES ('user', 'user', '1');
