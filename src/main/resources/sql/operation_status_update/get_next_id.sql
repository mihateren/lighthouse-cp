select coalesce(max(update_id), 0) + 1
from lighthouse.operation_status_updates;

