DROP PROCEDURE IF EXISTS `change_user_password`;
DELIMITER $$
CREATE PROCEDURE `change_user_password`(in usr_id int, in usr_password varchar(255))
BEGIN
update LML_USER set PWD_ = usr_password where ID_ = usr_id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `get_user_info`;
DELIMITER $$
CREATE PROCEDURE `get_user_info`(in usr_name varchar(255))
BEGIN
select * from LML_USER where USER_NAME_ = usr_name;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `insert_new_user`;
DELIMITER $$
CREATE PROCEDURE `insert_new_user`(in usr_name varchar(255), in usr_id varchar(64), in usr_password varchar(255))
BEGIN
insert into LML_USER values (usr_id, usr_name, usr_password);
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `insert_user_session`;
DELIMITER $$
CREATE PROCEDURE `insert_user_session`(in token varchar(255), in usr_id varchar(64),  session_timestamp timestamp)
BEGIN
insert into LML_USER_SESSION values (token, usr_id, session_timestamp);
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `set_user_session_exipiration`;
DELIMITER $$
CREATE PROCEDURE `set_user_session_exipiration`(in token varchar(255), in user_id varchar(64), in session_timestamp timestamp)
BEGIN
update LML_USER_SESSION set LML_USER_SESSION.EXP_TIME_ = session_timestamp where LML_USER_SESSION.USER_TOKEN_ = token and LML_USER_SESSION.ID_ = user_id;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `verify_user`;
DELIMITER $$
CREATE PROCEDURE `verify_user`(in user_name varchar(255), in user_password varchar(255), out out_user_id varchar(64))
BEGIN
select LML_USER.ID_ into out_user_id from LML_USER where LML_USER.USER_NAME_ = user_name and LML_USER.PWD_ = user_password;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `verify_user_session`;
DELIMITER $$
CREATE PROCEDURE `verify_user_session`(in token varchar(255), inout inout_user_id varchar(64))
BEGIN
IF EXISTS (select 1 from LML_USER_SESSION where LML_USER_SESSION.ID_ = inout_user_id and LML_USER_SESSION.USER_TOKEN_ = token and LML_USER_SESSION.EXP_TIME_ > UTC_TIMESTAMP())
THEN
BEGIN
select LML_USER_SESSION.ID_ into inout_user_id from LML_USER_SESSION where LML_USER_SESSION.ID_ = inout_user_id and LML_USER_SESSION.USER_TOKEN_ = token and LML_USER_SESSION.EXP_TIME_ > UTC_TIMESTAMP();
END;
ELSE
BEGIN
select null into inout_user_id;
END;
END IF;
END$$
DELIMITER ;
