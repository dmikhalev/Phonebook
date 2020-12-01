package backend.jdbc;

public interface DAO<Entity, Key> {

    Long create(Entity model);

    Entity read(Key key);

    boolean update(Entity model);

    boolean delete(Key key);
}
