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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检区域表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_area`
--

/*!40000 ALTER TABLE `t_area` DISABLE KEYS */;
INSERT INTO `t_area` VALUES (13,'A101','https://checkapp.pengxinxu.com/qrcode/area/13','Server Room A','A','AREA101','Server Room A','Main server equipment room','Data Center 1F','server','active','2025-06-10 19:39:33','2025-06-10 19:39:33',0),(14,'B102','https://checkapp.pengxinxu.com/qrcode/area/14','Power Room B','B','AREA102','Power Room B','Power distribution room','Data Center B1','power','active','2025-06-10 19:39:33','2025-06-10 19:39:33',0),(15,'C103','https://checkapp.pengxinxu.com/qrcode/dynamic/15?t=1749555573','Network Room C','C','AREA103','Network Room C','Network equipment room','Data Center 2F','network','active','2025-06-10 19:39:33','2025-06-10 19:39:33',0);
/*!40000 ALTER TABLE `t_area` ENABLE KEYS */;

--
-- Table structure for table `t_area_check_item`
--

DROP TABLE IF EXISTS `t_area_check_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_area_check_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `area_id` bigint NOT NULL COMMENT '区域ID',
  `name` varchar(100) NOT NULL COMMENT '巡检项名称',
  `type` varchar(50) NOT NULL COMMENT '巡检项类型：environment-环境巡检项，security-安全巡检项，device-设备巡检项',
  `sort` int DEFAULT '0' COMMENT '排序',
  `required` tinyint DEFAULT '1' COMMENT '是否必填：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='区域巡检项模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_area_check_item`
--

/*!40000 ALTER TABLE `t_area_check_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_area_check_item` ENABLE KEYS */;

--
-- Table structure for table `t_area_type`
--

DROP TABLE IF EXISTS `t_area_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_area_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type_code` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型编码',
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型描述',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_code` (`type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='区域类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_area_type`
--

/*!40000 ALTER TABLE `t_area_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_area_type` ENABLE KEYS */;

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
  `status` int DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_department`
--

/*!40000 ALTER TABLE `t_department` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_department` ENABLE KEYS */;

--
-- Table structure for table `t_file_upload`
--

DROP TABLE IF EXISTS `t_file_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_file_upload` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `file_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名',
  `file_path` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件路径',
  `file_size` bigint NOT NULL COMMENT '文件大小（字节）',
  `file_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件类型',
  `upload_user_id` bigint NOT NULL COMMENT '上传用户ID',
  `business_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '业务类型',
  `business_id` bigint DEFAULT NULL COMMENT '业务ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_business` (`business_type`,`business_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件上传表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_file_upload`
--

/*!40000 ALTER TABLE `t_file_upload` DISABLE KEYS */;
INSERT INTO `t_file_upload` VALUES (1,'manual.pdf','/data/uploads/2025/05/manual.pdf',2458621,'application/pdf',8,'SYSTEM',NULL,'2025-06-10 19:39:33'),(2,'equipment.pdf','/data/uploads/2025/05/equipment.pdf',1536842,'application/pdf',8,'SYSTEM',NULL,'2025-06-10 19:39:33'),(3,'record1.jpg','/data/uploads/2025/05/record1.jpg',245862,'image/jpeg',8,'RECORD',1,'2025-06-10 19:39:33'),(4,'record2.jpg','/data/uploads/2025/05/record2.jpg',235461,'image/jpeg',8,'RECORD',2,'2025-06-10 19:39:33'),(5,'device1.png','/data/uploads/2025/05/device1.png',156324,'image/png',8,'DEVICE',1,'2025-06-10 19:39:33'),(6,'device2.png','/data/uploads/2025/05/device2.png',142536,'image/png',8,'DEVICE',2,'2025-06-10 19:39:33'),(7,'large_blueprint.dwg','/data/uploads/2025/06/blueprint.dwg',8542761,'application/acad',8,'AREA',13,'2025-06-10 19:39:33'),(8,'empty_test.txt','/data/uploads/2025/06/empty.txt',0,'text/plain',8,'TEST',NULL,'2025-06-10 19:39:33'),(9,'max_size_file.zip','/data/uploads/2025/06/max_size.zip',10485759,'application/zip',8,'BACKUP',NULL,'2025-06-10 19:39:33');
/*!40000 ALTER TABLE `t_file_upload` ENABLE KEYS */;

--
-- Table structure for table `t_inspection_item`
--

DROP TABLE IF EXISTS `t_inspection_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_inspection_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '巡检项ID',
  `record_id` bigint NOT NULL COMMENT '巡检记录ID',
  `name` varchar(100) NOT NULL COMMENT '巡检项名称',
  `type` varchar(50) NOT NULL COMMENT '巡检项类型：environment-环境巡检项，security-安全巡检项，device-设备巡检项',
  `status` varchar(20) NOT NULL DEFAULT 'normal' COMMENT '巡检项状态：normal-正常，abnormal-异常',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_item`
--

/*!40000 ALTER TABLE `t_inspection_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_inspection_item` ENABLE KEYS */;

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
  UNIQUE KEY `uk_record_no` (`record_no`),
  KEY `idx_area_inspector_time` (`area_id`,`inspector_id`,`start_time`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_record`
--

/*!40000 ALTER TABLE `t_inspection_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_inspection_record` ENABLE KEYS */;

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
  PRIMARY KEY (`id`),
  KEY `idx_area_inspector_time` (`area_id`,`inspector_id`,`plan_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_task`
--

/*!40000 ALTER TABLE `t_inspection_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_inspection_task` ENABLE KEYS */;

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
  UNIQUE KEY `uk_issue_no` (`issue_no`),
  KEY `idx_record_type_status` (`record_id`,`type`,`status`),
  KEY `idx_handler_id` (`handler_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_issue`
--

/*!40000 ALTER TABLE `t_issue` DISABLE KEYS */;
INSERT INTO `t_issue` VALUES (1,'ISS20250601001',1,3,'AC temperature fluctuation','environment','closed',8,8,'2025-06-09 19:39:33','Replaced filter and adjusted settings','2025-06-10 19:39:33','2025-06-10 19:39:33',0),(2,'ISS20250601002',2,7,'Power Room B high temperature','environment','processing',8,8,NULL,NULL,'2025-06-10 19:39:33','2025-06-10 19:39:33',0);
/*!40000 ALTER TABLE `t_issue` ENABLE KEYS */;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题处理记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_issue_process`
--

/*!40000 ALTER TABLE `t_issue_process` DISABLE KEYS */;
INSERT INTO `t_issue_process` VALUES (1,1,'create',8,'2025-06-07 19:39:33','Found AC temperature fluctuation','[\"/data/uploads/2025/06/issue1_1.jpg\", \"/data/uploads/2025/06/issue1_2.jpg\"]','2025-06-10 19:39:33','2025-06-10 19:39:33'),(2,1,'process',8,'2025-06-08 19:39:33','Checked AC system, found clogged filter','[\"/data/uploads/2025/06/issue1_3.jpg\"]','2025-06-10 19:39:33','2025-06-10 19:39:33'),(3,1,'close',8,'2025-06-09 19:39:33','Replaced AC filter, adjusted temp controls, issue resolved','[\"/data/uploads/2025/06/issue1_4.jpg\", \"/data/uploads/2025/06/issue1_5.jpg\"]','2025-06-10 19:39:33','2025-06-10 19:39:33'),(4,2,'create',8,'2025-06-10 19:39:33','Power Room B high temperature, need to check cooling','[\"/data/uploads/2025/06/issue2_1.jpg\"]','2025-06-10 19:39:33','2025-06-10 19:39:33'),(5,2,'process',8,'2025-06-10 19:39:33','Initial check, likely ventilation issue','[\"/data/uploads/2025/06/issue2_2.jpg\"]','2025-06-10 19:39:33','2025-06-10 19:39:33'),(6,2,'document',8,'2025-06-10 19:39:33','Added issue documentation and HD images','[\"/data/uploads/2025/06/issue_doc_1.pdf\", \"/data/uploads/2025/06/issue_hd_image_1.png\"]','2025-06-10 19:39:33','2025-06-10 19:39:33');
/*!40000 ALTER TABLE `t_issue_process` ENABLE KEYS */;

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
  KEY `idx_user_status` (`user_id`,`status`),
  KEY `idx_user_type_status` (`user_id`,`type`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_notification`
--

/*!40000 ALTER TABLE `t_notification` DISABLE KEYS */;
INSERT INTO `t_notification` VALUES (9,8,'System Upgrade Notice','System will upgrade to v1.1.0 on June 15, 2025','system','unread','2025-06-10 19:39:33','2025-06-10 19:39:33',0),(10,8,'Issue Processing Reminder','You have an issue to process: Power Room B high temperature','issue','unread','2025-06-10 19:39:33','2025-06-10 19:39:33',0),(11,8,'Task Reminder','You have 3 inspection tasks pending','task','unread','2025-06-10 19:39:33','2025-06-10 19:39:33',0);
/*!40000 ALTER TABLE `t_notification` ENABLE KEYS */;

--
-- Table structure for table `t_statistics_cache`
--

DROP TABLE IF EXISTS `t_statistics_cache`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_statistics_cache` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `cache_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '缓存键',
  `cache_data` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '缓存数据（JSON格式）',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cache_key` (`cache_key`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计数据缓存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_statistics_cache`
--

/*!40000 ALTER TABLE `t_statistics_cache` DISABLE KEYS */;
INSERT INTO `t_statistics_cache` VALUES (1,'inspection_count_daily','{\"date\": \"2025-06-10\", \"total\": 6, \"normal\": 5, \"by_area\": [{\"count\": 2, \"area_id\": 13, \"area_name\": \"Server Room A\"}, {\"count\": 2, \"area_id\": 14, \"area_name\": \"Power Room B\"}, {\"count\": 2, \"area_id\": 15, \"area_name\": \"Network Room C\"}], \"abnormal\": 1}','2025-06-11 19:39:33','2025-06-10 19:39:33','2025-06-10 19:39:33'),(2,'inspection_count_weekly','{\"total\": 24, \"by_day\": [{\"date\": \"2025-06-09\", \"count\": 3}, {\"date\": \"2025-06-10\", \"count\": 4}, {\"date\": \"2025-06-11\", \"count\": 3}, {\"date\": \"2025-06-12\", \"count\": 5}, {\"date\": \"2025-06-13\", \"count\": 3}, {\"date\": \"2025-06-14\", \"count\": 3}, {\"date\": \"2025-06-15\", \"count\": 3}], \"normal\": 20, \"abnormal\": 4, \"end_date\": \"2025-06-15\", \"start_date\": \"2025-06-09\"}','2025-06-17 19:39:33','2025-06-10 19:39:33','2025-06-10 19:39:33'),(3,'issue_statistics','{\"open\": 3, \"total\": 10, \"closed\": 3, \"by_type\": [{\"type\": \"environment\", \"count\": 4}, {\"type\": \"security\", \"count\": 3}, {\"type\": \"device\", \"count\": 2}, {\"type\": \"other\", \"count\": 1}], \"processing\": 4}','2025-06-11 19:39:33','2025-06-10 19:39:33','2025-06-10 19:39:33'),(4,'dashboard_statistics','{\"areas\": {\"total\": 12, \"active\": 10, \"inactive\": 2}, \"tasks\": {\"total\": 56, \"pending\": 10, \"completed\": 41, \"inProgress\": 5}, \"users\": {\"total\": 35, \"active\": 30, \"inactive\": 5}, \"issues\": {\"open\": 5, \"total\": 25, \"closed\": 12, \"processing\": 8}, \"records\": {\"total\": 120, \"lastMonth\": 32, \"thisMonth\": 24}}','2025-06-11 19:39:33','2025-06-10 19:39:33','2025-06-10 19:39:33');
/*!40000 ALTER TABLE `t_statistics_cache` ENABLE KEYS */;

--
-- Table structure for table `t_system_param`
--

DROP TABLE IF EXISTS `t_system_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_system_param` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `param_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数键',
  `param_value` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数值',
  `param_desc` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参数描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_param_key` (`param_key`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_system_param`
--

/*!40000 ALTER TABLE `t_system_param` DISABLE KEYS */;
INSERT INTO `t_system_param` VALUES (1,'system.name','Inspection System','System Name','2025-06-10 19:39:33','2025-06-10 19:39:33'),(2,'system.version','v1.0.7','System Version','2025-06-10 19:39:33','2025-06-10 19:39:33'),(3,'task.auto_assign','true','Auto Assign Tasks','2025-06-10 19:39:33','2025-06-10 19:39:33'),(4,'task.reminder_minutes','30','Task Reminder Minutes','2025-06-10 19:39:33','2025-06-10 19:39:33'),(5,'notification.enable','true','Enable Notifications','2025-06-10 19:39:33','2025-06-10 19:39:33'),(6,'file.upload.path','/data/uploads','File Upload Path','2025-06-10 19:39:33','2025-06-10 19:39:33'),(7,'file.allowed_types','jpg,jpeg,png,pdf,doc,docx','Allowed File Types','2025-06-10 19:39:33','2025-06-10 19:39:33'),(8,'file.max_size','10485760','Max File Size (bytes)','2025-06-10 19:39:33','2025-06-10 19:39:33'),(9,'system.maintenance_mode','false','System Maintenance Mode','2025-06-10 19:39:33','2025-06-10 19:39:33'),(10,'system.login_attempts','5','Max Login Attempts','2025-06-10 19:39:33','2025-06-10 19:39:33'),(11,'system.password_expiry_days','90','Password Expiry Days','2025-06-10 19:39:33','2025-06-10 19:39:33'),(12,'report.export_format','pdf,excel,csv','Report Export Formats','2025-06-10 19:39:33','2025-06-10 19:39:33'),(13,'map.default_center','30.6598,104.0654','Map Default Center','2025-06-10 19:39:33','2025-06-10 19:39:33');
/*!40000 ALTER TABLE `t_system_param` ENABLE KEYS */;

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
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (8,'admin','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','Administrator',1,'admin','active',NULL,NULL,'https://checkapp.pengxinxu.com/avatars/default-8.png',NULL,'2025-06-10 19:30:15','2025-06-10 19:30:15',0);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;

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

/*!40000 ALTER TABLE `t_user_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_user_permission` ENABLE KEYS */;

--
-- Dumping routines for database 'check_app'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-12 23:29:43
