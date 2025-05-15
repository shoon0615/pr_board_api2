/* DML */

/*INSERT INTO crud_entity(id, title, contents, created_date, modified_date)
VALUES (DEFAULT, '제목1', '내용1', NOW(), NOW());

INSERT INTO crud_entity(id, title, contents, created_date, modified_date)
VALUES (DEFAULT, '제목2', '내용2', NOW(), NOW());*/

/*INSERT INTO crud_entity(id, title, contents, created_date, modified_date)
VALUES
--('제목1', '내용1', NOW(), NOW())
(DEFAULT, '제목1', '내용1', NOW(), NOW())
, (DEFAULT, '제목1', '내용1', NOW(), NOW());*/

/* postgreSQL 문법 -> MODE 설정 안하면 H2 는 실행 안됨 */
BEGIN
--    IF NOT EXISTS (SELECT * FROM crud_entity WHERE crud_id = 1) THEN
    IF NOT EXISTS (SELECT * FROM crud_entity WHERE title = '제목1' AND contents = '내용1') THEN
        INSERT INTO crud_entity(id, title, contents, created_date, modified_date)
        VALUES (DEFAULT, '제목1', '내용1', NOW(), NOW());
    END IF;
END;

MERGE INTO crud_entity(title, contents, created_date, modified_date)
KEY(title)
VALUES ('제목2', '내용2', NOW(), NOW());

MERGE INTO crud_entity(id, title, contents, created_date, modified_date)
KEY(title)
VALUES (DEFAULT, '제목3', '내용3', NOW(), NOW());

MERGE INTO crud_entity(id, title, contents, created_date, modified_date)
KEY(title)
VALUES (DEFAULT, '제목4', '내용4', NOW(), NOW());