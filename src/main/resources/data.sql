INSERT INTO accessioning_user
  SELECT
    'amp-dev@ebi.ac.uk',
    'ROLE_ADMIN'
  WHERE
    NOT EXISTS(
        SELECT user_id
        FROM accessioning_user
        WHERE user_id = 'amp-dev@ebi.ac.uk'
    );