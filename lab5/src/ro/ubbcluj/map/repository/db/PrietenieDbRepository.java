package ro.ubbcluj.map.repository.db;


import ro.ubbcluj.map.domain.Prietenie;
import ro.ubbcluj.map.domain.validators.Validator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;

public class PrietenieDbRepository extends InMemoryRepository<Long, Prietenie> {
    private final String url;
    private final String username;
    private final String password;

    public PrietenieDbRepository(Validator<Prietenie> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Prietenie save(Prietenie prietenie) {
        String verifySql = "SELECT COUNT(*) FROM friendships f WHERE f.id1 = ? and f.id2 = ? and f.data = ?";

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            try {
                PreparedStatement ps = connection.prepareStatement(verifySql);

                try {
                    ps.setLong(1, prietenie.getId2());
                    ps.setLong(2, prietenie.getId1());
                    String dataString = prietenie.getData().toString();
                    ps.setString(3, dataString);
                    ResultSet resultSet = ps.executeQuery();
                    resultSet.next();
                    int ok = resultSet.getInt(1);
                    if (ok == 1) {
                        throw new IllegalArgumentException("The friendship already exists!");
                    }
                } catch (Throwable var15) {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Throwable var11) {
                            var15.addSuppressed(var11);
                        }
                    }

                    throw var15;
                }

                if (ps != null) {
                    ps.close();
                }
            } catch (Throwable var16) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var10) {
                        var16.addSuppressed(var10);
                    }
                }

                throw var16;
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException var17) {
            var17.printStackTrace();
        }

        String sql = "insert into friendships (id1, id2, data) values (?, ?, ?)";

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            try {
                PreparedStatement ps = connection.prepareStatement(sql);

                try {
                    ps.setLong(1, prietenie.getId1());
                    ps.setLong(2, prietenie.getId2());
                    String dataString = prietenie.getData().toString();
                    ps.setString(3, dataString);
                    ps.executeUpdate();
                } catch (Throwable var12) {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Throwable var9) {
                            var12.addSuppressed(var9);
                        }
                    }

                    throw var12;
                }

                if (ps != null) {
                    ps.close();
                }
            } catch (Throwable var13) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var8) {
                        var13.addSuppressed(var8);
                    }
                }

                throw var13;
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException var14) {
            var14.printStackTrace();
        }

        return null;
    }

    public Prietenie delete(Long id) {
        String sql = "delete from friendships where id=?";

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            try {
                PreparedStatement ps = connection.prepareStatement(sql);

                try {
                    ps.setLong(1, id);
                    ps.executeUpdate();
                } catch (Throwable var9) {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }

                    throw var9;
                }

                if (ps != null) {
                    ps.close();
                }
            } catch (Throwable var10) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                    }
                }

                throw var10;
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
        }

        return null;
    }

    public Set<Prietenie> findAll() {
        HashSet friendships = new HashSet();

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            HashSet var19;
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");

                try {
                    ResultSet resultSet = statement.executeQuery();

                    try {
                        while(resultSet.next()) {
                            Long id = resultSet.getLong("id");
                            Long id1 = resultSet.getLong("id1");
                            Long id2 = resultSet.getLong("id2");
                            String data = resultSet.getString("data");
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate dateTime = LocalDate.parse(data, formatter);
                            Prietenie prietenie = new Prietenie(id1, id2, dateTime);
                            prietenie.setId(id);
                            friendships.add(prietenie);
                        }

                        var19 = friendships;
                    } catch (Throwable var15) {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (Throwable var14) {
                                var15.addSuppressed(var14);
                            }
                        }

                        throw var15;
                    }

                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (Throwable var16) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var13) {
                            var16.addSuppressed(var13);
                        }
                    }

                    throw var16;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var17) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var12) {
                        var17.addSuppressed(var12);
                    }
                }

                throw var17;
            }

            if (connection != null) {
                connection.close();
            }

            return var19;
        } catch (SQLException var18) {
            var18.printStackTrace();
            return friendships;
        }
    }
}
