CREATE DATABASE library;
USE library;

CREATE TABLE users ( id INT UNSIGNED NULL PRIMARY KEY AUTO_INCREMENT , username VARCHAR(60) , pass VARCHAR(60) );
CREATE TABLE books ( genre VARCHAR(30) NOT NULL,title VARCHAR(60) NOT NULL, date_of_pub INT UNSIGNED NOT NULL   );
CREATE TABLE Reserved_Books ( idRes INT UNSIGNED NULL , FOREIGN KEY(idRes) REFERENCES users(id) , title VARCHAR(80) NOT NULL, Mtimestamp1 TIMESTAMP DEFAULT CURRENT_TIMESTAMP );
CREATE TABLE Requested_Books ( idRes INT UNSIGNED NULL , FOREIGN KEY(idRes) REFERENCES users(id) , title VARCHAR(80) NOT NULL, Mtimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP  );
CREATE TABLE user_History ( idRes INT UNSIGNED NULL , FOREIGN KEY(idRes) REFERENCES users(id) , title VARCHAR(80) NOT NULL, QueryType VARCHAR(30) NOT NULL , Mtimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP  );

INSERT INTO books VALUE ('Textbook','fundamentals of physics',1967);
INSERT INTO books VALUE ('Textbook','Math is fun',1989);
INSERT INTO books VALUE ('Textbook','EECE 350 funbook',1900);

INSERT INTO books VALUE ('History','Napoleon Borntoparty',1990);
INSERT INTO books VALUE ('History','The French Touch',1999);
INSERT INTO books VALUE ('History','American Freedom',1976);

INSERT INTO books VALUE ('Drama','X loves Y',1972);
INSERT INTO books VALUE ('Drama','Juliette and Romeo',1890);
INSERT INTO books VALUE ('Drama','What a sinister day',1922);

INSERT INTO books VALUE ('Sci-fi','War of the Worlds',1943);
INSERT INTO books VALUE ('Sci-fi','The Great Saibaman',1932);
INSERT INTO books VALUE ('Sci-fi','Toasted!',1998);

CREATE DATABASE libraryAlt;
USE libraryAlt;

CREATE TABLE books ( genre VARCHAR(30) NOT NULL,title VARCHAR(60) NOT NULL, date_of_pub INT UNSIGNED NOT NULL   );

INSERT INTO books VALUE ('Sci-fi','Great Scott!',1936);
INSERT INTO books VALUE ('Sci-fi','Taboo',1993);

select * from users






