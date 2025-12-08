insert into lighthouse.victims (victim_id, first_name, last_name, city, age, height, photo, notes, updated_at)
values (:victim_id, :first_name, :last_name, :city, :age, :height, :photo, :notes, now())
returning victim_id, first_name, last_name, city, age, height, photo, notes, updated_at;

