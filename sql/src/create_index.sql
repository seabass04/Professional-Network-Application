CREATE INDEX index_usr_usrid ON USR(userId);
/*CREATE INDEX index_usr_password ON USR(password);
CREATE INDEX index_usr_email ON USR(email);
CREATE INDEX index_usr_name ON USR(name);
CREATE INDEX index_usr_dateOfBirth ON USR(dateOfBirth);*/

CREATE INDEX index_work_expr_userid ON WORK_EXPR(userId);
/*CREATE INDEX index_work_expr_company ON WORK_EXPR(company);
CREATE INDEX index_work_expr_role ON WORK_EXPR(role);
CREATE INDEX index_work_expr_location ON WORK_EXPR(location);
CREATE INDEX index_work_expr_startdate ON WORK_EXPR(startDate);
CREATE INDEX index_work_expr_enddate ON WORK_EXPR(endDate);*/

CREATE INDEX index_edu_details_userid ON EDUCATIONAL_DETAILS(userId);
CREATE INDEX index_edu_details_institutionname ON EDUCATIONAL_DETAILS(instituitionName);
CREATE INDEX index_edu_details_major ON EDUCATIONAL_DETAILS(major);
CREATE INDEX index_edu_details_degree ON EDUCATIONAL_DETAILS(degree);
CREATE INDEX index_edu_details_startdate ON EDUCATIONAL_DETAILS(startdate);
CREATE INDEX index_edu_details_enddate ON EDUCATIONAL_DETAILS(enddate);

CREATE INDEX index_message_msgId ON MESSAGE(msgId);
CREATE INDEX index_message_senderid ON MESSAGE(senderId); 
CREATE INDEX index_message_receiverid ON MESSAGE(receiverId); 
CREATE INDEX index_message_contents ON MESSAGE(contents); 
CREATE INDEX index_message_sendtime ON MESSAGE(sendTime); 
CREATE INDEX index_message_deletestatus ON MESSAGE(deleteStatus); 
CREATE INDEX index_message_status ON MESSAGE(status);

CREATE INDEX index_connection_usr_userid ON CONNECTION_USR(userId); 
CREATE INDEX index_connection_usr_connectionid ON CONNECTION_USR(connectionId); 
CREATE INDEX index_connection_usr_status ON CONNECTION_USR(status);
