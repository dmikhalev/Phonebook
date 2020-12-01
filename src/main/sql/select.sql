SELECT users.username, country.name, COUNT(country.name)
FROM user_contact
       JOIN users ON user_contact.user_id = users.id
       JOIN contact ON user_contact.contact_id = contact.id
       JOIN address ON contact.address_id = address.id
       JOIN city ON address.city_id = city.id
       JOIN country ON city.country_id = country.id
GROUP BY users.username, country.name
ORDER BY users.username