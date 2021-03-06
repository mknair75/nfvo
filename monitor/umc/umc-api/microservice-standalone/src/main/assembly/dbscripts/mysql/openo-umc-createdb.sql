﻿--
--
-- Copyright 2016, CMCC Technologies Co., Ltd.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

/******************drop old database and user***************************/
use mysql;
drop database IF  EXISTS umc;
delete from user where User='umc';
FLUSH PRIVILEGES;

/******************create new database and user***************************/
create database umc CHARACTER SET utf8;

GRANT ALL PRIVILEGES ON umc.* TO 'umc'@'%' IDENTIFIED BY 'umc' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON mysql.* TO 'umc'@'%' IDENTIFIED BY 'umc' WITH GRANT OPTION;

GRANT ALL PRIVILEGES ON umc.* TO 'umc'@'localhost' IDENTIFIED BY 'umc' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON mysql.* TO 'umc'@'localhost' IDENTIFIED BY 'umc' WITH GRANT OPTION;
FLUSH PRIVILEGES; 