package ro.ubbcluj.map.repository.db;

import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.validators.Validator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;

public class MesajeDbRepository extends InMemoryRepository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;

    public MesajeDbRepository(Validator<Message> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Message findOne(Long aLong) {
        String sql = "SELECT * from messages  where id = ?";

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            Message var14;
            try {
                PreparedStatement statement = connection.prepareStatement(sql);

                try {
                    statement.setLong(1, aLong);
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();
                    Long from = resultSet.getLong("from_user");
                    Long to = resultSet.getLong("to_user");
                    String message = resultSet.getString("message");
                    String data = resultSet.getString("data");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dateTime = LocalDate.parse(data, formatter);
                    Long reply = resultSet.getLong("reply");
                    Message messageEntity = new Message(from, to, message, dateTime, reply);
                    messageEntity.setId(aLong);
                    var14 = messageEntity;
                } catch (Throwable var17) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var16) {
                            var17.addSuppressed(var16);
                        }
                    }

                    throw var17;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var18) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var15) {
                        var18.addSuppressed(var15);
                    }
                }

                throw var18;
            }

            if (connection != null) {
                connection.close();
            }

            return var14;
        } catch (SQLException var19) {
            var19.printStackTrace();
            return null;
        }
    }

    public Iterable<Message> findAll() {
        HashSet messages = new HashSet();

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            HashSet var14;
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * from messages");

                try {
                    ResultSet resultSet = statement.executeQuery();

                    try {
                        while(resultSet.next()) {
                            Long id = resultSet.getLong("id");
                            messages.add(this.findOne(id));
                        }

                        var14 = messages;
                    } catch (Throwable var10) {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (Throwable var9) {
                                var10.addSuppressed(var9);
                            }
                        }

                        throw var10;
                    }

                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (Throwable var11) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var8) {
                            var11.addSuppressed(var8);
                        }
                    }

                    throw var11;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var12) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var7) {
                        var12.addSuppressed(var7);
                    }
                }

                throw var12;
            }

            if (connection != null) {
                connection.close();
            }

            return var14;
        } catch (SQLException var13) {
            var13.printStackTrace();
            return null;
        }
    }

    public Message save(Message entity) {
        String sql = "insert into messages (from_user, to_user, message, data, reply) values (?, ?, ?, ?, ?)";

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            try {
                PreparedStatement ps = connection.prepareStatement(sql);

                try {
                    ps.setLong(1, entity.getFrom());
                    ps.setLong(2, entity.getTo());
                    String dataString = entity.getData().toString();
                    ps.setString(3, entity.getMessage());
                    ps.setString(4, dataString);
                    ps.setLong(5, entity.getReply());
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

    public Message delete(Long aLong) {
        String sql = "DELETE from messages WHERE id = ?";

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            try {
                PreparedStatement statement = connection.prepareStatement(sql);

                try {
                    statement.setLong(1, aLong);
                    statement.executeUpdate();
                } catch (Throwable var9) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }

                    throw var9;
                }

                if (statement != null) {
                    statement.close();
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
}
