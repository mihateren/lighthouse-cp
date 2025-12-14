select victim_id, first_name, last_name, city, age, height, notes, updated_at
from lighthouse.victims
where victim_id = :victim_id;

