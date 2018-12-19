package utils;

import com.lm.demo.entity.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Predicate;

//http://zh.lucida.me/blog/java-8-lambdas-insideout-language-features/
//https://www.cnblogs.com/franson-2016/p/5593080.html
//https://blog.csdn.net/a879611951/article/details/80104014
public class LambdaOperation {

    public static void main(String[] args) {
        //baseOper();
        lambdaWithStream();
    }

    private static void lambdaWithStream() {
        List<Person> javaProgrammers = new ArrayList<Person>() {
            {
                add(new Person("Elsdon", "Jaycob", "Java programmer", "male", 43, 2000));
                add(new Person("Tamsen", "Brittany", "Java programmer", "female", 23, 1500));
                add(new Person("Floyd", "Donny", "Java programmer", "male", 33, 1800));
                add(new Person("Sindy", "Jonie", "Java programmer", "female", 32, 1600));
                add(new Person("Vere", "Hervey", "Java programmer", "male", 22, 1200));
                add(new Person("Maude", "Jaimie", "Java programmer", "female", 27, 1900));
                add(new Person("Shawn", "Randall", "Java programmer", "male", 30, 2300));
                add(new Person("Jayden", "Corrina", "Java programmer", "female", 35, 1700));
                add(new Person("Palmer", "Dene", "Java programmer", "male", 33, 2000));
                add(new Person("Addison", "Pam", "Java programmer", "female", 34, 1300));
            }
        };

        List<Person> phpProgrammers = new ArrayList<Person>() {
            {
                add(new Person("Jarrod", "Pace", "PHP programmer", "male", 34, 1550));
                add(new Person("Clarette", "Cicely", "PHP programmer", "female", 23, 1200));
                add(new Person("Victor", "Channing", "PHP programmer", "male", 32, 1600));
                add(new Person("Tori", "Sheryl", "PHP programmer", "female", 21, 1000));
                add(new Person("Osborne", "Shad", "PHP programmer", "male", 32, 1100));
                add(new Person("Rosalind", "Layla", "PHP programmer", "female", 25, 1300));
                add(new Person("Fraser", "Hewie", "PHP programmer", "male", 36, 1100));
                add(new Person("Quinn", "Tamara", "PHP programmer", "female", 21, 1000));
                add(new Person("Alvin", "Lance", "PHP programmer", "male", 38, 1600));
                add(new Person("Evonne", "Shari", "PHP programmer", "female", 40, 1800));
            }
        };
        System.out.println("所有程序员姓名：");
        javaProgrammers.forEach((p) -> System.out.printf(" %s %s " , p.getFirstName() , p.getLastName()));
        phpProgrammers.forEach((p) -> System.out.printf(" %s %s " , p.getFirstName() , p.getLastName()));
        System.out.println();
//        System.out.println("加薪");
//        Consumer<Person> giverRaise = e -> e.setSalary(e.getSalary()/100*5 + e.getSalary());
//        javaProgrammers.forEach(giverRaise);
//        phpProgrammers.forEach(giverRaise);
//        System.out.println("给程序员加薪 5% :");
//        Consumer<Person> giveRaise = e -> e.setSalary(e.getSalary() / 100 * 5 + e.getSalary());
//
//        javaProgrammers.forEach(giveRaise);
//        phpProgrammers.forEach(giveRaise);
//        System.out.println("下面是月薪超过 $1,400 的PHP程序员:");
//        phpProgrammers.stream()
//                .filter((p) -> (p.getSalary() > 1400))
//                .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));
//        System.out.println();
//        //定义filters
//        Predicate<Person> ageFilter = (p) -> (p.getAge() > 20);
//        Predicate<Person> salaryFilter = (p) -> (p.getSalary() > 1400);
        Predicate<Person> genderFilter = (p) -> (p.getGender().equals("female"));
//        System.out.println("下面是年龄大于 24岁且月薪在$1,400以上的女PHP程序员:");
//        phpProgrammers.stream()
//                .filter(ageFilter).filter(salaryFilter).filter(genderFilter)
//                .forEach((p) -> System.out.printf(" %s %s " , p.getFirstName() , p.getLastName()));
//        System.out.println();
//        System.out.println("最前面的三个 Java程序员");
//        javaProgrammers.stream().limit(3).forEach((p)-> System.out.printf(" %s %s " , p.getFirstName() , p.getLastName()));
//        System.out.println();
//        System.out.println("最前面的三个女性程序员");
//        javaProgrammers.stream().filter(genderFilter).limit(3).forEach((p)-> System.out.printf(" %s %s " , p.getFirstName() , p.getLastName()));
//        System.out.println();
//        System.out.println("根据name排序 ， 并显示前5个 Java程序员");
//        List<Person> sortedJavaProgrammers = javaProgrammers.stream().sorted((p , p1)->(p.getFirstName().compareTo(p1.getFirstName())))
//                .limit(5).collect(Collectors.toList());
//        System.out.println();
//        System.out.println("根据 salary 排序 Java programmers:");
//        sortedJavaProgrammers = javaProgrammers.stream().sorted((p , p1)->(p.getSalary()-p1.getSalary())).collect(Collectors.toList());
//        sortedJavaProgrammers.forEach((p) -> System.out.printf("%s %s; %n", p.getFirstName(), p.getLastName()));

//        System.out.println();
//        System.out.println("工资最低的Java程序员");
//        Person pers = javaProgrammers.stream().min((p1 , p2)->(p1.getSalary()-p2.getSalary())).get();
//        System.out.printf("Name: %s %s; Salary: $%,d.", pers.getFirstName(), pers.getLastName(), pers.getSalary());
//
//        System.out.println();
//        System.out.println("工资最高的 Java programmer:");
//        Person person = javaProgrammers
//                .stream()
//                .max((p, p2) -> (p.getSalary() - p2.getSalary()))
//                .get();
//
//        System.out.printf("Name: %s %s; Salary: $%,d.", person.getFirstName(), person.getLastName(), person.getSalary());
//        System.out.println();
//        System.out.println("将 PHP programmers 的 first name 拼接成字符串:");
//        String phpDevelopers = phpProgrammers.stream().map(Person::getFirstName).collect(Collectors.joining(" ; ")); // 在进一步的操作中可以作为标记(token)
//        System.out.println(phpDevelopers);
//        System.out.println("将 Java programmers 的 first name 存放到 Set:");
//        Set<String> javaDevFirstName = javaProgrammers.stream().map(Person::getFirstName).collect(Collectors.toSet());
//        System.out.println(javaDevFirstName);
//        System.out.println();
//        System.out.println("将 Java programmers 的 first name 存放到 TreeSet:");
//        TreeSet<String> treeSet = javaProgrammers.stream().map(Person::getFirstName).collect(Collectors.toCollection(TreeSet::new));
//        System.out.println(treeSet);
//        System.out.println();
//        System.out.println("计算付给 Java programmers 的所有money:");
//        int total = javaProgrammers.parallelStream().mapToInt(p-> p.getSalary()).sum();
//        System.out.println(total);
        System.out.println();
        //计算 count, min, max, sum, and average for numbers
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        IntSummaryStatistics stats = numbers
                .stream()
                .mapToInt((x) -> x)
                .summaryStatistics();

        System.out.println("List中最大的数字 : " + stats.getMax());
        System.out.println("List中最小的数字 : " + stats.getMin());
        System.out.println("所有数字的总和   : " + stats.getSum());
        System.out.println("所有数字的平均值 : " + stats.getAverage());
    }


    private static void baseOper() {
        String[] atp = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka",
                "David Ferrer","Roger Federer",
                "Andy Murray","Tomas Berdych",
                "Juan Martin Del Potro"};
        //循环
        List<String> players = Arrays.asList(atp);
        players.forEach( player -> System.out.println(player + ";"));
        //players.forEach(System.out::println);
        //多线程
        new Thread(()-> System.out.println("hello world")).start();
        Runnable runnable = () -> System.out.println("hello world");
        runnable.run();
        //集合操作
        String[] sporters = {"Rafael Nadal", "Novak Djokovic",
                "Stanislas Wawrinka", "David Ferrer",
                "Roger Federer", "Andy Murray",
                "Tomas Berdych", "Juan Martin Del Potro",
                "Richard Gasquet", "John Isner"};
        Arrays.sort(sporters, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        Comparator<String> sortByName = (String o1 , String o2) -> (o1.compareTo(o2));
        Arrays.sort(sporters , sortByName);
        Arrays.sort(sporters , (String o1 , String o2) -> (o1.compareTo(o2)));
    }
}
