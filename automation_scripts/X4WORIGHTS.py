# Automation Script (Jython) - MULTI LAUNCH POINT
# LP su: WORKORDER, WPLABOR, LABTRANS
# Logica:
# - WORKORDER: onadd OR (onupdate and OWNERGROUP modified) -> delete SOLO in update
# - WPLABOR : onadd OR (onupdate and X4TITRIS modified)   -> rebuild pulito (delete+insert)
# - LABTRANS: onadd OR (onupdate and REFWO modified)      -> rebuild pulito (delete+insert)
#
# NB: niente "return" top-level: si usa if/else.

from psdi.server import MXServer
from psdi.mbo import SqlFormat, DBShortcut

# =========================
# CONFIG: NOMI LAUNCH POINT
# =========================
LP_WO    = "X4WORIGHTS"
LP_WPLA  = "X4WPRIGHTS"
LP_LABTR = "X4LABRIGHTS"

# =========================
# HELPERS
# =========================

def _lookup_workorder(wonum, siteid):
    """Ritorna (wonum, workorderid) cercando WORKORDER per wonum+siteid (siteid può essere None/'')."""
    if wonum is None or wonum == "":
        return (None, None)

    woSet = MXServer.getMXServer().getMboSet("WORKORDER", mbo.getUserInfo())
    try:
        wonum_q = wonum.replace("'", "''")

        if siteid is not None and siteid != "":
            siteid_q = siteid.replace("'", "''")
            woSet.setWhere("wonum = '%s' and siteid = '%s'" % (wonum_q, siteid_q))
        else:
            woSet.setWhere("wonum = '%s'" % wonum_q)

        woSet.reset()
        wo = woSet.getMbo(0)
        if wo is None:
            return (None, None)

        return (wo.getString("WONUM"), wo.getLong("WORKORDERID"))
    finally:
        try:
            woSet.close()
        except:
            pass

def get_wo_keys_from_current_mbo():
    """Ritorna (wonum, woid) del WO target in base al launchPoint corrente."""
    lp = str(launchPoint)

    if lp == LP_WO:
        return (mbo.getString("WONUM"), mbo.getLong("WORKORDERID"))

    if lp == LP_WPLA:
        # tipicamente WPLABOR ha WONUM e SITEID
        return _lookup_workorder(mbo.getString("WONUM"), mbo.getString("SITEID"))

    if lp == LP_LABTR:
        # su LABTRANS il wonum sta in REFWO
        # tipicamente LABTRANS ha SITEID; se nel tuo caso non c'è, basta lasciare None
        try:
            siteid = mbo.getString("SITEID")
        except:
            siteid = None
        return _lookup_workorder(mbo.getString("REFWO"), siteid)

    return (None, None)

def should_run_and_delete():
    """Ritorna (runit, dodel) in base al launchPoint e alle modifiche."""
    lp = str(launchPoint)

    if lp == LP_WO:
        runit = (onadd or (onupdate and mbo.isModified("OWNERGROUP")))
        dodel = (onupdate and mbo.isModified("OWNERGROUP"))   # delete solo in update
        return (runit, dodel)

    if lp == LP_WPLA:
        runit = (onadd or (onupdate and mbo.isModified("X4TITRIS")))
        dodel = True  # rebuild pulito quando scatta
        return (runit, dodel)

    if lp == LP_LABTR:
        # MINIMALE: scatta su add o se cambia REFWO
        runit = (onadd)
        # Se vuoi che scatti anche quando cambiano campi che impattano i gruppi (es. LABORCODE),
        # estendi così:
        # runit = onadd or (onupdate and (mbo.isModified("REFWO") or mbo.isModified("LABORCODE")))
        dodel = True  # rebuild pulito quando scatta
        return (runit, dodel)

    return (False, False)

# =========================
# MAIN
# =========================

runit, dodel = should_run_and_delete()

if runit:

    wonum, woid = get_wo_keys_from_current_mbo()

    if wonum is None or woid is None:
        pass
    else:
        # --- QUERY (identica alla tua, non la tocco) ---
        sql = """
            SELECT DISTINCT
                   pg_sib.persongroup       AS PERSNGROUP,
                   w.workorderid            AS WORKORDERID
            FROM wo w
            JOIN pg pg_owner
              ON pg_owner.persongroup = w.ownergroup
            JOIN pg pg_sib
              ON pg_sib.x3parentpersongroup = pg_owner.x3parentpersongroup
            WHERE w.ownergroup IN (SELECT pg FROM v_rlat)
              AND pg_owner.x4tipudb = 'COAM'
              AND w.wonum=:1

            UNION ALL

            SELECT DISTINCT
                   w.ownergroup             AS PERSNGROUP,
                   w.workorderid            AS WORKORDERID
            FROM wo w
            WHERE w.wonum=:1

            UNION ALL
            
            SELECT DISTINCT persongroup.persongroup                    AS PERSNGROUP,
                   w.workorderid            AS WORKORDERID
                   FROM WORKORDER w
                   JOIN PERSONGROUP  ON (X3PARENTPERSONGROUP IN (SELECT x3swerk FROM asset WHERE assetnum=(SELECT assetnum FROM workorder WHERE wonum=:1))
            OR X3PARENTPERSONGROUP IN (SELECT x3swerk FROM asset WHERE assetnum=(SELECT ANCESTOR FROM ASSET WHERE ASSETNUM =(SELECT assetnum FROM workorder WHERE wonum=:1))))
                   WHERE 
            X4TIPUDB IN ('RL','COAM')
            AND WONUM=:1 
            
            UNION ALL

            SELECT DISTINCT
                   c.pg                     AS PERSNGROUP,
                   w.workorderid            AS WORKORDERID
            FROM wo w
            JOIN pg b
              ON b.persongroup = w.ownergroup
            JOIN v_mid a
              ON a.pr = b.x3parentpersongroup
            JOIN v_coam1 c
              ON c.pr = a.pr
            WHERE c.pg IN (SELECT pg FROM v_coam)
              AND w.wonum=:1
            UNION ALL 
            SELECT DISTINCT
                   c.pg                     AS PERSNGROUP,
                   w.workorderid            AS WORKORDERID
            FROM wo w
            JOIN pg b
              ON b.persongroup = w.ownergroup
            JOIN v_mid a
              ON a.pg = b.x3parentpersongroup
            JOIN v_coam1 c
              ON c.pr = a.pr
            WHERE c.pg IN (SELECT pg FROM v_coam)
              AND w.wonum=:1
            UNION ALL
            select distinct
            vw.titris as persngroup,
            w2.workorderid as workorderid
            from wo w2
            join v_wpl vw
            on vw.siteid = w2.siteid
            and (vw.wonum = w2.wonum or vw.wonum in (select wonum from workorder where parent=w2.wonum))
            where w2.wonum= :1


            UNION ALL
            SELECT DISTINCT
                   g.persongroup            AS PERSNGROUP,
                   w2.workorderid           AS WORKORDERID
            FROM wo w2
            JOIN v_wpl vw
              ON vw.siteid = w2.siteid
             AND vw.wonum  = w2.wonum
            JOIN pg g
              ON g.persongroup = vw.titris
            WHERE g.x4udb      = 1
              AND g.x4tipudb   = 'RL'
              AND g.x3parentpersongroup IN (
                  SELECT pr
                  FROM v_coam1
              )
              AND w2.wonum=:1

            UNION ALL
            SELECT DISTINCT
                   g_src.persongroup        AS PERSNGROUP,
                   w2.workorderid           AS WORKORDERID
            FROM wo w2
            JOIN v_ltr l
              ON l.refwo  = w2.wonum
             AND l.siteid = w2.siteid
            JOIN pgt t_rg
              ON t_rg.resppartygroup = l.laborcode
            JOIN v_ui1 u
              ON u.pg = t_rg.persongroup
            JOIN v_mid m
              ON m.pg = u.pr
            JOIN v_coam1 c
              ON c.pr = m.pr
            JOIN pg g_src
              ON g_src.persongroup = c.pg
            WHERE g_src.x4udb    = 1
              AND g_src.x4tipudb <> 'UI'
              AND w2.wonum=:1

            UNION ALL
            SELECT DISTINCT
                   t.persongroup            AS PERSNGROUP,
                   w.workorderid            AS WORKORDERID
            FROM wo w
            JOIN V_AST_GRP a
              ON a.assetnum = w.assetnum
            JOIN pgt t
              ON t.persongroup = a.grp
            WHERE w.wonum=:1
            
            UNION ALL
            SELECT DISTINCT
                   t_rg.persongroup        AS PERSNGROUP,
                   w2.workorderid           AS WORKORDERID
            FROM wo w2
            JOIN v_ltr l
              ON l.refwo  = w2.wonum
             AND l.siteid = w2.siteid
            JOIN pgt t_rg
              ON t_rg.respparty = l.laborcode
            JOIN persongroup x
              ON x.X4UDB=1 AND x.persongroup=t_rg.persongroup AND x.persongroup!=w2.ownergroup
              WHERE w2.wonum=:1
            UNION ALL
            SELECT DISTINCT
                   g_src.persongroup        AS PERSNGROUP,
                   w2.workorderid           AS WORKORDERID
            FROM wo w2
            JOIN v_ltr l
              ON l.refwo  = w2.wonum
             AND l.siteid = w2.siteid
             JOIN pgt t_rg
              ON t_rg.resppartygroup = l.laborcode
             JOIN pg g_src
              ON g_src.persongroup = t_rg.persongroup
            WHERE g_src.x4udb    = 1
              AND w2.wonum=:1
        """

        sqfQuery = SqlFormat(sql)
        sqfQuery.setObject(1, "WORKORDER", "WONUM", wonum)
        formatted = sqfQuery.format()

        dbs = DBShortcut()
        rs = None
        connected = False
        rightsSet = None

        try:
            # connect fallback
            try:
                dbs.connect(mbo.getUserInfo())
                connected = True
            except:
                try:
                    dbs.connect(mbo.getUserInfo().getConnectionKey())
                    connected = True
                except:
                    connected = False

            # query fallback
            try:
                rs = dbs.executeQuery(formatted)
            except:
                rs = dbs.executeQuery(formatted, mbo.getUserInfo())

            if rs is None:
                pass
            else:
                # delete via JDBC (bypassa permessi MBO) se richiesto
                if dodel:
                    conn = None
                    stmt = None
                    conKey = mbo.getUserInfo().getConnectionKey()
                    try:
                        conn = MXServer.getMXServer().getDBManager().getConnection(conKey)
                        stmt = conn.createStatement()
                        stmt.executeUpdate("DELETE FROM X4_WO_RIGHTS WHERE WORKORDERID = %d" % woid)
                        conn.commit()
                    finally:
                        if stmt is not None:
                            try: stmt.close()
                            except: pass
                        if conn is not None:
                            try: MXServer.getMXServer().getDBManager().freeConnection(conKey)
                            except: pass

                # insert
                rightsSet = MXServer.getMXServer().getMboSet("X4_WO_RIGHTS", MXServer.getMXServer().getSystemUserInfo())
                inserted = 0
                existingPG = set()

                while rs.next():
                    pg = rs.getString("PERSNGROUP")
                    # salta se NULL o stringa vuota/spazi
                    if pg is None or pg.strip() == "":
                        continue
                    if pg in existingPG:
                        continue
                    newRow = rightsSet.add()
                    newRow.setValue("PERSNGROUP", rs.getString("PERSNGROUP"))
                    newRow.setValue("WORKORDERID", rs.getLong("WORKORDERID"))
                    inserted += 1
                    existingPG.add(pg)

                if inserted > 0:
                    rightsSet.save()

        finally:
            if rs is not None:
                try: rs.close()
                except: pass
            if rightsSet is not None:
                try: rightsSet.close()
                except: pass
            if connected:
                try: dbs.close()
                except: pass

else:
    pass