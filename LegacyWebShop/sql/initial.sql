insert into `role` (`id`, `level1`, `type`) values(1, 0, 'admin');
insert into `role` (`id`, `level1`, `type`) values(2, 1, 'user');


insert into `customer` (`id`, `name`, `lastname`, `password`, `username`, `role`) values(0, 'admin', 'admin', 'admin', 'admin', 1);