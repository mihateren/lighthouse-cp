select coalesce(max(admin_id), 0) + 1
from lighthouse.admins;

