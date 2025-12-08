update lighthouse.admins
set center_id = :center_id,
    first_name = :first_name,
    last_name = :last_name,
    email = :email,
    birthdate = :birthdate
where admin_id = :admin_id
returning admin_id, center_id, first_name, last_name, email, birthdate;

