package galaxia.week3.dbquery.repository;

import galaxia.week3.dbquery.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(
            value = """
                    SELECT
                        b.id AS boardId,
                        b.title AS title,
                        b.content AS content,
                        b.type AS type,
                        b.created_at AS createdAt,
                        CASE
                            WHEN b.type = 'CP' THEN cu.name
                            WHEN b.type = 'MASTER' THEN ma.name
                        END AS userName
                    FROM board b
                    LEFT JOIN cp_user cu
                        ON b.type = 'CP'
                       AND b.user_id = cu.id
                    LEFT JOIN master_admin ma
                        ON b.type = 'MASTER'
                       AND b.user_id = ma.id
                    WHERE b.created_at BETWEEN :fromDate AND :toDate
                    ORDER BY b.id DESC
                    LIMIT :limit OFFSET :offset
                    """,
            nativeQuery = true
    )
    List<Map<String, Object>> findBoardList(
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

}