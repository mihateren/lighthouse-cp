select coalesce(max(victim_id), 0) + 1
from lighthouse.victims;

