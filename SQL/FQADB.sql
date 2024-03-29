/*
 Navicat Premium Data Transfer

 Source Server         : FAQDB
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : bj-cynosdbmysql-grp-kf9vc2hk.sql.tencentcdb.com:21437
 Source Schema         : FQADB

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 31/08/2022 02:43:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager`  (
  `id` int(11) NOT NULL,
  `qq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `question_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1301 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `answer_id` int(11) NULL DEFAULT NULL,
  `question_id` int(11) NULL DEFAULT NULL,
  `group_id` bigint(20) NULL DEFAULT NULL,
  `last_edit_user` bigint(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKhmw49oeaxy93yc72okw2voxjk`(`answer_id`) USING BTREE,
  INDEX `FK6tbw2ts0kacdf64f5b2hansqy`(`question_id`) USING BTREE,
  CONSTRAINT `FK6tbw2ts0kacdf64f5b2hansqy` FOREIGN KEY (`question_id`) REFERENCES `message` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKhmw49oeaxy93yc72okw2voxjk` FOREIGN KEY (`answer_id`) REFERENCES `message` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 653 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service
-- ----------------------------
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pattern` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `pattern_mode` int(255) NOT NULL,
  `req_method` int(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for welcome
-- ----------------------------
DROP TABLE IF EXISTS `welcome`;
CREATE TABLE `welcome`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group` bigint(255) NOT NULL,
  `talk` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
