select center_id, title, city, post_code, address
from lighthouse.volunteer_centers
where center_id = :center_id;

