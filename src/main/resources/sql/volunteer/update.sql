update lighthouse.volunteers
set center_id = :center_id,
    first_name = :first_name,
    last_name = :last_name,
    birthdate = :birthdate,
    phone = :phone,
    email = :email,
    skills = :skills,
    is_active = :is_active,
    updated_at = now()
where volunteer_id = :volunteer_id
returning volunteer_id, center_id, first_name, last_name, birthdate, phone, email, skills, is_active, registration_date, updated_at;

