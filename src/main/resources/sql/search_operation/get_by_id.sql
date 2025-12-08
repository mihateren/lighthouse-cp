select operation_id, victim_id, admin_id, status, created_at, updated_at
from lighthouse.search_operations
where operation_id = :operation_id;

