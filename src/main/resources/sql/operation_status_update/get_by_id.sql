select update_id, operation_id, volunteer_id, proposed_status, reason, admin_decision, admin_notes, created_at, decided_at, updated_at
from lighthouse.operation_status_updates
where update_id = :update_id;

