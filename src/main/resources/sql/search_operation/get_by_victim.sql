select operation_id, victim_id, admin_id, status, created_at, updated_at
from lighthouse.search_operations
where victim_id = :victim_id
order by created_at desc;

