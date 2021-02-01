SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  `DEPARTMENT_CODE` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门代码',
  `DEPARTMENT_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `PARENT_ID` bigint(11) NULL DEFAULT NULL COMMENT '上级部门ID',
  `LEVEL` smallint(1) NULL DEFAULT NULL COMMENT '部门等级',
  `SORT` decimal(35, 30) UNSIGNED NULL DEFAULT NULL COMMENT '部门排序号',
  `CREATE_TIME` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '部门创建时间',
  `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '部门修改时间',
  `CAN_MODIFY` bit(1) NULL DEFAULT b'1' COMMENT '是否可以修改',
  `COMMENT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '部门备注',
  `ENABLE` bit(1) NULL DEFAULT b'1' COMMENT '是否启用',
  `DELETE` bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `idx_parentid_sort`(`PARENT_ID`, `SORT`) USING BTREE,
  INDEX `idx_d_code`(`DEPARTMENT_CODE`) USING BTREE,
  INDEX `idx_d_name`(`DEPARTMENT_NAME`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8784608430471836108 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
                           `ID` bigint(11) NOT NULL,
                           `MEMBER_CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工号',
                           `MEMBER_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
                           `GENDER` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
                           `AGE` tinyint(4) NOT NULL COMMENT '年龄',
                           `ENTRY_DATE` datetime NOT NULL COMMENT '入职时间',
                           `PHONE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
                           `DEPARTMENT_ID` bigint(11) NULL DEFAULT NULL COMMENT '所属部门ID',
                           `SORT` smallint(6) NULL DEFAULT NULL COMMENT '排序号',
                           `COMMENT` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                           `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
                           `MODIFY_TIME` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                           `ENABLE` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用',
                           PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
