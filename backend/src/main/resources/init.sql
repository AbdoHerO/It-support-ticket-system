ALTER SESSION SET CONTAINER=XEPDB1;

BEGIN
  -- Check if user exists
  DECLARE v_count NUMBER;
  BEGIN
    SELECT COUNT(*) INTO v_count FROM all_users WHERE username = 'DB_ITSUPPTICKETS';
    
    -- Only create user if it does not exist
    IF v_count = 0 THEN
      EXECUTE IMMEDIATE 'CREATE USER db_itsupptickets IDENTIFIED BY itsupportpass';
      EXECUTE IMMEDIATE 'GRANT CONNECT, RESOURCE, DBA TO db_itsupptickets';
      EXECUTE IMMEDIATE 'ALTER USER db_itsupptickets QUOTA UNLIMITED ON USERS';
    END IF;
  END;
END;
/
