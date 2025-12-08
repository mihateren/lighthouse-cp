select admin_id, center_id, first_name, last_name, email, birthdate
from lighthouse.admins
where admin_id = :admin_id;

