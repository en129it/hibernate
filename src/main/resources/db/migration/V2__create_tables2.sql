create table TXNLOCK (
	TXN_ID INT,
	USER_ID INT,
	START_TIMESTAMP TIMESTAMP,
	ACTION TEXT(20)
);

create table TRANSACTION (
	ID INT,
	TYPE TEXT(50)
);
