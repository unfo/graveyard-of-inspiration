create table Compo (
	compoID INT,
	compoName VARCHAR(50) NOT NULL,
	compoDesc TEXT NOT NULL,
	votingActive CHAR(1) default '0' NOT NULL,
	browsable CHAR(1) default '0' NOT NULL,
	PRIMARY KEY (compoID)
);

create table Entry (
	entryID INT,
	compoID INT,
	entryName VARCHAR(50) NOT NULL,
	entryDesc TEXT NOT NULL,
	entryAuthor VARCHAR(50) NOT NULL,
	PRIMARY KEY (entryID, compoID),
	FOREIGN KEY (compoID) REFERENCES Compo (compoID)
);

create table Vote (
	compoID INT,
	entryID INT,
	userID VARCHAR(20),
	PRIMARY KEY (compoID, userID),
	FOREIGN KEY (compoID) REFERENCES Compo (compoID),
	FOREIGN KEY (entryID) REFERENCES Entry (entryID),
	FOREIGN KEY (userID) REFERENCES Users (userID)
);