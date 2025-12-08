insert into lighthouse.volunteers (volunteer_id, center_id, first_name, last_name, birthdate, phone, email, skills, is_active, registration_date, updated_at)
values (:volunteer_id, :center_id, :first_name, :last_name, :birthdate, :phone, :email, :skills, :is_active, now(), now())
returning volunteer_id, center_id, first_name, last_name, birthdate, phone, email, skills, is_active, registration_date, updated_at;

