package com.leo.nlp.structure;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Created by lionel on 17/8/23.
 */
@Slf4j
public class ListDGraph<V> implements DGraph<V> {
    /**
     * 邻接表实现有向图
     */
    private class VE {

        private V v;//此顶点
        private List<Edge<V>> edgeList;//以此顶点为起点的边的集合

        public VE(V v) {
            this.v = v;
            this.edgeList = new LinkedList<Edge<V>>();
        }

        @Override
        public String toString() {
            return "VE{" +
                    "v=" + v +
                    ", edgeList.size()=" + edgeList.size() +
                    '}';
        }

        /**
         * 读取某条边
         *
         * @param target 终点
         * @return 端点
         */
        public Edge<V> getEdge(V target) {
            Edge<V> res = null;
            if (target == null) {
                return null;
            }
            for (Edge<V> edge : edgeList) {
                if (edge.getTarget() != null && edge.getTarget().equals(target)) {
                    res = edge;
                    break;
                }
            }
            return res;
        }

        /**
         * 添加一条边,首先需要判断这条边是否存在,存在则不添加,不存在则添加
         *
         * @param edge 边
         */
        public void addEdge(Edge<V> edge) {
            if (getEdge(edge.getTarget()) == null) {
                edgeList.add(edge);
            } else {
                log.info("edge is already exist");
            }
        }

        /**
         * 删除一条边
         *
         * @param target 端点
         * @return 终点是 target 的边
         */
        public Edge<V> removeEdge(V target) {
            Edge<V> res = new Edge<V>();
            if (target == null) {
                return res;
            }
            for (Edge<V> edge : edgeList) {
                if (edge.getTarget() != null && edge.getTarget().equals(target)) {
                    log.info("remove edge:%s", edge);
                    res = edge;
                    edgeList.remove(res);
                    break;
                }
            }
            return res;
        }
    }

    /**
     * 广度优先遍历的迭代器
     */
    private class BFSIterator implements Iterator<V> {
        //已经访问过的顶点列表
        private List<V> visitedList = null;

        //待访问的顶点列表
        private Queue<V> queue = null;

        public BFSIterator(V v) {
            this.visitedList = new LinkedList<V>();
            this.queue = new LinkedList<V>();
            queue.offer(v);
        }

        @Override
        public boolean hasNext() {
            return queue.size() > 0;
        }

        @Override
        public V next() {
            V v = queue.poll();
            if (v == null) {
                return null;
            }
            VE ve = getVE(v);
            if (ve == null) {
                return null;
            }
            List<Edge<V>> edges = ve.edgeList;
            for (Edge<V> edge : edges) {
                V target = edge.getTarget();
                if (!visitedList.contains(target) && !queue.contains(target)) {
                    queue.offer(target);
                }
            }
            visitedList.add(v);
            return v;
        }

        @Override
        public void remove() {
            //TODO
        }
    }

    /**
     * 深度优先遍历迭代器
     */
    private class DFSIterator implements Iterator<V> {

        //已经访问过的顶点列表
        private List<V> visitedList = null;

        //待访问的顶点列表
        private Stack<V> stack = null;


        public DFSIterator(V v) {
            this.visitedList = new LinkedList<V>();
            this.stack = new Stack<V>();
            stack.add(v);
        }

        @Override
        public boolean hasNext() {
            return stack.size() > 0;
        }

        @Override
        public V next() {
            V v = stack.pop();
            if (v == null) {
                return null;
            }
            VE ve = getVE(v);
            if (ve == null) {
                return null;
            }
            List<Edge<V>> edges = ve.edgeList;
            for (Edge<V> edge : edges) {
                V target = edge.getTarget();
                if (!visitedList.contains(target)) {
                    stack.add(target);
                }
            }
            visitedList.add(v);
            return v;
        }

        @Override
        public void remove() {

        }
    }

    private LinkedList<VE> veList;

    public ListDGraph() {
        this.veList = new LinkedList<VE>();
    }

    @Override
    public int add(V v) {
        int index = -1;
        if (v != null) {
            VE ve = new VE(v);
            veList.add(ve);
            index = veList.indexOf(ve);
        }
        return index;
    }


    @Override
    public void add(Edge<V> edge) {
        if (edge == null) {
            return;
        }
        VE ve = getVE(edge.getSource());
        if (ve != null) {
            ve.addEdge(edge);
        } else {
            log.info("Error, can't find v : %s", edge.getSource());
        }
    }

    /**
     * 获取以 v 为起点的边
     *
     * @param v 端点
     * @return 以 v 为起点的边
     */
    private VE getVE(V v) {
        VE res = null;
        if (v == null) {
            return null;
        }
        for (VE ve : veList) {
            if (ve.v != null && ve.v.equals(v)) {
                res = ve;
                break;
            }
        }
        return res;
    }

    @Override
    public V remove(V v) {

        VE ve = removeVE(v);
        if (ve != null) {
            return ve.v;
        }
        removeRelateEdge(v);
        return null;
    }

    private VE removeVE(V v) {
        VE res = null;
        if (v == null) {
            return null;
        }
        for (VE ve : veList) {
            if (ve.v != null && ve.v.equals(v)) {
                res = ve;
                veList.remove(ve);
                break;
            }
        }
        return res;
    }

    private void removeRelateEdge(V v) {
        if (v == null) {
            return;
        }
        for (VE ve : veList) {
            ve.removeEdge(v);
        }
    }

    @Override
    public Edge<V> remove(Edge<V> edge) {
        Edge<V> res = null;
        if (edge == null) {
            return null;
        }
        V v = edge.getSource();
        VE ve = getVE(v);
        if (ve != null) {
            res = ve.removeEdge(edge.getTarget());
        }
        return res;
    }

    @Override
    public V get(int index) {
        V res = null;
        if (index < 0 || index >= veList.size()) {
            return null;
        }
        VE ve = veList.get(index);
        if (ve != null) {
            res = ve.v;
        }
        return res;
    }

    @Override
    public Edge<V> get(int source, int target) {
        Edge<V> edge = null;
        V s = get(source);
        V t = get(target);
        if (s == null || t == null) {
            return null;
        }
        VE ve = getVE(s);
        if (ve != null) {
            edge = ve.getEdge(t);
        }
        return edge;
    }

    @Override
    public Iterator<V> iterator(int type, V root) {
        Iterator<V> iterator = null;

        if (type == 0) {
            iterator = new DFSIterator(root);
        }
        if (type == 1) {
            iterator = new BFSIterator(root);
        }

        return iterator;
    }

    @Override
    public void convertDAG() {
    }
}
