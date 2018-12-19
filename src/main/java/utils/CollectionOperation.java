package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

//https://blog.csdn.net/qq_36787384/article/details/77116042
public class CollectionOperation {

    /**
     * 返回空的List集,不可写,下面是从继承关系上分析
     * 1.AbstractList实现List接口,实现了add(E e)方法
     * 2.AbstractList中的add方法的实现仅仅是抛出异常:UnsupportedOperationException()
     * public void add(int index, E element) {
     *     throw new UnsupportedOperationException();
     * }
     * 3.EmytyList继承AbstractList，没有重写add方法
     */
    private static void emptyList() {
        List<String> list = Collections.emptyList();
        list.add("lala");
//        list = new ArrayList<>();
//        list.add("haha");
        System.out.println(list);//此处会抛错
    }

    /**
     * 返回受检的List集合:可读可写
     */
    private static void checkedList () {
        List<Object> list = new ArrayList<>();
        list.add("123");
        list.add(new Integer(0));
        //指定list中存储的元素类型
        Class type = String.class;
        List checkedList = Collections.checkedList(list , type);
        checkedList.add("hello");
        //将integer类型的数据添加到受检的String元素类型的集合 抛错
        checkedList.add(new Integer(10));
        System.out.println(checkedList);
    }

    /**
     * 返回不可修改集合
     * 不可修改集合元素是针对的返回的不可修改的集合（指向的是原集合的引用），对原集合的修改，依然有效
     */
    public static void unmodifiableList () {
        List<Object> list = new ArrayList<>();
        list.add("123");
        List<Object> unmodifiableList = Collections.unmodifiableList(list);
        //unmodifiableList.add("456"); //不可添加元素
        list.add("456");
        System.out.println(unmodifiableList);
    }

    /**
     * 从继承关系上分析
     * 1.AbstractCollection实现Collection接口,实现了add(E e)方法
     * 2.AbstractCollection中的add方法的实现仅仅是抛出异常:UnsupportedOperationException().
     * public void add(int index, E element) {
     *     throw new UnsupportedOperationException();
     * }
     * 3.AbstractSet继承AbstractCollection接口,没有重写add(E e)方法
     * 4.EmytyList继承AbstractList，没有重写add方法
     */
    private static void singleton () {
        Object o = new Object();
        //返回单元素Set集合
        Set<Object> set = Collections.singleton(o);
        //返回单元素List集合
        Collections.singletonList(o);
        //返回单元素Map集合
        Collections.singletonMap("name" , "Mike");
        //不可修改是有限定条件的，取决于元素本身是否可以修改，element拿到的是元素的引用而已
        String s = "hello";
        Set<String> singletonSet = Collections.singleton(s);
        s = "world";
        System.out.println(singletonSet);//输出结果:[hello],单元素集合的元素并没有被改变，因为String本身是不可变的(immutable)
        List<Object> list = new ArrayList<>();
        list.add("123");
        Set<List<Object>> singleton = Collections.singleton(list);
        list.add("456");
        System.out.println(singleton);//输出结果:[[123, 456]].单元素集合的元素被改变，因为List本身是可变的(mutable)
        List list1 = Collections.singletonList(list);
        list.add("1");
        System.out.println(list1);
    }

    /**
     * 返回线程安全的集合
     * 线程安全、可读可写
     */
    private static void synchronizedList () {
        List<Object> list = new ArrayList<>();
        list.add("123");
        List<Object> synchronizedList = Collections.synchronizedList(list);
        System.out.println(synchronizedList);
        synchronizedList.add("hello");
        System.out.println(synchronizedList);
    }

    public static void main(String[] args) {
        //emptyList();
        //checkedList();
        //unmodifiableList();
        //singleton();
        synchronizedList();
    }

}
