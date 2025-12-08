select coalesce(max(volunteer_id), 0) + 1
from lighthouse.volunteers;

