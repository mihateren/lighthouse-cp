select volunteer_id, center_id, first_name, last_name, birthdate, phone, email, skills, is_active, registration_date, updated_at
from lighthouse.volunteers
where volunteer_id = :volunteer_id;

