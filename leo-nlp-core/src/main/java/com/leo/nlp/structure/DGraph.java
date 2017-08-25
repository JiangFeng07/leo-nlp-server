package com.leo.nlp.structure;

import lombok.Data;

import java.util.Iterator;

/**
 * 有向图的实现
 * Created by lionel on 17/8/23.
 */
public interface DGraph<V> {
    //深度优先遍历
    int ITERATOR_TYPE_DFS = 0;

    //广度优先遍历
    int ITERATOR_TYPE_BFS = 1;

    /**
     * 添加一个端点
     *
     * @param v 端点的索引
     */
    int add(V v);

    /**
     * 添加一条边
     *
     * @param edge 边
     */
    void add(Edge<V> edge);

    /**
     * 删除一个端点
     *
     * @param v 端点
     * @return 删除掉的端点
     */
    V remove(V v);

    /**
     * 删除一条边
     *
     * @param edge 边
     * @return 删除掉的边
     */
    Edge<V> remove(Edge<V> edge);

    /**
     * 获取一个端点
     *
     * @param index 端点索引
     * @return 端点
     */
    V get(int index);

    /**
     * 获取一条边
     *
     * @param source 起点
     * @param target 终点
     * @return 边
     */
    Edge<V> get(int source, int target);

    /**
     * 有向图的遍历
     *
     * @param type 遍历类型
     * @param root 遍历起始节点
     * @return 遍历
     */
    Iterator<V> iterator(int type, V root);

    /**
     * 转为无环图
     */
    void convertDAG();

    @Data
    class Edge<V> {
        V source;//起点
        V target;//终点
        double weight;//权值

        public Edge() {
        }

        public Edge(V source, V target) {
            this(source, target, 0);
        }

        public Edge(V source, V target, double weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }
    }
}
