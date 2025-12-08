select admin_id, center_id, first_name, last_name, email, birthdate
from lighthouse.admins
where center_id = :center_id
order by last_name, first_name;

