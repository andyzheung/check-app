-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: check_app
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_area`
--

DROP TABLE IF EXISTS `t_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_area` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '区域ID',
  `area_code` varchar(8) NOT NULL COMMENT '区域编码',
  `qr_code_url` varchar(255) DEFAULT NULL COMMENT '二维码图片URL',
  `area_name` varchar(50) NOT NULL COMMENT '区域名称',
  `area_type` char(1) NOT NULL COMMENT '区域类型：A-机房,B-办公�?C-设备�?,
  `code` varchar(50) NOT NULL COMMENT '区域编码',
  `name` varchar(100) NOT NULL COMMENT '区域名称',
  `description` varchar(500) DEFAULT NULL COMMENT '区域描述',
  `address` varchar(200) DEFAULT NULL COMMENT '区域地址',
  `type` varchar(50) DEFAULT NULL COMMENT '区域类型',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '区域状态：active-活跃，inactive-未激�?,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  UNIQUE KEY `uk_area_code` (`area_code`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检区域�?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_area_check_item`
--

DROP TABLE IF EXISTS `t_area_check_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_area_check_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `area_id` bigint NOT NULL COMMENT '区域ID',
  `name` varchar(100) NOT NULL COMMENT '巡检项名�?,
  `type` varchar(50) NOT NULL COMMENT '巡检项类型：environment-环境巡检项，security-安全巡检项，device-设备巡检�?,
  `sort` int DEFAULT '0' COMMENT '排序',
  `required` tinyint DEFAULT '1' COMMENT '是否必填�?-否，1-�?,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='区域巡检项模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_area_type`
--

DROP TABLE IF EXISTS `t_area_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_area_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type_code` char(1) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型编码',
  `type_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型名称',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型描述',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_code` (`type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='区域类型�?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_department`
--

DROP TABLE IF EXISTS `t_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_department` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` varchar(100) NOT NULL COMMENT '部门名称',
  `code` varchar(50) NOT NULL COMMENT '部门编码',
  `parent_id` bigint DEFAULT NULL COMMENT '父部门ID',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` int DEFAULT '1' COMMENT '状态（0-禁用�?-启用�?,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门�?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_inspection_item`
--

DROP TABLE IF EXISTS `t_inspection_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_inspection_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '巡检项ID',
  `record_id` bigint NOT NULL COMMENT '巡检记录ID',
  `name` varchar(100) NOT NULL COMMENT '巡检项名�?,
  `type` varchar(50) NOT NULL COMMENT '巡检项类型：environment-环境巡检项，security-安全巡检项，device-设备巡检�?,
  `status` varchar(20) NOT NULL DEFAULT 'normal' COMMENT '巡检项状态：normal-正常，abnormal-异常',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_inspection_record`
--

DROP TABLE IF EXISTS `t_inspection_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_inspection_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `record_no` varchar(50) NOT NULL COMMENT '记录编号',
  `task_id` bigint DEFAULT NULL COMMENT '巡检任务ID',
  `area_id` bigint NOT NULL COMMENT '巡检区域ID',
  `inspector_id` bigint NOT NULL COMMENT '巡检人员ID',
  `start_time` datetime NOT NULL COMMENT '巡检开始时�?,
  `end_time` datetime DEFAULT NULL COMMENT '巡检结束时间',
  `status` varchar(20) NOT NULL DEFAULT 'normal' COMMENT '巡检状态：normal-正常，abnormal-异常',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `route_path` text COMMENT '巡检路径（JSON格式存储�?,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_no` (`record_no`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检记录�?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_inspection_task`
--

DROP TABLE IF EXISTS `t_inspection_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_inspection_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `area_id` bigint NOT NULL COMMENT '巡检区域ID',
  `inspector_id` bigint NOT NULL COMMENT '巡检人员ID',
  `plan_time` datetime DEFAULT NULL COMMENT '计划巡检时间',
  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '任务状态：pending-待执行，processing-执行中，completed-已完成，canceled-已取�?,
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检任务�?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_issue`
--

DROP TABLE IF EXISTS `t_issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_issue` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '问题ID',
  `issue_no` varchar(50) NOT NULL COMMENT '问题编号',
  `record_id` bigint NOT NULL COMMENT '巡检记录ID',
  `item_id` bigint DEFAULT NULL COMMENT '巡检项ID',
  `description` varchar(500) NOT NULL COMMENT '问题描述',
  `type` varchar(50) DEFAULT NULL COMMENT '问题类型：environment-环境问题，security-安全问题，device-设备问题，other-其他',
  `status` varchar(20) NOT NULL DEFAULT 'open' COMMENT '问题状态：open-未处理，processing-处理中，closed-已关�?,
  `recorder_id` bigint NOT NULL COMMENT '记录人ID',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` varchar(500) DEFAULT NULL COMMENT '处理结果',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_issue_no` (`issue_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题�?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_issue_process`
--

DROP TABLE IF EXISTS `t_issue_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_issue_process` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `issue_id` bigint NOT NULL COMMENT '问题ID',
  `action` varchar(20) NOT NULL COMMENT '操作类型：create-创建，process-处理，close-关闭',
  `processor_id` bigint NOT NULL COMMENT '处理人ID',
  `process_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '处理时间',
  `content` varchar(500) NOT NULL COMMENT '处理内容',
  `images` text COMMENT '图片（JSON数组存储多张图片URL�?,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题处理记录�?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_notification`
--

DROP TABLE IF EXISTS `t_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_notification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `title` varchar(100) NOT NULL COMMENT '通知标题',
  `content` varchar(500) NOT NULL COMMENT '通知内容',
  `type` varchar(50) NOT NULL COMMENT '通知类型：task-任务通知，system-系统通知，issue-问题通知',
  `status` varchar(20) NOT NULL DEFAULT 'unread' COMMENT '通知状态：unread-未读，read-已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`),
  KEY `idx_user_status` (`user_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息通知�?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户�?,
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `department_id` bigint DEFAULT NULL COMMENT '部门ID',
  `role` varchar(20) NOT NULL DEFAULT 'user' COMMENT '角色：admin-管理员，user-普通用�?,
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '状态：active-活跃，inactive-未激活，locked-已锁�?,
  `phone` varchar(20) DEFAULT NULL COMMENT '手机�?,
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时�?,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户�?;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_permission`
--

DROP TABLE IF EXISTS `t_user_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `permission_code` varchar(50) NOT NULL COMMENT '权限代码',
  `permission_name` varchar(100) DEFAULT NULL COMMENT '权限名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除�?-未删除，1-已删�?,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_permission` (`user_id`,`permission_code`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户权限�?;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-10 12:56:52
