package ro.ubbcluj.map.repository.db;

import ro.ubbcluj.map.domain.FriendRequest;
import ro.ubbcluj.map.domain.validators.Validator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;

public class CereriDbRepository extends InMemoryRepository<Long, FriendRequest> {
    private final String url;
    private final String username;
    private final String password;

    public CereriDbRepository(Validator<FriendRequest> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public FriendRequest findOne(Long aLong) {
        String sql = "SELECT * from friend_requests where id = ?";

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            FriendRequest var10;
            try {
                PreparedStatement statement = connection.prepareStatement(sql);

                try {
                    statement.setLong(1, aLong);
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();
                    Long id1 = resultSet.getLong("id1");
                    Long id2 = resultSet.getLong("id2");
                    String status = resultSet.getString("status");
                    FriendRequest friendRequest = new FriendRequest(id1, id2, status);
                    friendRequest.setId(aLong);
                    var10 = friendRequest;
                } catch (Throwable var13) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                    }

                    throw var13;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var14) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var11) {
                        var14.addSuppressed(var11);
                    }
                }

                throw var14;
            }

            if (connection != null) {
                connection.close();
            }

            return var10;
        } catch (SQLException var15) {
            var15.printStackTrace();
            return null;
        }
    }

    public Iterable<FriendRequest> findAll() {
        HashSet friendRequests = new HashSet();

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            HashSet var14;
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * from friend_requests");

                try {
                    ResultSet resultSet = statement.executeQuery();

                    try {
                        while(resultSet.next()) {
                            Long id = resultSet.getLong("id");
                            friendRequests.add(this.findOne(id));
                        }

                        var14 = friendRequests;
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

    public FriendRequest save(FriendRequest entity) {
        String sql = "insert into friend_requests (id1, id2, status) values (?, ?, ?)";

        try {
            Connection connection = DriverManager.getConnection(this.url, this.username, this.password);

            try {
                PreparedStatement ps = connection.prepareStatement(sql);

                try {
                    ps.setLong(1, entity.getId1());
                    ps.setLong(2, entity.getId2());
                    ps.setString(3, entity.getStatus());
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

    public FriendRequest delete(Long aLong) {
        String sql = "DELETE from friend_requests WHERE id = ?";

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
