delimiter $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `Check4Clique`(
	IN p_actor1 INT(10), 
	IN p_actor2 INT(10),
	IN p_actor3 INT(10),
	IN p_actor4 INT(10),
	IN p_year INT(4),
	OUT p_count INT(10))
BEGIN
-- Create temp tabels for results 
 DROP TEMPORARY TABLE if exists TmpActor1;
 CREATE TEMPORARY TABLE TmpActor1 engine=memory (
 	SELECT actor_id, movie_id, year from imdb.roles roles LEFT OUTER JOIN imdb.movies movies ON movies.id = roles.movie_id where actor_id = p_actor1 AND year >= p_year
 );
 DROP TEMPORARY TABLE if exists TmpActor2;
 CREATE TEMPORARY TABLE TmpActor2 engine=memory (
	SELECT actor_id, movie_id, year from imdb.roles roles LEFT OUTER JOIN imdb.movies movies ON movies.id = roles.movie_id where actor_id = p_actor2 AND year >= p_year
 );
 DROP TEMPORARY TABLE if exists TmpActor3;
 CREATE TEMPORARY TABLE TmpActor3 engine=memory (
 	SELECT actor_id, movie_id, year from imdb.roles roles LEFT OUTER JOIN imdb.movies movies ON movies.id = roles.movie_id where actor_id = p_actor3 AND year >= p_year
 );
 DROP  TABLE if exists TmpActor4;
 CREATE TEMPORARY TABLE TmpActor4 engine=memory (
 	SELECT actor_id, movie_id, year from imdb.roles roles LEFT OUTER JOIN imdb.movies movies ON movies.id = roles.movie_id where actor_id = p_actor4 AND year >= p_year
 );
-- Compare and store in new tables
DROP  TABLE if exists CmpTbl1;
 CREATE TEMPORARY TABLE CmpTbl1 engine=memory (
 	SELECT TmpActor1.movie_id
	FROM TmpActor1 INNER JOIN TmpActor2
	USING (movie_id)
 );
DROP  TABLE if exists CmpTbl2;
 CREATE TEMPORARY TABLE CmpTbl2 engine=memory (
 	SELECT TmpActor3.movie_id
	FROM TmpActor3 INNER JOIN TmpActor4
	USING (movie_id)
 );

DROP  TABLE if exists CliqueTbl;
 CREATE TEMPORARY TABLE CliqueTbl engine=memory (
 	SELECT CmpTbl1.movie_id
	FROM CmpTbl1 INNER JOIN CmpTbl2
	USING (movie_id)
 );

-- Clean up in memory
DROP TEMPORARY TABLE TmpActor1;
DROP TEMPORARY TABLE TmpActor2;
DROP TEMPORARY TABLE TmpActor3;
DROP TEMPORARY TABLE TmpActor4;

SELECT count(movie_id) INTO p_count FROM CliqueTbl;

-- Drop temp tables on exit
DROP TEMPORARY TABLE CmpTbl1;
DROP TEMPORARY TABLE CmpTbl2;
DROP TEMPORARY TABLE CliqueTbl;
END$$

