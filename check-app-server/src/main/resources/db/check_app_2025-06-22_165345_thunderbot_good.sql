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
  `qr_code_url` varchar(255) DEFAULT NULL COMMENT '二维码图片URL',
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
  `module_count` int DEFAULT '0' COMMENT '妯″潡鏁伴噺锛堟暟鎹?腑蹇冧笓鐢?級',
  `config_json` text COMMENT '鍖哄煙閰嶇疆锛圝SON鏍煎紡锛',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检区域表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_area`
--

/*!40000 ALTER TABLE `t_area` DISABLE KEYS */;
INSERT INTO `t_area` VALUES (13,'https://checkapp.pengxinxu.com/qrcode/area/13','A','AREA101','Server Room A','Main server equipment room','Data Center 1F','server','active','2025-06-10 19:39:33','2025-06-10 19:39:33',0,0,NULL),(14,'https://checkapp.pengxinxu.com/qrcode/area/14','B','AREA102','Power Room B','Power distribution room','Data Center B1','power','active','2025-06-10 19:39:33','2025-06-10 19:39:33',0,0,NULL),(15,'https://checkapp.pengxinxu.com/qrcode/dynamic/15?t=1749555573','C','AREA103','Network Room C','Network equipment room','Data Center 2F','network','active','2025-06-10 19:39:33','2025-06-10 19:39:33',0,0,NULL),(16,NULL,'D','DC001','数据中心1','涓绘暟鎹?腑蹇冩満鎴','1鍙锋ゼB1灞','datacenter','active','2025-06-19 21:03:57','2025-06-22 00:47:45',0,4,'{\"modules\": [{\"id\": 1, \"name\": \"计算模块1\", \"type\": \"compute\"}, {\"id\": 2, \"name\": \"计算模块2\", \"type\": \"compute\"}, {\"id\": 3, \"name\": \"存储模块1\", \"type\": \"storage\"}, {\"id\": 4, \"name\": \"网络模块1\", \"type\": \"network\"}]}'),(17,NULL,'D','DC002','数据中心2','澶囩敤鏁版嵁涓?績鏈烘埧','2鍙锋ゼB1灞','datacenter','active','2025-06-19 21:03:57','2025-06-22 00:47:46',0,3,'{\"modules\": [{\"id\": 1, \"name\": \"计算模块1\", \"type\": \"compute\"}, {\"id\": 2, \"name\": \"存储模块1\", \"type\": \"storage\"}, {\"id\": 3, \"name\": \"网络模块1\", \"type\": \"network\"}]}'),(18,NULL,'D','DC003','数据中心3','鐏惧?鏁版嵁涓?績鏈烘埧','3鍙锋ゼB2灞','datacenter','active','2025-06-19 21:03:57','2025-06-22 00:47:48',0,6,'{\"modules\": [{\"id\": 1, \"name\": \"计算模块1\", \"type\": \"compute\"}, {\"id\": 2, \"name\": \"计算模块2\", \"type\": \"compute\"}, {\"id\": 3, \"name\": \"计算模块3\", \"type\": \"compute\"}, {\"id\": 4, \"name\": \"存储模块1\", \"type\": \"storage\"}, {\"id\": 5, \"name\": \"存储模块2\", \"type\": \"storage\"}, {\"id\": 6, \"name\": \"网络模块1\", \"type\": \"network\"}]}'),(19,NULL,'E','WR001','弱电间1','1鍙锋ゼ寮辩數闂','1鍙锋ゼ1灞','weakroom','active','2025-06-19 21:03:57','2025-06-19 21:08:49',0,0,'{}'),(20,NULL,'E','WR002','弱电间2','2鍙锋ゼ寮辩數闂','2鍙锋ゼ1灞','weakroom','active','2025-06-19 21:03:57','2025-06-19 21:08:49',0,0,'{}'),(21,NULL,'E','WR003','弱电间3','3鍙锋ゼ寮辩數闂','3鍙锋ゼ2灞','weakroom','active','2025-06-19 21:03:57','2025-06-19 21:08:49',0,0,'{}'),(22,NULL,'E','WR004','弱电间4','4鍙锋ゼ寮辩數闂','4鍙锋ゼ鍦颁笅1灞','weakroom','active','2025-06-19 21:03:57','2025-06-19 21:08:49',0,0,'{}');
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='区域类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_area_type`
--

/*!40000 ALTER TABLE `t_area_type` DISABLE KEYS */;
INSERT INTO `t_area_type` VALUES (8,'D','数据中心','数据中心机房区域','2025-06-19 21:03:57','2025-06-19 21:07:34'),(9,'E','弱电间','弱电间设备区域','2025-06-19 21:03:57','2025-06-19 21:07:42');
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
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件路径',
  `file_size` bigint NOT NULL COMMENT '文件大小（字节）',
  `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件类型',
  `upload_user_id` bigint NOT NULL COMMENT '上传用户ID',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '业务类型',
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
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_item`
--

/*!40000 ALTER TABLE `t_inspection_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_inspection_item` ENABLE KEYS */;

--
-- Table structure for table `t_inspection_item_template`
--

DROP TABLE IF EXISTS `t_inspection_item_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_inspection_item_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '妯℃澘ID',
  `area_type` char(1) NOT NULL COMMENT '閫傜敤鍖哄煙绫诲瀷锛欴-鏁版嵁涓?績, E-寮辩數闂',
  `item_name` varchar(100) NOT NULL COMMENT '宸℃?椤圭洰鍚嶇О',
  `item_code` varchar(50) NOT NULL COMMENT '宸℃?椤圭洰缂栫爜',
  `item_type` varchar(20) NOT NULL DEFAULT 'boolean' COMMENT '椤圭洰绫诲瀷锛歜oolean-鏄?惁, number-鏁板?, text-鏂囨湰',
  `default_value` varchar(100) DEFAULT NULL COMMENT '榛樿?鍊',
  `is_required` tinyint(1) DEFAULT '1' COMMENT '鏄?惁蹇呭～',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '鏄?惁鍚?敤',
  `sort_order` int DEFAULT '0' COMMENT '鎺掑簭搴忓彿',
  `remark` varchar(500) DEFAULT NULL COMMENT '澶囨敞璇存槑',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
  PRIMARY KEY (`id`),
  KEY `idx_area_type_active` (`area_type`,`is_active`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='宸℃?椤圭洰妯℃澘琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_item_template`
--

/*!40000 ALTER TABLE `t_inspection_item_template` DISABLE KEYS */;
INSERT INTO `t_inspection_item_template` VALUES (1,'D','机房范围内是否有声光报警','sound_light_alarm','boolean',NULL,1,1,1,'检查机房内是否有声光报警设备正常工作','2025-06-19 21:03:57','2025-06-19 21:08:49'),(2,'D','机房内是否有纸箱、泡沫等火载量','fire_load_materials','boolean',NULL,1,1,2,'检查机房内是否存在可燃物品','2025-06-19 21:03:57','2025-06-19 21:08:49'),(3,'D','机房内是否有漏水，天花是否有漏水','water_leakage','boolean',NULL,1,1,3,'检查机房地面、墙壁、天花板是否有漏水现象','2025-06-19 21:03:57','2025-06-19 21:08:49'),(4,'D','机房环境温度','room_temperature','number',NULL,1,1,4,'记录机房当前环境温度（摄氏度）','2025-06-19 21:03:57','2025-06-19 21:08:49'),(5,'D','模块回风温度检查','module_return_temp','text',NULL,0,1,5,'检查各模块回风温度是否正常','2025-06-19 21:03:57','2025-06-19 21:08:49'),(6,'D','模块供电状态检查','module_power_status','text',NULL,0,1,6,'检查各模块供电是否正常','2025-06-19 21:03:57','2025-06-19 21:08:49'),(7,'E','机房地面、墙壁和天花是否漏水','water_leakage_check','boolean',NULL,1,1,1,'检查弱电间地面、墙壁、天花板是否有漏水','2025-06-19 21:03:57','2025-06-19 21:08:49'),(8,'E','机房是否有垃圾纸箱等可燃物','combustible_materials','boolean',NULL,1,1,2,'检查弱电间内是否存在垃圾、纸箱等可燃物品','2025-06-19 21:03:57','2025-06-19 21:08:49'),(9,'E','机房环境温度','room_temperature','number',NULL,1,1,3,'记录弱电间当前环境温度（摄氏度）','2025-06-19 21:03:57','2025-06-19 21:08:49'),(10,'E','市电和UPS双路供电是否正常','dual_power_supply','boolean',NULL,1,1,4,'检查市电和UPS双路供电状态','2025-06-19 21:03:57','2025-06-19 21:08:49'),(11,'E','网络机柜是否正常上锁','network_cabinet_lock','boolean',NULL,1,1,5,'检查网络机柜门锁是否正常','2025-06-19 21:03:57','2025-06-19 21:08:49'),(12,'E','机房门禁是否正常','access_control','boolean',NULL,1,1,6,'检查弱电间门禁系统是否正常工作','2025-06-19 21:03:57','2025-06-19 21:08:49'),(13,'E','机房设备运行是否有异响','equipment_noise','boolean',NULL,1,1,7,'检查设备运行是否有异常声响','2025-06-19 21:03:57','2025-06-19 21:08:49'),(14,'E','机房照明是否正常','lighting_system','boolean',NULL,1,1,8,'检查弱电间照明设备是否正常','2025-06-19 21:03:57','2025-06-19 21:08:49'),(15,'D','机房范围内是否有声光报警','sound_light_alarm','boolean',NULL,1,1,1,'检查机房内是否有声光报警设备正常工作','2025-06-20 19:26:46','2025-06-20 19:26:46'),(16,'D','机房内是否有纸箱、泡沫等火载量','fire_load_materials','boolean',NULL,1,1,2,'检查机房内是否存在可燃物品','2025-06-20 19:26:46','2025-06-20 19:26:46'),(17,'D','机房内是否有漏水，天花是否有漏水','water_leakage','boolean',NULL,1,1,3,'检查机房地面、墙壁、天花板是否有漏水现象','2025-06-20 19:26:46','2025-06-20 19:26:46'),(18,'D','机房环境温度','room_temperature','number',NULL,1,1,4,'记录机房当前环境温度（摄氏度）','2025-06-20 19:26:46','2025-06-20 19:26:46'),(19,'D','模块回风温度检查','module_return_temp','text',NULL,0,1,5,'检查各模块回风温度是否正常','2025-06-20 19:26:46','2025-06-20 19:26:46'),(20,'D','模块供电状态检查','module_power_status','text',NULL,0,1,6,'检查各模块供电是否正常','2025-06-20 19:26:46','2025-06-20 19:26:46'),(21,'E','机房地面、墙壁和天花是否漏水','water_leakage_check','boolean',NULL,1,1,1,'检查弱电间地面、墙壁、天花板是否有漏水','2025-06-20 19:26:47','2025-06-20 19:26:47'),(22,'E','机房是否有垃圾纸箱等可燃物','combustible_materials','boolean',NULL,1,1,2,'检查弱电间内是否存在垃圾、纸箱等可燃物品','2025-06-20 19:26:47','2025-06-20 19:26:47'),(23,'E','机房环境温度','room_temperature','number',NULL,1,1,3,'记录弱电间当前环境温度（摄氏度）','2025-06-20 19:26:47','2025-06-20 19:26:47'),(24,'E','市电和UPS双路供电是否正常','dual_power_supply','boolean',NULL,1,1,4,'检查市电和UPS双路供电状态','2025-06-20 19:26:47','2025-06-20 19:26:47'),(25,'E','网络机柜是否正常上锁','network_cabinet_lock','boolean',NULL,1,1,5,'检查网络机柜门锁是否正常','2025-06-20 19:26:47','2025-06-20 19:26:47'),(26,'E','机房门禁是否正常','access_control','boolean',NULL,1,1,6,'检查弱电间门禁系统是否正常工作','2025-06-20 19:26:47','2025-06-20 19:26:47'),(27,'E','机房设备运行是否有异响','equipment_noise','boolean',NULL,1,1,7,'检查设备运行是否有异常声响','2025-06-20 19:26:47','2025-06-20 19:26:47'),(28,'E','机房照明是否正常','lighting_system','boolean',NULL,1,1,8,'检查弱电间照明设备是否正常','2025-06-20 19:26:47','2025-06-20 19:26:47'),(29,'E','机房地面、墙壁和天花是否漏水','water_leakage_check','boolean',NULL,1,1,1,'检查弱电间地面、墙壁、天花板是否有漏水','2025-06-20 19:27:31','2025-06-20 19:27:31'),(30,'E','机房是否有垃圾纸箱等可燃物','combustible_materials','boolean',NULL,1,1,2,'检查弱电间内是否存在垃圾、纸箱等可燃物品','2025-06-20 19:27:31','2025-06-20 19:27:31'),(31,'E','机房环境温度','room_temperature','number',NULL,1,1,3,'记录弱电间当前环境温度（摄氏度）','2025-06-20 19:27:31','2025-06-20 19:27:31'),(32,'E','市电和UPS双路供电是否正常','dual_power_supply','boolean',NULL,1,1,4,'检查市电和UPS双路供电状态','2025-06-20 19:27:31','2025-06-20 19:27:31'),(33,'E','网络机柜是否正常上锁','network_cabinet_lock','boolean',NULL,1,1,5,'检查网络机柜门锁是否正常','2025-06-20 19:27:31','2025-06-20 19:27:31'),(34,'E','机房门禁是否正常','access_control','boolean',NULL,1,1,6,'检查弱电间门禁系统是否正常工作','2025-06-20 19:27:31','2025-06-20 19:27:31'),(35,'E','机房设备运行是否有异响','equipment_noise','boolean',NULL,1,1,7,'检查设备运行是否有异常声响','2025-06-20 19:27:31','2025-06-20 19:27:31'),(36,'E','机房照明是否正常','lighting_system','boolean',NULL,1,1,8,'检查弱电间照明设备是否正常','2025-06-20 19:27:31','2025-06-20 19:27:31'),(37,'D','机房范围内是否有声光报警','sound_light_alarm','boolean',NULL,1,1,1,'检查机房内是否有声光报警设备正常工作','2025-06-20 19:27:33','2025-06-20 19:27:33'),(38,'D','机房内是否有纸箱、泡沫等火载量','fire_load_materials','boolean',NULL,1,1,2,'检查机房内是否存在可燃物品','2025-06-20 19:27:33','2025-06-20 19:27:33'),(39,'D','机房内是否有漏水，天花是否有漏水','water_leakage','boolean',NULL,1,1,3,'检查机房地面、墙壁、天花板是否有漏水现象','2025-06-20 19:27:33','2025-06-20 19:27:33'),(40,'D','机房环境温度','room_temperature','number',NULL,1,1,4,'记录机房当前环境温度（摄氏度）','2025-06-20 19:27:33','2025-06-20 19:27:33'),(41,'D','模块回风温度检查','module_return_temp','text',NULL,0,1,5,'检查各模块回风温度是否正常','2025-06-20 19:27:33','2025-06-20 19:27:33'),(42,'D','模块供电状态检查','module_power_status','text',NULL,0,1,6,'检查各模块供电是否正常','2025-06-20 19:27:33','2025-06-20 19:27:33');
/*!40000 ALTER TABLE `t_inspection_item_template` ENABLE KEYS */;

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
  `route_data` text COMMENT '巡检路径数据（JSON格式存储）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_no` (`record_no`),
  KEY `idx_area_inspector_time` (`area_id`,`inspector_id`,`start_time`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_record`
--

/*!40000 ALTER TABLE `t_inspection_record` DISABLE KEYS */;
INSERT INTO `t_inspection_record` VALUES (24,'R20250624001',NULL,16,9,'2025-06-24 09:00:00','2025-06-24 09:30:00','normal','数据中心1巡检正常',NULL,'2025-06-22 16:00:37','2025-06-22 16:00:37',0,NULL),(25,'R20250624002',NULL,17,10,'2025-06-24 10:00:00','2025-06-24 10:30:00','abnormal','发现温度异常',NULL,'2025-06-22 16:00:37','2025-06-22 16:00:37',0,NULL),(26,'R20250623001',NULL,18,9,'2025-06-23 14:00:00','2025-06-23 14:30:00','normal','弱电间1巡检正常',NULL,'2025-06-22 16:00:37','2025-06-22 16:00:37',0,NULL),(27,'R20250623002',NULL,19,10,'2025-06-23 15:00:00','2025-06-23 15:30:00','abnormal','发现漏水迹象',NULL,'2025-06-22 16:00:37','2025-06-22 16:00:37',0,NULL),(28,'R20250622001',NULL,20,9,'2025-06-22 09:00:00','2025-06-22 09:30:00','normal','数据中心2巡检正常',NULL,'2025-06-22 16:00:37','2025-06-22 16:00:37',0,NULL);
/*!40000 ALTER TABLE `t_inspection_record` ENABLE KEYS */;

--
-- Table structure for table `t_inspection_record_detail`
--

DROP TABLE IF EXISTS `t_inspection_record_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_inspection_record_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '璇︽儏ID',
  `record_id` bigint NOT NULL COMMENT '宸℃?璁板綍ID',
  `template_id` bigint NOT NULL COMMENT '妯℃澘ID',
  `item_name` varchar(100) NOT NULL COMMENT '宸℃?椤圭洰鍚嶇О',
  `item_code` varchar(50) NOT NULL COMMENT '宸℃?椤圭洰缂栫爜',
  `item_type` varchar(20) NOT NULL COMMENT '椤圭洰绫诲瀷',
  `item_value` varchar(500) DEFAULT NULL COMMENT '宸℃?缁撴灉鍊',
  `is_normal` tinyint(1) DEFAULT NULL COMMENT '鏄?惁姝ｅ父',
  `remark` varchar(500) DEFAULT NULL COMMENT '澶囨敞璇存槑',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='宸℃?璁板綍璇︽儏琛';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_record_detail`
--

/*!40000 ALTER TABLE `t_inspection_record_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_inspection_record_detail` ENABLE KEYS */;

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
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='巡检任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_inspection_task`
--

/*!40000 ALTER TABLE `t_inspection_task` DISABLE KEYS */;
INSERT INTO `t_inspection_task` VALUES (36,'数据中心日常巡检',1,1,'2025-06-21 09:00:00','PENDING',1,'2025-06-21 00:35:43','2025-06-21 00:35:43',0),(37,'弱电间设备检查',2,1,'2025-06-21 14:00:00','PENDING',1,'2025-06-21 00:35:43','2025-06-21 00:35:43',0),(38,'机房环境监测',3,1,'2025-06-21 16:00:00','COMPLETED',1,'2025-06-21 00:35:43','2025-06-21 00:35:43',0),(39,'网络设备巡检',1,1,'2025-06-21 10:30:00','COMPLETED',1,'2025-06-21 00:35:43','2025-06-21 00:35:43',0),(40,'安全检查任务',2,1,'2025-06-21 13:00:00','PENDING',1,'2025-06-21 00:35:43','2025-06-21 00:35:43',0),(41,'昨日数据中心巡检',1,1,'2025-06-20 09:00:00','COMPLETED',1,'2025-06-21 00:35:45','2025-06-21 00:35:45',0),(42,'昨日弱电间检查',2,1,'2025-06-20 14:00:00','COMPLETED',1,'2025-06-21 00:35:45','2025-06-21 00:35:45',0),(43,'数据中心1定期巡检',16,14,'2025-06-23 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0),(44,'数据中心1定期巡检',16,12,'2025-06-26 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0),(45,'数据中心1定期巡检',16,10,'2025-06-28 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0),(46,'数据中心1定期巡检',16,9,'2025-06-27 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0),(47,'数据中心2定期巡检',17,14,'2025-06-23 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0),(48,'数据中心2定期巡检',17,12,'2025-06-25 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0),(49,'数据中心2定期巡检',17,10,'2025-06-28 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0),(50,'数据中心2定期巡检',17,9,'2025-06-22 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0),(51,'数据中心3定期巡检',18,14,'2025-06-27 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0),(52,'数据中心3定期巡检',18,12,'2025-06-28 00:47:50','pending',1,'2025-06-22 00:47:50','2025-06-22 00:47:50',0);
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='问题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_issue`
--

/*!40000 ALTER TABLE `t_issue` DISABLE KEYS */;
INSERT INTO `t_issue` VALUES (1,'ISS20250601001',1,3,'AC temperature fluctuation','environment','closed',8,8,'2025-06-09 19:39:33','Replaced filter and adjusted settings','2025-06-10 19:39:33','2025-06-10 19:39:33',0),(2,'ISS20250601002',2,7,'Power Room B high temperature','environment','processing',8,8,NULL,NULL,'2025-06-10 19:39:33','2025-06-10 19:39:33',0),(3,'I20250624001',1,NULL,'数据中心温度过高，需要检查空调系统','environment','open',9,NULL,NULL,NULL,'2025-06-22 16:06:14','2025-06-22 16:06:14',0),(4,'I20250624002',2,NULL,'弱电间发现漏水迹象，可能影响设备安全','security','processing',10,9,NULL,NULL,'2025-06-22 16:06:14','2025-06-22 16:06:14',0),(5,'I20250624003',3,NULL,'机房设备噪音异常，怀疑风扇故障','device','open',9,NULL,NULL,NULL,'2025-06-22 16:06:14','2025-06-22 16:06:14',0),(6,'I20250623001',4,NULL,'网络设备指示灯异常闪烁','device','closed',10,9,'2025-06-23 16:00:00','已重启设备，恢复正常','2025-06-22 16:06:14','2025-06-22 16:06:14',0),(7,'I20250623002',5,NULL,'机房湿度偏高，需要调整除湿设备','environment','processing',9,10,NULL,NULL,'2025-06-22 16:06:14','2025-06-22 16:06:14',0);
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
  `cache_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '缓存键',
  `cache_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '缓存数据（JSON格式）',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cache_key` (`cache_key`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计数据缓存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_statistics_cache`
--

/*!40000 ALTER TABLE `t_statistics_cache` DISABLE KEYS */;
INSERT INTO `t_statistics_cache` VALUES (1,'inspection_count_daily','{\"date\": \"2025-06-10\", \"total\": 6, \"normal\": 5, \"by_area\": [{\"count\": 2, \"area_id\": 13, \"area_name\": \"Server Room A\"}, {\"count\": 2, \"area_id\": 14, \"area_name\": \"Power Room B\"}, {\"count\": 2, \"area_id\": 15, \"area_name\": \"Network Room C\"}], \"abnormal\": 1}','2025-06-11 19:39:33','2025-06-10 19:39:33','2025-06-10 19:39:33'),(2,'inspection_count_weekly','{\"total\": 24, \"by_day\": [{\"date\": \"2025-06-09\", \"count\": 3}, {\"date\": \"2025-06-10\", \"count\": 4}, {\"date\": \"2025-06-11\", \"count\": 3}, {\"date\": \"2025-06-12\", \"count\": 5}, {\"date\": \"2025-06-13\", \"count\": 3}, {\"date\": \"2025-06-14\", \"count\": 3}, {\"date\": \"2025-06-15\", \"count\": 3}], \"normal\": 20, \"abnormal\": 4, \"end_date\": \"2025-06-15\", \"start_date\": \"2025-06-09\"}','2025-06-17 19:39:33','2025-06-10 19:39:33','2025-06-10 19:39:33'),(3,'issue_statistics','{\"total\":2,\"processing\":1,\"closed\":1,\"by_type\":[{\"count\":2,\"type\":\"environment\"}],\"open\":0}','2025-06-23 14:12:18','2025-06-10 19:39:33','2025-06-22 14:12:17'),(4,'dashboard_statistics','{\"records\":{\"total\":2,\"lastMonth\":0,\"thisMonth\":2},\"areas\":{\"total\":10,\"inactive\":0,\"active\":10},\"issues\":{\"total\":2,\"processing\":1,\"closed\":1,\"open\":0},\"tasks\":{\"total\":17,\"inProgress\":0,\"pending\":10,\"completed\":0},\"users\":{\"total\":7,\"inactive\":0,\"active\":7}}','2025-06-22 16:28:58','2025-06-10 19:39:33','2025-06-22 15:28:58'),(7,'dashboard_data','{\"tasks\":{\"total\":15,\"active\":8},\"records\":{\"thisMonth\":25,\"thisWeek\":8},\"issues\":{\"total\":12,\"open\":3,\"processing\":2,\"closed\":7},\"users\":{\"total\":5,\"active\":3}}','2025-06-22 17:00:47','2025-06-22 16:00:47','2025-06-22 16:00:47');
/*!40000 ALTER TABLE `t_statistics_cache` ENABLE KEYS */;

--
-- Table structure for table `t_system_param`
--

DROP TABLE IF EXISTS `t_system_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_system_param` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `param_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数键',
  `param_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数值',
  `param_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参数描述',
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
  `ad_username` varchar(100) DEFAULT NULL COMMENT 'AD域账号',
  `is_ad_user` tinyint DEFAULT '0' COMMENT '是否AD用户：0-本地用户，1-AD用户',
  `ad_sync_time` datetime DEFAULT NULL COMMENT 'AD同步时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_ad_username` (`ad_username`),
  KEY `idx_is_ad_user` (`is_ad_user`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (8,'admin','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','Administrator',1,'admin','active',NULL,NULL,'https://checkapp.pengxinxu.com/avatars/default-8.png',NULL,'2025-06-10 19:30:15','2025-06-10 19:30:15',0,NULL,0,NULL),(9,'zhang.san','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','张三',NULL,'inspector','active','13800138001','zhang.san@company.com',NULL,NULL,'2025-06-22 00:47:38','2025-06-22 00:47:38',0,'zhang.san',1,'2025-06-22 00:47:38'),(10,'li.si','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','李四',NULL,'inspector','active','13800138002','li.si@company.com',NULL,NULL,'2025-06-22 00:47:38','2025-06-22 00:47:38',0,'li.si',1,'2025-06-22 00:47:38'),(11,'wang.wu','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','王五',NULL,'admin','active','13800138003','wang.wu@company.com',NULL,NULL,'2025-06-22 00:47:38','2025-06-22 00:47:38',0,'wang.wu',1,'2025-06-22 00:47:38'),(12,'chen.liu','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','陈六',NULL,'inspector','active','13800138004','chen.liu@company.com',NULL,NULL,'2025-06-22 00:47:38','2025-06-22 00:47:38',0,'chen.liu',1,'2025-06-22 00:47:38'),(13,'zhao.qi','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','赵七',NULL,'user','active','13800138005','zhao.qi@company.com',NULL,NULL,'2025-06-22 00:47:38','2025-06-22 00:47:38',0,'zhao.qi',1,'2025-06-22 00:47:38'),(14,'sun.ba','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','孙八',NULL,'inspector','active','13800138006','sun.ba@company.com',NULL,NULL,'2025-06-22 00:47:38','2025-06-22 00:47:38',0,'sun.ba',1,'2025-06-22 00:47:38');
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_permission`
--

/*!40000 ALTER TABLE `t_user_permission` DISABLE KEYS */;
INSERT INTO `t_user_permission` VALUES (9,9,'INSPECTION_EXECUTE','执行巡检任务','2025-06-22 00:47:40','2025-06-22 00:47:40',0),(10,10,'INSPECTION_EXECUTE','执行巡检任务','2025-06-22 00:47:40','2025-06-22 00:47:40',0),(11,12,'INSPECTION_EXECUTE','执行巡检任务','2025-06-22 00:47:40','2025-06-22 00:47:40',0),(12,14,'INSPECTION_EXECUTE','执行巡检任务','2025-06-22 00:47:40','2025-06-22 00:47:40',0),(16,11,'ADMIN_MANAGE','管理员权限','2025-06-22 00:47:42','2025-06-22 00:47:42',0);
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

-- Dump completed on 2025-06-22 16:54:07
