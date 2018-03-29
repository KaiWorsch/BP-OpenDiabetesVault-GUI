/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.database;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.HsqldbDatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.logger.Log;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.hsqldb.util.DatabaseManager;
import org.hsqldb.util.DatabaseManagerSwing;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author danielschafer
 */
/* public class VaultDB {
    private static final String database_url = "jdbc:hsqldb:mem:vaultDB";
    
    private static VaultDB INSTANCE = null;
    //private ConnectionSource connectionSource;
    private static final Logger LOG = Logger.getLogger(VaultDB.class.getName());

    private VaultDB() throws SQLException {
        this.c = DriverManager.getConnection("jdbc:hsqldb:mem:vaultDB", "SA", "");
    }
    
    Connection c;
    
} */