COPY USR(
	userID,
	password,
	email,
	name,
	dateOfBirth
)
FROM 'USR.csv'
DELIMITER ',' CSV HEADER;
