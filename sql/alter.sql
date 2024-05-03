Alter table teacher add constraint fk_department foreign key (department_id) references department(department_id) on delete set null;
Alter table course add constraint fk_department2 foreign key (department_id) references department(department_id) on delete cascade;
Alter table student add constraint fk_teacher2 foreign key (teacher_id) references teacher(teacher_id) on delete set null;
Alter table student add constraint fk_course foreign key (course_id) references course(course_id) on delete set null;
Alter table insurance add constraint fk_teacher3 foreign key (teacher_id) references teacher(teacher_id) on delete set null;