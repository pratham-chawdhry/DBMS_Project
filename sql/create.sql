create table teacher (teacher_id int auto_increment, teacher_name char(30), age int, department_id int, constraint pk_teacher primary key (teacher_id));

create table department (department_id int auto_increment, department_name char(30), constraint pk_department primary key (department_id));

create table course (course_id int auto_increment, course_name char(30), department_id int, constraint pk_course primary key (course_id));

create table student (student_id int auto_increment, student_age int, student_name char(30), teacher_id int, course_id int, constraint pk_student primary key (student_id));  

create table insurance (people_id int auto_increment, people_name char(30), teacher_id int, constraint pk_insurance primary key (people_id));