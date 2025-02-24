ALTER SESSION SET CONTAINER=XEPDB1;

DECLARE
  v_count NUMBER;
BEGIN
  -- Check if user exists
  SELECT COUNT(*) INTO v_count 
  FROM dba_users 
  WHERE username = 'DB_ITSUPPTICKETS';
  
  -- Create user if it doesn't exist
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE USER db_itsupptickets IDENTIFIED BY itsupportpass';
    EXECUTE IMMEDIATE 'GRANT CONNECT, RESOURCE, DBA TO db_itsupptickets';
    EXECUTE IMMEDIATE 'ALTER USER db_itsupptickets QUOTA UNLIMITED ON USERS';
  END IF;
  
  -- Always grant permissions (in case they were revoked)
  EXECUTE IMMEDIATE 'GRANT CONNECT, RESOURCE, DBA TO db_itsupptickets';
  EXECUTE IMMEDIATE 'ALTER USER db_itsupptickets QUOTA UNLIMITED ON USERS';
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
    RAISE;
END;
/