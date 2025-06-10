# 巡检系统API接口详细文档索引

## 概述

本文档是巡检系统API接口详细文档的索引页，列出了所有分类文档及其主要内容。API接口按照功能模块和用途进行分类，分别标记为`[APP]`、`[ADMIN]`和`[COMMON]`，以表示接口的使用场景。

## 分类文档列表

以下是API接口详细文档的分类列表：

1. [文件上传相关接口](api-interface-details-file-upload.md)
   - 上传文件 [COMMON]
   - 获取文件列表 [COMMON]
   - 删除文件 [COMMON]
   - 获取文件信息 [COMMON]

2. [系统参数相关接口](api-interface-details-system-param.md)
   - 获取所有系统参数 [ADMIN]
   - 获取指定系统参数 [COMMON]
   - 设置系统参数 [ADMIN]
   - 删除系统参数 [ADMIN]

3. [统计数据相关接口](api-interface-details-statistics.md)
   - 获取每日巡检统计 [COMMON]
   - 获取每周巡检统计 [COMMON]
   - 获取问题统计 [COMMON]
   - 获取仪表盘数据 [ADMIN]
   - 刷新统计缓存 [ADMIN]

4. [用户头像相关接口](api-interface-details-user-avatar.md)
   - 获取用户详情（含头像URL） [COMMON]
   - 获取当前用户信息（含头像URL） [COMMON]
   - 上传用户头像 [COMMON]
   - 获取用户头像 [COMMON]

5. [区域二维码相关接口](api-interface-details-area-qrcode.md)
   - 获取区域详情（含二维码URL） [COMMON]
   - 获取区域列表（含二维码URL） [COMMON]
   - 获取区域二维码 [COMMON]
   - 生成区域二维码 [ADMIN]

6. [巡检记录路径相关接口](api-interface-details-record-route.md)
   - 获取巡检记录详情（含路径数据） [COMMON]
   - 提交巡检路径 [APP]
   - 获取巡检路径 [COMMON]

7. [问题处理图片相关接口](api-interface-details-issue-images.md)
   - 获取问题处理记录（含图片URL数组） [COMMON]
   - 处理问题（可提交图片URL数组） [COMMON]
   - 上传问题处理图片 [COMMON]
   - 获取问题处理图片 [COMMON]
   - 删除问题处理图片 [COMMON]

## 全局响应格式

所有API响应均使用以下统一格式：

```json
{
  "code": 200,          // 状态码：200成功，其他值表示错误
  "message": "success", // 状态信息
  "data": {}            // 响应数据，具体格式因接口而异
}
```

## 全局状态码

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 权限不足 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 用途分类说明

- **[APP]** - 表示该接口主要供移动应用使用
- **[ADMIN]** - 表示该接口主要供管理后台使用
- **[COMMON]** - 表示该接口为移动应用和管理后台共用

## 数据库扩展影响

本文档中的所有接口都基于数据库扩展后的表结构设计，相关表及字段包括：

1. `t_area.qr_code_url` - 区域二维码URL
2. `t_inspection_record.route_path` - 巡检路径（JSON格式）
3. `t_issue_process.images` - 问题处理的多张图片URL（JSON数组）
4. `t_user.avatar` - 用户头像URL
5. `t_statistics_cache` - 统计数据缓存表
6. `t_system_param` - 系统参数表
7. `t_file_upload` - 文件上传表 