package in.appnow.astrobuddy.dao.repository;

import java.util.List;

public interface BaseRepository<T> {
    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    long insert(T obj);

    /**
     * Insert an array of objects in the database.
     *
     * @param list the objects to be inserted.
     */
    long[] insert(List<T> list);

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    int update(T obj);

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    int delete(T obj);

    /**
     * Delete an object from the database
     *
     * @param list the object to be deleted
     */
    int delete(List<T> list);
}
