DROP TABLE IF EXISTS `bsl_bs_plan_info`;
CREATE TABLE `bsl_bs_plan_info` (
                                    `bs_id` varchar(20) NOT NULL COMMENT '收发编号 通知单号或销售单号',
                                    `bs_type` char(1) NOT NULL COMMENT '收发标志 0-发货通知单 1-销售出库单',
                                    `bs_company` varchar(20) NOT NULL COMMENT '供应商',
                                    `bs_customer` varchar(20) DEFAULT NULL COMMENT '客户',
                                    `bs_hasguarantee` char(1) DEFAULT NULL COMMENT '含质保书 0-否1-是',
                                    `bs_amt` int(5) NOT NULL COMMENT '数量',
                                    `bs_weight` float(10,2) NOT NULL COMMENT '重量/吨',
  `bs_flag` char(1) NOT NULL COMMENT '收发类别 0-自购卷板 1-外接卷板 2-半成品发货 3-成品发货 4-废品发货',
  `bs_transport` varchar(20) DEFAULT NULL COMMENT '运输单位',
  `bs_carno` varchar(20) DEFAULT NULL COMMENT '运输车号',
  `bs_gettype` varchar(20) DEFAULT NULL COMMENT '提货方式',
  `bs_status` char(1) NOT NULL COMMENT '收发状态 0-创建 1-进行中 2-已完成',
  `bs_inputuser` varchar(20) DEFAULT NULL COMMENT '产品录入人',
  `bs_checkuser` varchar(20) DEFAULT NULL COMMENT '产品审批人',
  `crt_date` date DEFAULT NULL COMMENT '创建日期',
  `upd_date` date DEFAULT NULL COMMENT '修改日期',
  `remark` varchar(120) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`bs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收发货明细表';



DROP TABLE IF EXISTS `bsl_change_status_record`;
CREATE TABLE `bsl_change_status_record` (
                                            `change_serno` varchar(20) NOT NULL COMMENT '维护流水号',
                                            `change_type` char(1) NOT NULL COMMENT '维护类别 0-产品状态维护 1-收发货通知单维护 2-生产指令维护 3-废品重量维护',
                                            `change_prod_id` varchar(20) NOT NULL COMMENT '维护产品编号 废品输入编丝料头等信息',
                                            `before_status` varchar(20) NOT NULL COMMENT '废品为维护前重量',
                                            `after_status` varchar(20) NOT NULL COMMENT '废品为维护后重量',
                                            `inputuser` varchar(20) DEFAULT NULL COMMENT '产品录入人',
                                            `crt_date` date NOT NULL COMMENT '创建日期',
                                            `remark` varchar(120) DEFAULT NULL COMMENT '备注',
                                            PRIMARY KEY (`change_serno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='状态强制维护记录表';


DROP TABLE IF EXISTS `bsl_make_plan_info`;
CREATE TABLE `bsl_make_plan_info` (
                                      `plan_id` varchar(20) NOT NULL COMMENT '指令号',
                                      `plan_flag` char(1) NOT NULL COMMENT '指令标志 0-半成品生产指令 1-产品生产指令',
                                      `company` varchar(20) DEFAULT NULL COMMENT '工厂',
                                      `plan_department` varchar(20) DEFAULT NULL COMMENT '生产部门',
                                      `plan_order` varchar(20) NOT NULL COMMENT '半成品对应发货单号 产品对应半成品生产指令号',
                                      `plan_status` char(1) NOT NULL COMMENT '指令状态 0-创建 1-进行中 2-已完成',
                                      `inputuser` varchar(20) NOT NULL COMMENT '录入人',
                                      `checkuser` varchar(20) DEFAULT NULL COMMENT '审批人',
                                      `crt_date` date NOT NULL COMMENT '创建日期',
                                      `remark` varchar(120) DEFAULT NULL COMMENT '备注',
                                      PRIMARY KEY (`plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='生产指令表';



DROP TABLE IF EXISTS `bsl_make_plan_info_detail`;
CREATE TABLE `bsl_make_plan_info_detail` (
                                             `plan_id` varchar(20) NOT NULL COMMENT '指令号',
                                             `product_name` varchar(20) DEFAULT NULL COMMENT '产品名称',
                                             `norm` varchar(20) NOT NULL COMMENT '规格',
                                             `prod_material` varchar(20) NOT NULL COMMENT '产品钢种',
                                             `prod_level` char(1) DEFAULT NULL COMMENT '产品质量等级',
                                             `plan_status` int(11) NOT NULL COMMENT '条数',
                                             `plan_output_volume` varchar(20) DEFAULT NULL COMMENT '计划产出量',
                                             `plan_finist_date` date DEFAULT NULL COMMENT '计划完工日期',
                                             `collected_units` varchar(20) DEFAULT NULL COMMENT '实收机组',
                                             `plan_finist_norm` varchar(20) DEFAULT NULL COMMENT '成品规格',
                                             `prod_inputuser` varchar(20) DEFAULT NULL COMMENT '产品录入人',
                                             `crt_date` date NOT NULL COMMENT '创建日期',
                                             `remark` varchar(120) DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='生产指令详细计划表';



DROP TABLE IF EXISTS `bsl_product_info`;
CREATE TABLE `bsl_product_info` (
                                    `prod_id` varchar(20) NOT NULL COMMENT '产品编号',
                                    `prod_name` varchar(40) DEFAULT NULL COMMENT '产品名称',
                                    `prod_type` char(1) NOT NULL COMMENT '产品类型 0-卷板 1-半成品 2-成品',
                                    `prod_norm` varchar(20) DEFAULT NULL COMMENT '产品规格 如：F200*200*200',
                                    `prod_material` varchar(20) DEFAULT NULL COMMENT '产品钢种',
                                    `prod_length` float(10,2) DEFAULT NULL COMMENT '产品定尺',
  `prod_level` char(1) DEFAULT NULL COMMENT '产品质量等级',
  `prod_record_weight` float(10,2) DEFAULT NULL COMMENT '产品单号记载重量 （半成品，成品为空）',
  `prod_rel_weight` float(10,2) DEFAULT NULL COMMENT '产品实际重量',
  `prod_print_weight` float(10,2) DEFAULT NULL COMMENT '产品打印重量 （半成品，成品 = 产品实际重量）',
  `prod_source_company` varchar(20) DEFAULT NULL COMMENT '产品发货仓库',
  `prod_status` char(1) NOT NULL COMMENT '产品状态 0-创建未入库（只针对卷板） 1-已入库 2-已出库',
  `prod_source` char(20) NOT NULL COMMENT '产品来源 0-自购入库 1-厂家入库 2-剩余入库',
  `prod_plan_no` varchar(20) NOT NULL COMMENT '产品对应批次号 卷板对应发货单号 半成品对应半成品生产批号 成品对应成品生产指令号',
  `prod_ori_id` varchar(20) DEFAULT NULL COMMENT '产品对应编号 卷板专用 厂家卷板号',
  `prod_parent_no` varchar(20) DEFAULT NULL COMMENT '产品对应来源 卷板对应发货单号 半成品对应卷板卷号 成品对应一些半成品盘号',
  `prod_inputuser` varchar(20) DEFAULT NULL COMMENT '产品录入人',
  `prod_checkuser` varchar(20) DEFAULT NULL COMMENT '产品审批人',
  `crt_date` date DEFAULT NULL COMMENT '创建日期',
  `upd_date` date DEFAULT NULL COMMENT '修改日期',
  `remark` varchar(120) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`prod_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品信息表';

DROP TABLE IF EXISTS `bsl_role`;
CREATE TABLE `bsl_role` (
                            `bsl_role_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                            `user_type` char(1) NOT NULL COMMENT '人员角色 0-管理员 1-原料业务员 2-原料库管员 3-生产计划调度员 4-业务员 5-操作员',
                            `no_open_pages` varchar(500) NOT NULL COMMENT '不可以打开的jsp 以|分隔',
                            `remark` varchar(120) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`bsl_role_id`),
                            UNIQUE KEY `u_usertype` (`user_type`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='用户权限表';

insert into bsl_role(user_type,no_open_pages,remark) values('0','','');
insert into bsl_role(user_type,no_open_pages,remark) values('1','M0001','不允许打开M0001.jsp页面');
insert into bsl_role(user_type,no_open_pages,remark) values('2','M0001','不允许打开M0001.jsp页面');
insert into bsl_role(user_type,no_open_pages,remark) values('3','M0001','不允许打开M0001.jsp页面');
insert into bsl_role(user_type,no_open_pages,remark) values('4','M0001','不允许打开M0001.jsp页面');
insert into bsl_role(user_type,no_open_pages,remark) values('5','M0001','不允许打开M0001.jsp页面');


DROP TABLE IF EXISTS `bsl_stock_change_detail`;
CREATE TABLE `bsl_stock_change_detail` (
                                           `trans_serno` varchar(20) NOT NULL COMMENT '交易流水号',
                                           `prod_id` varchar(20) DEFAULT NULL COMMENT '产品编号 非废品时必输',
                                           `plan_serno` varchar(20) NOT NULL COMMENT '指令号',
                                           `trans_code` char(4) NOT NULL COMMENT '交易码 1901-入库 1902-制造出库 1903-出售出库 1904-剩余重新入库 1905-未用退回',
                                           `prod_type` char(1) NOT NULL COMMENT '产品类型 0-废品 1-非废品',
                                           `rubbish_type` char(1) DEFAULT NULL COMMENT '废品类别 0-编丝废品 1-料头废品',
                                           `prod_ori_id` varchar(20) DEFAULT NULL COMMENT '原卷号 剩余重新入库时录入 非必输',
                                           `rubbish_weight` float(10,2) DEFAULT NULL COMMENT '交易重量 废品时必输',

  `price` float(10,2) DEFAULT NULL COMMENT '价格 废品时必输',
  `target_place` varchar(120) DEFAULT NULL COMMENT '去向',
  `inputuser` varchar(20) DEFAULT NULL COMMENT '录入人',
  `crt_date` date DEFAULT NULL COMMENT '创建日期',
  `remark` varchar(120) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`trans_serno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='库存变动明细表';

user bsldb;
DROP TABLE IF EXISTS `bsl_user_info`;
CREATE TABLE `bsl_user_info` (
                                 `user_id` varchar(8) NOT NULL COMMENT '员工编号',
                                 `user_name` varchar(20) NOT NULL COMMENT '姓名',
                                 `user_type` char(1) NOT NULL COMMENT '人员角色 0-管理员 1-原料业务员 2-原料库管员 3-生产计划调度员 4-业务员 5-操作员',
                                 `user_password` varchar(50) NOT NULL COMMENT '登录密码',
                                 `user_tel` varchar(20) DEFAULT NULL COMMENT '电话号码',
                                 `user_status` char(1) NOT NULL DEFAULT '0' COMMENT '用户状态 0-正常，1-停用',
                                 `crt_date` date DEFAULT NULL COMMENT '创建日期',
                                 `remark` varchar(120) DEFAULT NULL COMMENT '备注',
                                 PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into bsl_user_info values('0000000','admin','0','21232F297A57A5A743894A0E4A801FC3','','0',now(),'');


DROP TABLE IF EXISTS `bsl_waste_weight_info`;
CREATE TABLE `bsl_waste_weight_info` (
                                         `waste_type` char(1) NOT NULL COMMENT '废品类别 0-编丝废品 1-料头废品',
                                         `waste_weight` varchar(20) NOT NULL COMMENT '废品重量',
                                         `crt_date` date NOT NULL COMMENT '创建日期',
                                         `remark` varchar(120) DEFAULT NULL COMMENT '备注',
                                         PRIMARY KEY (`waste_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='废品重量统计表';
