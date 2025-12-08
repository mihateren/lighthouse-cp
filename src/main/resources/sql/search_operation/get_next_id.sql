select coalesce(max(operation_id), 0) + 1
from lighthouse.search_operations;

