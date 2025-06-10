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
  `area_type` char(1) NOT NULL COMMENT '区域类型：A-机房,B-办公区,C-设备区',
  `code` varchar(50) NOT NULL COMMENT '区域编码',
  `name` varchar(100) NOT NULL COMMENT '区域名称',
  `description` varchar(500) DEFAULT NULL COMMENT '区域描述',
  `address` varchar(200) DEFAULT NULL COMMENT '区域地址',
  `type` varchar(50) DEFAULT NULL COMMENT '区域类型',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '区域状态：active-活跃，inactive-未激活',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  UNIQUE KEY `uk_area_code` (`area_code`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检区域表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_area`
--

LOCK TABLES `t_area` WRITE;
/*!40000 ALTER TABLE `t_area` DISABLE KEYS */;
INSERT INTO `t_area` VALUES (1,'AREAA001',NULL,'区域1','A','AREA001','机房A','主机房',NULL,'A','active','2025-05-21 21:01:30','2025-05-28 20:13:41',0),(2,'AREAA002',NULL,'区域2','A','AREA002','机房B','备用机房',NULL,'A','active','2025-05-21 21:01:30','2025-05-28 20:13:41',0),(3,'AREAA003',NULL,'区域3','A','AREA003','机房C','测试机房',NULL,'A','active','2025-05-21 21:01:30','2025-05-28 20:13:41',0),(4,'AREAA004',NULL,'区域4','A','AREA004','配电室','主配电室',NULL,'C','active','2025-05-21 21:01:30','2025-05-28 20:13:45',0),(5,'AREAA005',NULL,'区域5','A','AREA005','监控室','安防监控室',NULL,'B','active','2025-05-21 21:01:30','2025-05-28 20:13:44',0),(6,'AREA101',NULL,'主机房A区','A','AREA101','主机房A区','主要服务器机房','数据中心1区','server_room','active','2025-05-30 00:56:12','2025-05-30 00:56:12',0),(7,'AREA102',NULL,'配电室B区','B','AREA102','配电室B区','主要配电室','数据中心2区','power_room','active','2025-05-30 00:56:12','2025-05-30 00:56:12',0),(8,'AREA103',NULL,'网络机房C区','C','AREA103','网络机房C区','核心网络设备机房','数据中心3区','network_room','active','2025-05-30 00:56:12','2025-05-30 00:56:12',0);
/*!40000 ALTER TABLE `t_area` ENABLE KEYS */;
UNLOCK TABLES;

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
  `start_time` datetime NOT NULL COMMENT '巡检开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '巡检结束时间',
  `status` varchar(20) NOT NULL DEFAULT 'normal' COMMENT '巡检状态：normal-正常，abnormal-异常',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `route_path` text COMMENT '巡检路径（JSON格式存储）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_no` (`record_no`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_record`
--

LOCK TABLES `t_inspection_record` WRITE;
/*!40000 ALTER TABLE `t_inspection_record` DISABLE KEYS */;
INSERT INTO `t_inspection_record` VALUES (1,'R20250523203512',NULL,1,1,'2025-05-23 20:35:12','2025-05-23 20:35:12','normal','',NULL,'2025-05-23 20:35:12','2025-05-23 20:35:12',0),(2,'R20250524010239',NULL,1,1,'2025-05-23 17:02:39','2025-05-23 17:02:39','normal','',NULL,'2025-05-24 01:02:39','2025-05-24 01:02:39',0),(3,'R20250524010248',NULL,1,1,'2025-05-23 17:02:48','2025-05-23 17:02:48','normal','',NULL,'2025-05-24 01:02:48','2025-05-24 01:02:48',0),(4,'R20250524010344',NULL,1,1,'2025-05-23 17:03:45','2025-05-23 17:03:45','normal','',NULL,'2025-05-24 01:03:45','2025-05-24 01:03:45',0),(5,'R20250524010417',NULL,1,1,'2025-05-23 17:04:18','2025-05-23 17:04:18','normal','',NULL,'2025-05-24 01:04:18','2025-05-24 01:04:18',0),(6,'R20250524010756',NULL,1,1,'2025-05-23 17:07:56','2025-05-23 17:07:56','normal','',NULL,'2025-05-24 01:07:56','2025-05-24 01:07:56',0),(7,'R20250527130159',NULL,1,1,'2025-05-27 05:01:59','2025-05-27 05:01:59','normal','',NULL,'2025-05-27 13:01:59','2025-05-27 13:01:59',0),(8,'R20250527130214',NULL,1,1,'2025-05-27 05:02:15','2025-05-27 05:02:15','normal','',NULL,'2025-05-27 13:02:15','2025-05-27 13:02:15',0),(9,'R20250527130231',NULL,1,1,'2025-05-27 05:02:31','2025-05-27 05:02:31','normal','',NULL,'2025-05-27 13:02:31','2025-05-27 13:02:31',0),(10,'R20250527130346',NULL,1,1,'2025-05-27 05:03:46','2025-05-27 05:03:46','normal','',NULL,'2025-05-27 13:03:47','2025-05-27 13:03:47',0),(11,'R20250527194718',NULL,1,1,'2025-05-27 11:47:19','2025-05-27 11:47:19','normal','',NULL,'2025-05-27 19:47:19','2025-05-27 19:47:19',0),(12,'R20250527211019',NULL,1,1,'2025-05-27 13:10:19','2025-05-27 13:10:19','normal','',NULL,'2025-05-27 21:10:19','2025-05-27 21:10:19',0),(13,'R20250528195915',NULL,1,1,'2025-05-28 11:59:15','2025-05-28 11:59:15','normal','',NULL,'2025-05-28 19:59:16','2025-05-28 19:59:16',0),(14,'R20250528203415',NULL,1,1,'2025-05-28 12:34:16','2025-05-28 12:34:16','normal','',NULL,'2025-05-28 20:34:16','2025-05-28 20:34:16',0),(15,'R20250528205521',NULL,1,1,'2025-05-28 12:55:21','2025-05-28 12:55:21','normal','',NULL,'2025-05-28 20:55:21','2025-05-28 20:55:21',0),(16,'R20250528205858',NULL,3,1,'2025-05-28 12:58:58','2025-05-28 12:58:58','normal','',NULL,'2025-05-28 20:58:58','2025-05-28 20:58:58',0),(17,'R20250529194802',NULL,3,1,'2025-05-29 11:48:02','2025-05-29 11:48:02','abnormal','',NULL,'2025-05-29 19:48:02','2025-05-29 19:48:02',0),(18,'R20250529202933',NULL,3,1,'2025-05-29 12:29:33','2025-05-29 12:29:33','abnormal','',NULL,'2025-05-29 20:29:33','2025-05-29 20:29:33',0),(19,'REC20240514001',5,1,1,'2025-05-29 22:33:31','2025-05-29 23:33:31','normal',NULL,NULL,'2025-05-30 00:33:31','2025-05-30 00:33:31',0),(20,'REC20240514002',6,2,1,'2025-05-29 21:33:31','2025-05-29 22:33:31','normal',NULL,NULL,'2025-05-30 00:33:31','2025-05-30 00:33:31',0),(21,'REC20240514003',7,3,1,'2025-05-29 20:33:31','2025-05-29 21:33:31','normal',NULL,NULL,'2025-05-30 00:33:31','2025-05-30 00:33:31',0);
/*!40000 ALTER TABLE `t_inspection_record` ENABLE KEYS */;
UNLOCK TABLES;

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
  `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '任务状态：pending-待执行，processing-执行中，completed-已完成，canceled-已取消',
  `create_user_id` bigint NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_task`
--

LOCK TABLES `t_inspection_task` WRITE;
/*!40000 ALTER TABLE `t_inspection_task` DISABLE KEYS */;
INSERT INTO `t_inspection_task` VALUES (1,'机房A区日常巡检',1,1,'2025-05-29 21:53:22','pending',1,'2025-05-29 20:53:22','2025-05-29 20:53:22',0),(2,'机房B区日常巡检',2,1,'2025-05-29 22:53:22','pending',1,'2025-05-29 20:53:22','2025-05-29 20:53:22',0),(3,'配电室日常巡检',3,1,'2025-05-29 23:53:22','pending',1,'2025-05-29 20:53:22','2025-05-29 20:53:22',0),(4,'机房A区周检',1,1,'2025-05-28 20:53:22','completed',1,'2025-05-29 20:53:22','2025-05-29 20:53:22',0),(5,'机房B区周检',2,1,'2025-05-27 20:53:22','completed',1,'2025-05-29 20:53:22','2025-05-29 20:53:22',0),(6,'机房A区日常巡检',1,1,'2025-05-29 22:01:45','PENDING',1,'2025-05-29 21:01:45','2025-05-29 21:01:45',0),(7,'机房B区日常巡检',2,1,'2025-05-29 23:01:45','PENDING',1,'2025-05-29 21:01:45','2025-05-29 21:01:45',0),(8,'配电室日常巡检',3,1,'2025-05-30 00:01:45','PENDING',1,'2025-05-29 21:01:45','2025-05-29 21:01:45',0),(9,'机房A区周检',1,1,'2025-05-28 21:01:45','COMPLETED',1,'2025-05-29 21:01:45','2025-05-29 21:01:45',0),(10,'机房B区周检',2,1,'2025-05-27 21:01:45','COMPLETED',1,'2025-05-29 21:01:45','2025-05-29 21:01:45',0),(11,'主机房A区日常巡检',1,1,'2025-05-30 01:33:15','PENDING',1,'2025-05-30 00:33:15','2025-05-30 00:33:15',0),(12,'配电室B区日常巡检',2,1,'2025-05-30 02:33:15','PENDING',1,'2025-05-30 00:33:15','2025-05-30 00:33:15',0),(13,'网络机房C区日常巡检',3,1,'2025-05-30 03:33:15','PENDING',1,'2025-05-30 00:33:15','2025-05-30 00:33:15',0),(14,'UPS机房D区日常巡检',4,1,'2025-05-30 04:33:15','PENDING',1,'2025-05-30 00:33:15','2025-05-30 00:33:15',0),(15,'主机房A区早班巡检',1,1,'2025-05-29 22:33:15','COMPLETED',1,'2025-05-30 00:33:15','2025-05-30 00:33:15',0),(16,'配电室B区早班巡检',2,1,'2025-05-29 21:33:15','COMPLETED',1,'2025-05-30 00:33:15','2025-05-30 00:33:15',0),(17,'网络机房C区早班巡检',3,1,'2025-05-29 20:33:15','COMPLETED',1,'2025-05-30 00:33:15','2025-05-30 00:33:15',0),(18,'主机房A区日常巡检',6,1,'2025-05-30 01:56:20','PENDING',1,'2025-05-30 00:56:20','2025-05-30 00:56:20',0),(19,'配电室B区日常巡检',7,1,'2025-05-30 02:56:20','PENDING',1,'2025-05-30 00:56:20','2025-05-30 00:56:20',0),(20,'网络机房C区日常巡检',8,1,'2025-05-30 03:56:20','PENDING',1,'2025-05-30 00:56:20','2025-05-30 00:56:20',0),(21,'主机房A区早班巡检',6,1,'2025-05-29 22:56:21','COMPLETED',1,'2025-05-30 00:56:21','2025-05-30 00:56:21',0),(22,'配电室B区早班巡检',7,1,'2025-05-29 21:56:21','COMPLETED',1,'2025-05-30 00:56:21','2025-05-30 00:56:21',0),(23,'网络机房C区早班巡检',8,1,'2025-05-29 20:56:21','COMPLETED',1,'2025-05-30 00:56:21','2025-05-30 00:56:21',0),(24,'主机房A区日常巡检',6,1,'2025-05-30 01:57:28','PENDING',1,'2025-05-30 00:57:28','2025-05-30 00:57:28',0),(25,'配电室B区日常巡检',7,1,'2025-05-30 02:57:28','PENDING',1,'2025-05-30 00:57:28','2025-05-30 00:57:28',0),(26,'网络机房C区日常巡检',8,1,'2025-05-30 03:57:28','PENDING',1,'2025-05-30 00:57:28','2025-05-30 00:57:28',0),(27,'主机房A区早班巡检',6,1,'2025-05-29 22:57:35','COMPLETED',1,'2025-05-30 00:57:35','2025-05-30 00:57:35',0),(28,'配电室B区早班巡检',7,1,'2025-05-29 21:57:35','COMPLETED',1,'2025-05-30 00:57:35','2025-05-30 00:57:35',0),(29,'网络机房C区早班巡检',8,1,'2025-05-29 20:57:35','COMPLETED',1,'2025-05-30 00:57:35','2025-05-30 00:57:35',0),(30,'主机房A区日常巡检',6,1,'2025-05-31 01:11:16','PENDING',1,'2025-05-31 00:11:16','2025-05-31 00:11:16',0),(31,'配电室B区日常巡检',7,1,'2025-05-31 02:11:16','PENDING',1,'2025-05-31 00:11:16','2025-05-31 00:11:16',0),(32,'网络机房C区日常巡检',8,1,'2025-05-31 03:11:16','PENDING',1,'2025-05-31 00:11:16','2025-05-31 00:11:16',0),(33,'主机房A区早班巡检',6,1,'2025-05-30 22:11:16','COMPLETED',1,'2025-05-31 00:11:16','2025-05-31 00:11:16',0),(34,'配电室B区早班巡检',7,1,'2025-05-30 21:11:16','COMPLETED',1,'2025-05-31 00:11:16','2025-05-31 00:11:16',0),(35,'网络机房C区早班巡检',8,1,'2025-05-30 20:11:16','COMPLETED',1,'2025-05-31 00:11:16','2025-05-31 00:11:16',0);
/*!40000 ALTER TABLE `t_inspection_task` ENABLE KEYS */;
UNLOCK TABLES;

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
  `status` varchar(20) NOT NULL DEFAULT 'open' COMMENT '问题状态：open-未处理，processing-处理中，closed-已关闭',
  `recorder_id` bigint NOT NULL COMMENT '记录人ID',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` varchar(500) DEFAULT NULL COMMENT '处理结果',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_issue_no` (`issue_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_issue`
--

LOCK TABLES `t_issue` WRITE;
/*!40000 ALTER TABLE `t_issue` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_issue` ENABLE KEYS */;
UNLOCK TABLES;

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
  `images` text COMMENT '图片（JSON数组存储多张图片URL）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题处理记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_issue_process`
--

LOCK TABLES `t_issue_process` WRITE;
/*!40000 ALTER TABLE `t_issue_process` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_issue_process` ENABLE KEYS */;
UNLOCK TABLES;

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
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_status` (`user_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_notification`
--

LOCK TABLES `t_notification` WRITE;
/*!40000 ALTER TABLE `t_notification` DISABLE KEYS */;
INSERT INTO `t_notification` VALUES (1,1,'新任务通知','您有一个新的巡检任务待处理,','task','unread','2025-05-29 20:18:45','2025-05-29 20:18:45',0),(2,1,'系统维护通知','系统将于今晚22:00进行维护升级','system','unread','2025-05-29 20:18:45','2025-05-29 20:18:45',0),(3,2,'问题处理通知','您提交的问题已被处理','issue','unread','2025-05-29 20:18:45','2025-05-29 20:18:45',0),(4,2,'任务完成提醒','您的巡检任务已超时，请及时处理,','task','unread','2025-05-29 20:18:45','2025-05-29 20:18:45',0),(5,3,'系统更新通知','系统已更新到最新版,','system','unread','2025-05-29 20:18:45','2025-05-29 20:18:45',0),(6,1,'新任务提醒','您有一个新的巡检任务：机房A区日常巡检','task','unread','2025-05-29 20:57:43','2025-05-29 20:57:43',0),(7,1,'任务完成提醒','机房B区周检任务已完成,','task','unread','2025-05-29 20:57:43','2025-05-29 20:57:43',0),(8,1,'系统通知','系统将于今晚22:00进行维护升级','system','unread','2025-05-29 20:57:43','2025-05-29 20:57:43',0);
/*!40000 ALTER TABLE `t_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `department_id` bigint DEFAULT NULL COMMENT '部门ID',
  `role` varchar(20) NOT NULL DEFAULT 'user' COMMENT '角色：admin-管理员，user-普通用户',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '状态：active-活跃，inactive-未激活，locked-已锁定',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'admin','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','系统管理员',1,'admin','active',NULL,NULL,NULL,NULL,'2025-05-21 21:01:26','2025-05-21 21:01:26',0),(2,'zhangsan','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','张三',2,'user','active',NULL,NULL,NULL,NULL,'2025-05-21 21:01:27','2025-05-21 21:01:27',0),(3,'lisi','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','李四',3,'user','active',NULL,NULL,NULL,NULL,'2025-05-21 21:01:27','2025-05-21 21:01:27',0),(4,'wangwu','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','王五',2,'user','active',NULL,NULL,NULL,NULL,'2025-05-21 21:01:27','2025-05-21 21:01:27',0),(5,'zhaoliu','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','赵六',3,'admin','active',NULL,NULL,NULL,NULL,'2025-05-21 21:01:27','2025-05-21 21:01:27',0),(6,'inspector1','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','巡检员1',2,'user','active',NULL,NULL,NULL,NULL,'2025-05-30 00:33:01','2025-05-30 00:33:01',0),(7,'inspector2','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','巡检员2',2,'user','active',NULL,NULL,NULL,NULL,'2025-05-30 00:33:01','2025-05-30 00:33:01',0);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

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
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_permission` (`user_id`,`permission_code`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_permission`
--

LOCK TABLES `t_user_permission` WRITE;
/*!40000 ALTER TABLE `t_user_permission` DISABLE KEYS */;
INSERT INTO `t_user_permission` VALUES (1,1,'dashboard','仪表盘查看权限','2025-05-21 21:01:28','2025-05-21 21:01:28',0),(2,1,'records_view','巡检记录查看权限','2025-05-21 21:01:28','2025-05-21 21:01:28',0),(3,1,'records_all','查看所有巡检记录','2025-05-21 21:01:28','2025-05-21 21:01:28',0),(4,1,'records_export','巡检记录导出权限','2025-05-21 21:01:28','2025-05-21 21:01:28',0),(5,1,'issues_view','问题列表查看权限','2025-05-21 21:01:28','2025-05-21 21:01:28',0),(6,1,'issues_edit','问题处理权限','2025-05-21 21:01:28','2025-05-21 21:01:28',0),(7,1,'user_manage','用户管理权限','2025-05-21 21:01:28','2025-05-21 21:01:28',0),(8,1,'system_config','系统配置权限','2025-05-21 21:01:28','2025-05-21 21:01:28',0);
/*!40000 ALTER TABLE `t_user_permission` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-10  0:35:48 
