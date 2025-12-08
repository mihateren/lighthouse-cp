insert into lighthouse.search_operations (operation_id, victim_id, admin_id, status, created_at, updated_at)
values (:operation_id, :victim_id, :admin_id, :status, now(), now())
returning operation_id, victim_id, admin_id, status, created_at, updated_at;

