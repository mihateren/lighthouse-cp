select coalesce(max(diary_id), 0) + 1
from lighthouse.search_diaries;

