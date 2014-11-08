GRANT ALL PRIVILEGES ON *.* TO root@localhost IDENTIFIED BY 'root' WITH GRANT OPTION;
DROP DATABASE IF EXISTS openstack_projects;
CREATE DATABASE openstack_projects;
USE openstack_projects;

grant all on feedback.* to 'user'@'localhost' identified by 'userpw';

DROP TABLE IF EXISTS Projects;
CREATE TABLE Projects
(
	Name varchar(30),
	Description varchar(300),
	PRIMARY KEY (Name)
);

DROP TABLE IF EXISTS Meetings;
CREATE TABLE Meetings
(
	project_name varchar(30),
    name varchar(30),
	link varchar(60),
	year int,
	FOREIGN KEY(project_name) REFERENCES Projects(Name)
		ON DELETE CASCADE
);