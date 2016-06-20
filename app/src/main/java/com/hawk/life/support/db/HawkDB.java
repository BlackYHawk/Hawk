package com.hawk.life.support.db;


import com.hawk.library.common.context.GlobalContext;
import com.hawk.orm.SqliteUtility;
import com.hawk.orm.SqliteUtilityBuilder;

public class HawkDB {

    public static final String HAWK_DB_NAME = "hawk_db";
    public static final int HAWK_DB_VERSION = 1;

	public static SqliteUtility getSqlite() {
        if (SqliteUtility.getInstance(HAWK_DB_NAME) == null)
            new SqliteUtilityBuilder().configDBName(HAWK_DB_NAME).configVersion(HAWK_DB_VERSION).build(GlobalContext.getInstance());

		return SqliteUtility.getInstance(HAWK_DB_NAME);
	}

}
