create table QRCB_CONFIG.CONFIG_INFO
(
    ID                 BIGINT generated always as identity
        constraint CONFIG_INFO_PK
            primary key,
    DATA_ID            VARCHAR(255)  default ''                not null,
    GROUP_ID           VARCHAR(128)  default ''                not null,
    CONTENT            CLOB,
    MD5                VARCHAR(32),
    GMT_CREATE         TIMESTAMP  not null DEFAULT CURRENT TIMESTAMP,
    GMT_MODIFIED       TIMESTAMP  not null DEFAULT CURRENT TIMESTAMP,
    SRC_USER           CLOB,
    SRC_IP             VARCHAR(50),
    APP_NAME           VARCHAR(128),
    TENANT_ID          VARCHAR(128)  default ''                not null,
    C_DESC             VARCHAR(256),
    C_USE              VARCHAR(64),
    EFFECT             VARCHAR(64),
    TYPE               VARCHAR(64),
    C_SCHEMA           CLOB,
    ENCRYPTED_DATA_KEY CLOB default ''                not null,
    constraint UK_CONFIGINFO_DATAGROUPTENANT
        unique (DATA_ID, GROUP_ID, TENANT_ID)
);

comment on table QRCB_CONFIG.CONFIG_INFO is 'config_info';

comment on column QRCB_CONFIG.CONFIG_INFO.ID is 'id';

comment on column QRCB_CONFIG.CONFIG_INFO.CONTENT is 'content';

comment on column QRCB_CONFIG.CONFIG_INFO.MD5 is 'md5';

comment on column QRCB_CONFIG.CONFIG_INFO.GMT_CREATE is '创建时间';

comment on column QRCB_CONFIG.CONFIG_INFO.GMT_MODIFIED is '修改时间';

comment on column QRCB_CONFIG.CONFIG_INFO.SRC_USER is 'source user';

comment on column QRCB_CONFIG.CONFIG_INFO.SRC_IP is 'source ip';

comment on column QRCB_CONFIG.CONFIG_INFO.TENANT_ID is '租户字段';

comment on column QRCB_CONFIG.CONFIG_INFO.ENCRYPTED_DATA_KEY is '秘钥';

create table QRCB_CONFIG.CONFIG_INFO_AGGR
(
    ID           BIGINT generated always as identity
        constraint CONFIG_INFO_AGGR_PK
            primary key,
    DATA_ID      VARCHAR(255) default ''                not null,
    GROUP_ID     VARCHAR(128) default ''                not null,
    DATUM_ID     VARCHAR(255) default ''                not null,
    CONTENT      CLOB,
    GMT_MODIFIED TIMESTAMP not null DEFAULT CURRENT TIMESTAMP,
    APP_NAME     VARCHAR(128),
    TENANT_ID    VARCHAR(128) default ''                not null,
    constraint UK_CONFIGINFOAGGR_DATAGROUPTENANTDATUM
        unique (DATA_ID, GROUP_ID, TENANT_ID, DATUM_ID)
);

comment on table QRCB_CONFIG.CONFIG_INFO_AGGR is 'config_info_aggr';

comment on column QRCB_CONFIG.CONFIG_INFO_AGGR.ID is 'id';

comment on column QRCB_CONFIG.CONFIG_INFO_AGGR.DATA_ID is 'data_id';

comment on column QRCB_CONFIG.CONFIG_INFO_AGGR.GROUP_ID is 'group_id';

comment on column QRCB_CONFIG.CONFIG_INFO_AGGR.DATUM_ID is 'datum_id';

comment on column QRCB_CONFIG.CONFIG_INFO_AGGR.CONTENT is '内容';

comment on column QRCB_CONFIG.CONFIG_INFO_AGGR.GMT_MODIFIED is '修改时间';

comment on column QRCB_CONFIG.CONFIG_INFO_AGGR.APP_NAME is 'app name';

comment on column QRCB_CONFIG.CONFIG_INFO_AGGR.TENANT_ID is '租户字段';

create table QRCB_CONFIG.CONFIG_INFO_BETA
(
    ID                 BIGINT generated always as identity
        constraint CONFIG_INFO_BETA_PK
            primary key,
    DATA_ID            VARCHAR(255)  default ''                not null,
    GROUP_ID           VARCHAR(128)  default ''                not null,
    APP_NAME           VARCHAR(128)  default NULL,
    CONTENT            CLOB default ''                not null,
    BETA_IPS           VARCHAR(1024) default NULL,
    MD5                VARCHAR(32)   default NULL,
    GMT_CREATE         TIMESTAMP not null DEFAULT CURRENT TIMESTAMP,
    GMT_MODIFIED       TIMESTAMP not null DEFAULT CURRENT TIMESTAMP,
    SRC_USER           CLOB,
    SRC_IP             VARCHAR(50)   default NULL,
    TENANT_ID          VARCHAR(128)  default ''                not null,
    ENCRYPTED_DATA_KEY CLOB default ''                not null,
    constraint UK_CONFIGINFOBETA_DATAGROUPTENANT
        unique (DATA_ID, GROUP_ID, TENANT_ID)
);

comment on table QRCB_CONFIG.CONFIG_INFO_BETA is 'config_info_beta';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.ID is 'id';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.CONTENT is 'content';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.BETA_IPS is 'betaIps';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.MD5 is 'md5';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.GMT_CREATE is '创建时间';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.GMT_MODIFIED is '修改时间';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.SRC_USER is 'source user';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.SRC_IP is 'source ip';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.TENANT_ID is '租户字段';

comment on column QRCB_CONFIG.CONFIG_INFO_BETA.ENCRYPTED_DATA_KEY is '秘钥';

create table QRCB_CONFIG.CONFIG_INFO_TAG
(
    ID           BIGINT generated always as identity
        constraint CONFIG_INFO_TAG_PK
            primary key,
    DATA_ID      VARCHAR(255)  default ''                not null,
    GROUP_ID     VARCHAR(128)  default ''                not null,
    TENANT_ID    VARCHAR(128)  default ''                not null,
    TAG_ID       VARCHAR(128)  default ''                not null,
    APP_NAME     VARCHAR(128)  default NULL,
    CONTENT      CLOB default ''                not null,
    BETA_IPS     VARCHAR(1024) default NULL,
    MD5          VARCHAR(32)   default NULL,
    GMT_CREATE   TIMESTAMP  DEFAULT CURRENT TIMESTAMP not null,
    GMT_MODIFIED TIMESTAMP  DEFAULT CURRENT TIMESTAMP  not null,
    SRC_USER     CLOB,
    SRC_IP       VARCHAR(50)   default NULL,
    constraint UK_CONFIGINFOTAG_DATAGROUPTENANTTAG
        unique (DATA_ID, GROUP_ID, TENANT_ID, TAG_ID)
);

comment on table QRCB_CONFIG.CONFIG_INFO_TAG is 'config_info_tag';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.ID is 'id';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.TENANT_ID is '租户字段';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.TAG_ID is 'tag_id';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.CONTENT is 'content';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.BETA_IPS is 'betaIps';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.MD5 is 'md5';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.GMT_CREATE is '创建时间';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.GMT_MODIFIED is '修改时间';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.SRC_USER is 'source user';

comment on column QRCB_CONFIG.CONFIG_INFO_TAG.SRC_IP is 'source ip';

create table QRCB_CONFIG.CONFIG_TAGS_RELATION
(
    ID        BIGINT       default 0  not null
        constraint CONFIG_TAGS_RELATION_PK
            primary key,
    DATA_ID   VARCHAR(255) default '' not null,
    GROUP_ID  VARCHAR(128) default '' not null,
    TENANT_ID VARCHAR(128) default '' not null,
    TAG_NAME  VARCHAR(128) default '' not null,
    TAG_TYPE  VARCHAR(64)  default '' not null,
    NID       BIGINT generated always as identity,
    constraint UK_CONFIGTAGRELATION_CONFIGIDTAG
        unique (ID, TAG_NAME, TAG_TYPE)
);

create index IDX_TENANT_ID
    on QRCB_CONFIG.CONFIG_TAGS_RELATION (TENANT_ID);

create table QRCB_CONFIG.GROUP_CAPACITY
(
    ID                BIGINT generated always as identity
        constraint GROUP_CAPACITY_PK
            primary key,
    GROUP_ID          VARCHAR(128) default ''                not null
        constraint UK_GROUP_ID
            unique,
    QUOTA             INTEGER      default '0'               not null,
    USAGE             INTEGER      default '0'               not null,
    MAX_SIZE          INTEGER      default '0'               not null,
    MAX_AGGR_COUNT    INTEGER      default '0'               not null,
    MAX_AGGR_SIZE     INTEGER      default '0'               not null,
    MAX_HISTORY_COUNT INTEGER      default '0'               not null,
    GMT_CREATE        TIMESTAMP DEFAULT CURRENT TIMESTAMP not null,
    GMT_MODIFIED      TIMESTAMP DEFAULT CURRENT TIMESTAMP not null
);

comment on table QRCB_CONFIG.GROUP_CAPACITY is '集群、各Group容量信息表';

comment on column QRCB_CONFIG.GROUP_CAPACITY.ID is '主键ID';

comment on column QRCB_CONFIG.GROUP_CAPACITY.GROUP_ID is 'Group ID，空字符表示整个集群';

comment on column QRCB_CONFIG.GROUP_CAPACITY.QUOTA is '配额，0表示使用默认值';

comment on column QRCB_CONFIG.GROUP_CAPACITY.USAGE is '使用量';

comment on column QRCB_CONFIG.GROUP_CAPACITY.MAX_SIZE is '单个配置大小上限，单位为字节，0表示使用默认值';

comment on column QRCB_CONFIG.GROUP_CAPACITY.MAX_AGGR_COUNT is '聚合子配置最大个数，，0表示使用默认值';

comment on column QRCB_CONFIG.GROUP_CAPACITY.MAX_AGGR_SIZE is '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值';

comment on column QRCB_CONFIG.GROUP_CAPACITY.MAX_HISTORY_COUNT is '最大变更历史数量';

comment on column QRCB_CONFIG.GROUP_CAPACITY.GMT_CREATE is '创建时间';

comment on column QRCB_CONFIG.GROUP_CAPACITY.GMT_MODIFIED is '修改时间';

create table QRCB_CONFIG.HIS_CONFIG_INFO
(
    ID                 BIGINT        default 0                 not null,
    NID                BIGINT generated always as identity
        constraint HIS_CONFIG_INFO_PK
            primary key,
    DATA_ID            VARCHAR(255)  default ''                not null,
    GROUP_ID           VARCHAR(128)  default ''                not null,
    APP_NAME           VARCHAR(128),
    CONTENT            CLOB,
    MD5                VARCHAR(32),
    GMT_CREATE         TIMESTAMP DEFAULT CURRENT TIMESTAMP not null,
    GMT_MODIFIED       TIMESTAMP DEFAULT CURRENT TIMESTAMP not null,
    SRC_USER           CLOB,
    SRC_IP             VARCHAR(50),
    OP_TYPE            CHARACTER(10) default NULL,
    TENANT_ID          VARCHAR(128)  default ''                not null,
    ENCRYPTED_DATA_KEY CLOB default ''                not null
);

comment on table QRCB_CONFIG.HIS_CONFIG_INFO is 'his_config_info';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.ID is 'id';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.NID is 'nid';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.CONTENT is 'content';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.MD5 is 'md5';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.GMT_CREATE is '创建时间';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.GMT_MODIFIED is '修改时间';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.SRC_USER is 'source user';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.SRC_IP is 'source ip';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.TENANT_ID is '租户字段';

comment on column QRCB_CONFIG.HIS_CONFIG_INFO.ENCRYPTED_DATA_KEY is '秘钥';

create table QRCB_CONFIG.PERMISSIONS
(
    ROLE     VARCHAR(50)  not null,
    RESOURCE VARCHAR(255) not null,
    ACTION   VARCHAR(8)   not null,
    constraint UK_ROLE_PERMISSION
        unique (ROLE, RESOURCE, ACTION)
);

create table QRCB_CONFIG.ROLES
(
    USERNAME VARCHAR(50) not null,
    ROLE     VARCHAR(50) not null,
    constraint IDX_USER_ROLE
        unique (USERNAME, ROLE)
);

create table QRCB_CONFIG.TENANT_CAPACITY
(
    ID                BIGINT generated always as identity
        constraint TENANT_CAPACITY_PK
            primary key,
    TENANT_ID         VARCHAR(128) default ''                not null
        constraint UK_TENANT_ID
            unique,
    QUOTA             INTEGER      default '0'               not null,
    USAGE             INTEGER      default '0'               not null,
    MAX_SIZE          INTEGER      default '0'               not null,
    MAX_AGGR_COUNT    INTEGER      default '0'               not null,
    MAX_AGGR_SIZE     INTEGER      default '0'               not null,
    MAX_HISTORY_COUNT INTEGER      default '0'               not null,
    GMT_CREATE        TIMESTAMP DEFAULT CURRENT TIMESTAMP not null,
    GMT_MODIFIED      TIMESTAMP DEFAULT CURRENT TIMESTAMP not null
);

comment on table QRCB_CONFIG.TENANT_CAPACITY is '租户容量信息表';

comment on column QRCB_CONFIG.TENANT_CAPACITY.ID is '主键ID';

comment on column QRCB_CONFIG.TENANT_CAPACITY.TENANT_ID is 'Tenant ID';

comment on column QRCB_CONFIG.TENANT_CAPACITY.QUOTA is '配额，0表示使用默认值';

comment on column QRCB_CONFIG.TENANT_CAPACITY.USAGE is '使用量';

comment on column QRCB_CONFIG.TENANT_CAPACITY.MAX_SIZE is '单个配置大小上限，单位为字节，0表示使用默认值';

comment on column QRCB_CONFIG.TENANT_CAPACITY.MAX_AGGR_COUNT is '聚合子配置最大个数';

comment on column QRCB_CONFIG.TENANT_CAPACITY.MAX_AGGR_SIZE is '单个聚合数据的子配置大小上限';

comment on column QRCB_CONFIG.TENANT_CAPACITY.MAX_HISTORY_COUNT is '最大变更历史数量';

comment on column QRCB_CONFIG.TENANT_CAPACITY.GMT_CREATE is '创建时间';

comment on column QRCB_CONFIG.TENANT_CAPACITY.GMT_MODIFIED is '修改时间';

create table QRCB_CONFIG.TENANT_INFO
(
    ID            BIGINT generated always as identity
        constraint TENANT_INFO_PK
            primary key,
    KP            VARCHAR(128)            not null,
    TENANT_ID     VARCHAR(128) default '' not null
        constraint IDX_TENANT_ID
            unique,
    TENANT_NAME   VARCHAR(128) default '',
    TENANT_DESC   VARCHAR(256) default NULL,
    CREATE_SOURCE VARCHAR(32)  default NULL,
    GMT_CREATE    BIGINT                  not null,
    GMT_MODIFIED  BIGINT                  not null,
    constraint UK_TENANT_INFO_KPTENANTID
        unique (KP, TENANT_ID)
);

comment on table QRCN_CONFIG.TENANT_INFO is '租户信息表';

comment on column QRCB_CONFIG.TENANT_INFO.ID is 'id';

comment on column QRCB_CONFIG.TENANT_INFO.KP is 'kp';

comment on column QRCB_CONFIG.TENANT_INFO.TENANT_ID is 'tenant_id';

comment on column QRCB_CONFIG.TENANT_INFO.TENANT_NAME is 'tenant_name';

comment on column QRCB_CONFIG.TENANT_INFO.TENANT_DESC is 'tenant_desc';

comment on column QRCB_CONFIG.TENANT_INFO.CREATE_SOURCE is 'create_source';

comment on column QRCB_CONFIG.TENANT_INFO.GMT_CREATE is '创建时间';

comment on column QRCB_CONFIG.TENANT_INFO.GMT_MODIFIED is '修改时间';

create table QRCB_CONFIG.USERS
(
    USERNAME VARCHAR(50)  not null
        constraint USERS_USERNAME_PK
            primary key,
    PASSWORD VARCHAR(500) not null,
    ENABLED  SMALLINT     not null
);

comment on table QRCB_CONFIG.USERS is '用户表';

comment on column QRCB_CONFIG.USERS.USERNAME is 'username';

comment on column QRCB_CONFIG.USERS.PASSWORD is 'password';

comment on column QRCB_CONFIG.USERS.ENABLED is 'enabled';



---SQL---


insert into QRCB_CONFIG.ROLES(USERNAME, ROLE) VALUES ('nacos','ROLE_ADMIN');

insert into QRCB_CONFIG.USERS(username, password, enabled) VALUES ('nacos','$2a$10$491wqZA5cW309jODtFxOLu3hN0QsFIi5bT2rTYgP4Ggfv5kIgIiwe',1);


