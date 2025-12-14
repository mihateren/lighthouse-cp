update lighthouse.victims
set first_name = :first_name,
    last_name = :last_name,
    city = :city,
    age = :age,
    height = :height,
    notes = :notes,
    updated_at = now()
where victim_id = :victim_id
returning victim_id, first_name, last_name, city, age, height, notes, updated_at;

