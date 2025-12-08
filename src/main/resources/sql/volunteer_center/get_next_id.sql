select coalesce(max(center_id), 0) + 1
from lighthouse.volunteer_centers;

