create table QRCB_CODEGEN.GEN_DATASOURCE_CONF
(
    ID          INTEGER generated always as identity
        constraint GEN_DATASOURCE_CONF_PK
            primary key,
    NAME        VARCHAR(64),
    URL         VARCHAR(255),
    USERNAME    VARCHAR(64),
    PASSWORD    VARCHAR(64),
    CREATE_DATE TIMESTAMP default CURRENT TIMESTAMP not null,
    UPDATE_DATE TIMESTAMP not null generated by default for each row on update as row change timestamp,
    DEL_FLAG    CHARACTER(1) default '0'               not null,
    TENANT_ID   INTEGER,
    DS_TYPE     VARCHAR(64),
    CONF_TYPE   CHARACTER(1),
    DS_NAME     VARCHAR(64),
    INSTANCE    VARCHAR(64),
    PORT        INTEGER,
    HOST        VARCHAR(128),
    SCHEMA      VARCHAR(32)
);

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.SCHEMA is '数据库schema';

comment on table QRCB_CODEGEN.GEN_DATASOURCE_CONF is '数据源表';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.ID is '主键';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.NAME is '别名';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.URL is 'jdbcUrl';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.USERNAME is '用户名';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.PASSWORD is '密码';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.CREATE_DATE is '创建时间';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.UPDATE_DATE is '更新时间';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.DEL_FLAG is '删除标记';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.TENANT_ID is '租户ID';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.DS_TYPE is '数据库类型';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.CONF_TYPE is '配置类型';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.DS_NAME is '数据库名称';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.INSTANCE is '实例';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.PORT is '端口';

comment on column QRCB_CODEGEN.GEN_DATASOURCE_CONF.HOST is '主机';

--考虑动态多数据源name的唯一性
create unique index GEN_DATASOURCE_CONF_IDX_NAME
on QRCB_CODEGEN.GEN_DATASOURCE_CONF(name);

create table QRCB_CODEGEN.GEN_FORM_CONF
(
 ID INTEGER not null generated always as identity constraint GEN_FORM_CONF_PK primary key ,
 TABLE_NAME varchar(64) default null,
 FORM_INFO CLOB not null ,
 CREATE_TIME timestamp not null default CURRENT_TIMESTAMP ,
 UPDATE_TIME timestamp not null generated by default for each row on update as row change timestamp ,
 DEL_FLAG character(1) default '0' not null ,
 TENANT_ID integer default null
);

comment on table QRCB_CODEGEN.GEN_FORM_CONF is '表单配置';

comment on column QRCB_CODEGEN.GEN_FORM_CONF.ID is 'ID';

comment on column QRCB_CODEGEN.GEN_FORM_CONF.TABLE_NAME is '表名称';

comment on column QRCB_CODEGEN.GEN_FORM_CONF.FORM_INFO is '表单信息';

comment on column QRCB_CODEGEN.GEN_FORM_CONF.CREATE_TIME is '创建时间';

comment on column QRCB_CODEGEN.GEN_FORM_CONF.UPDATE_TIME is '更新时间';

comment on column QRCB_CODEGEN.GEN_FORM_CONF.DEL_FLAG is '删除标记';

comment on column QRCB_CODEGEN.GEN_FORM_CONF.TENANT_ID is '所属租户';

create index GEN_FORM_CONF_IDX_TABLE_NAME
on QRCB_CODEGEN.GEN_FORM_CONF(TABLE_NAME)

