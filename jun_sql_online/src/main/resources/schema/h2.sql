DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) DEFAULT NULL,
                            `department_id` int(11) DEFAULT NULL,
                            `create_time` datetime DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'lijz', 1, NULL);
INSERT INTO `sys_user` VALUES (2, 'lucy', 1, NULL);
INSERT INTO `sys_user` VALUES (3, 'bear', 2, NULL);

COMMIT;

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `name` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ;

INSERT INTO `department` VALUES (1, '部门1');
INSERT INTO `department` VALUES (2, '部门2');