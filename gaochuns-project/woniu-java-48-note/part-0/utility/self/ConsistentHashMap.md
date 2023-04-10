## 一致性 Hash 算法

```java
/**
 * 重点：
 * 1. 如何造一个 hash 环，
 * 2. 如何在哈希环上映射服务器节点，
 * 3. 如何找到对应的节点
 */
public class ConsistentHashMap<K, V> {

    //key表示服务器的hash值，value表示服务器
    private SortedMap<K, V> sortedMap;

    public ConsistentHashMap() {
        this.sortedMap = new TreeMap<>();
    }

    public void put(K key, V value) {
        sortedMap.put(key, value);
    }

    public V get(K key) {
        long hash = key.hashCode();
        // 得到大于该Hash值的所有Map
        SortedMap<K, V> subMap = sortedMap.tailMap(key);

        return subMap.isEmpty()
                ? sortedMap.get(sortedMap.firstKey()) // 如果没有比该key的hash值大的，则从第一个node开始
                : subMap.get(subMap.firstKey());      // 第一个Key就是顺时针过去离node最近的那个结点
    }

    public static void main(String[] args) {
        ConsistentHashMap<Integer, String> hashMap = new ConsistentHashMap<>();
        for (int i = 0; i < 100; i += 10)
            hashMap.put(i, String.valueOf(i));

        for (int i = 0; i < 100; i++)
            System.out.println(i + "被路由到结点[" + hashMap.get(i) + "]");
    }
}
```