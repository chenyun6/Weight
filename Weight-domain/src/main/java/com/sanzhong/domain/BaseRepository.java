package com.sanzhong.domain;

/**
 * 聚合根-仓储接口
 *
 * @author visual-ddd
 * @version 1.0
 * @param <T> 聚合根实体类型
 * @param <ID> 聚合根唯一标识类型
 */
public interface BaseRepository<T, ID> {

    /**
     * 存储
     *
     * @param entity 聚合根实体
     * @return 聚合根实体
     */
    T save(T entity);

    /**
     * 更新
     *
     * @param entity 聚合根实体
     * @return 聚合根实体
     */
    T update(T entity);

    /**
     * 删除
     *
     * @param entity 聚合根实体
     */
    void remove(T entity);

    /**
     * 查询
     *
     * @param id 唯一标识
     * @return 聚合根实体
     */
    T find(ID id);
}
