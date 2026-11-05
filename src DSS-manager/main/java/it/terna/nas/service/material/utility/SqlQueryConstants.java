package it.terna.nas.service.material.utility;

public class SqlQueryConstants
{
    public static final String QUERY_AGG_COND_FUNE = """
            SELECT F.LINEA 
            FROM (                    
                SELECT C.LINEA
                FROM (
                        SELECT B.LINEA, PKG_DSS_AGGREGATE.CALC_KELEG_TRATTA(B.DSS_PC_CORRELATION_ID, B.LINEA, B.TRATTA) AS KELEG_TRATTA
                        FROM (SELECT DSS_PC_CORRELATION_ID, LINEA, TRATTA
                        FROM X4_DSS_AGGREGATE WHERE DSS_PC_CORRELATION_ID = :CORRELATIONKEY AND LINEA = :LINEA
                        GROUP BY DSS_PC_CORRELATION_ID, LINEA, TRATTA) B) C
                    GROUP BY C.LINEA
                    HAVING SUM(KELEG_TRATTA) >= :SOGLIA_KELEG) F
                WHERE EXISTS (  SELECT 1
                                FROM X4_DSS_AGGREGATE XDO
                                WHERE XDO.DSS_PC_CORRELATION_ID = :CORRELATIONKEY 
                                    AND XDO.LINEA = F.LINEA 
                                    AND XDO.KELIGIBILITA >= :SOGLIA_ELIGIBILITA 
                                    AND ROWNUM = 1)
            """;
    
    
    public static final String QUERY_AGG_TELAIO = """
            
            SELECT F.LINEA 
            FROM (
                SELECT :LINEA AS LINEA, PKG_DSS_AGGREGATE.CALC_KELEG_STRUTTURA(:CORRELATIONKEY, :LINEA) AS KELEG
                FROM DUAL) F
            WHERE F.KELEG >= :SOGLIA_KELEG
                AND EXISTS (SELECT 1
                            FROM X4_DSS_AGGREGATE XDO
                            WHERE XDO.DSS_PC_CORRELATION_ID = :CORRELATIONKEY 
                                AND XDO.LINEA = F.LINEA 
                                AND XDO.KELIGIBILITA >= :SOGLIA_ELIGIBILITA 
                                AND ROWNUM = 1)
            """;
}
