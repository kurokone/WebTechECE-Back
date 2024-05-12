  +---------------+    +------------+    +---------------+    +------------+
  |    users      |    |   movies   |    |  place_types  |    |   places   |
  +---------------+    +------------+    +---------------+    +------------+
  | PK id         |    | PK id      |    | PK id         |    | PK id      |
  | first_name    |    | title      |    | name          |    | title      |
  | last_name     |    | director   |    +---------------+    | type_id    |
  | birthdate     |    | release_dt |                         | address    |
  | username      |    | synopsis   |                         | opening_h  |
  | password      |    | poster_url |                         +------------+
  | image         |    +------------+
  | created_at    |
  +---------------+

  +---------------+    +----------------+    +---------------+    +----------------+
  |   reviews     |    |   movie_reviews|    | place_reviews |    | comment_likes  |
  +---------------+    +----------------+    +---------------+    +----------------+
  | PK id         |    | PK id          |    | PK id         |    | review_id       |
  | user_id       |    | movie_id       |    | place_id      |    | user_id         |
  | entity_id     |    | rating         |    | rating        |    | liked           |
  | entity_type   |    | summary        |    | summary       |    | user_id         |
  | rating        |    | review         |    | review        |    | liked           |
  | summary       |    | created_at     |    | created_at    |    +----------------+
  | review        |    +----------------+    +---------------+
  | created_at    |
  +---------------+

  +---------------+
  |  comments     |
  +---------------+
  | PK id         |
  | user_id       |
  | review_id     |
  | content       |
  | created_at    |
  +---------------+
