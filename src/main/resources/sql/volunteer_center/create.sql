insert into lighthouse.volunteer_centers (center_id, title, city, post_code, address)
values (:center_id, :title, :city, :post_code, :address)
returning center_id, title, city, post_code, address;

