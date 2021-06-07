package me.exception.tickets.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AsyncMySQL {
    private ExecutorService executor;

    public AsyncMySQL(Plugin plugin, String host, int port, String user, String password, String database) {
        try {
            this.sql = new MySQL(host, port, user, "", database);
            this.executor = Executors.newCachedThreadPool();
            this.plugin = plugin;
        } catch (Exception e) {
            Bukkit.shutdown();
            return;
        }
    }

    private Plugin plugin;
    private MySQL sql;

    public void update(PreparedStatement statement) {
        this.executor.execute(() -> this.sql.queryUpdate(statement));
    }

    public void update(String statement) {
        this.executor.execute(() -> this.sql.queryUpdate(statement));
    }

    public void query(PreparedStatement statement, Consumer<ResultSet> consumer) {
        this.executor.execute(() -> {
            ResultSet result = this.sql.query(statement);
            Bukkit.getScheduler().runTask(this.plugin, () -> consumer.accept(result));
        });
    }

    public void query(String statement, Consumer<ResultSet> consumer) {
        this.executor.execute(() -> {
            ResultSet result = this.sql.query(statement);
            Bukkit.getScheduler().runTask(this.plugin, () -> consumer.accept(result));
        });
    }


    public PreparedStatement prepare(String query) {
        try {
            return this.sql.getConnection().prepareStatement(query);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public MySQL getMySQL() {
        return this.sql;
    }

    public static class MySQL {
        private String host;
        private String user;
        private String password;
        private String database;
        private int port;
        private Connection conn;

        public MySQL(String host, int port, String user, String password, String database) throws Exception {
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
            this.database = database;

            openConnection();
        }

        public void queryUpdate(String query) {
            checkConnection();
            try (PreparedStatement statement = this.conn.prepareStatement(query)) {
                queryUpdate(statement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void queryUpdate(PreparedStatement statement) {
            checkConnection();
            try {
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        public ResultSet query(String query) {
            checkConnection();
            try {
                return query(this.conn.prepareStatement(query));
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        public ResultSet query(PreparedStatement statement) {
            checkConnection();
            try {
                return statement.executeQuery();
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        public Connection getConnection() {
            return this.conn;
        }

        public void checkConnection() {
            try {
                if (this.conn == null || !this.conn.isValid(10) || this.conn.isClosed()) openConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public Connection openConnection() throws Exception {
            Class.forName("com.mysql.jdbc.Driver");
            return this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.user, this.password);
        }

        public void closeConnection() {
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                this.conn = null;
            }
        }

        public void createTabels() {
            try {
                Statement st = this.conn.createStatement();
                st.executeUpdate("CREATE TABLE IF NOT EXISTS Extras(UUID VARCHAR(255), EXTRA VARCHAR(100));");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


