update lighthouse.volunteer_centers
set title = :title,
    city = :city,
    post_code = :post_code,
    address = :address
where center_id = :center_id
returning center_id, title, city, post_code, address;

