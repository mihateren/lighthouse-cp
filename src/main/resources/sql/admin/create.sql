insert into lighthouse.admins (admin_id, center_id, first_name, last_name, email, birthdate)
values (:admin_id, :center_id, :first_name, :last_name, :email, :birthdate)
returning admin_id, center_id, first_name, last_name, email, birthdate;

