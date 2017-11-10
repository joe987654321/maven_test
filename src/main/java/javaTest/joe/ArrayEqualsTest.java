package javaTest.joe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by joe321 on 2017/2/22.
 */
public class ArrayEqualsTest {
    public static class MyInt {
        public int v;

        public boolean equals(Object obj) {
            System.out.println("in equals");

            if (!(obj instanceof MyInt)) {
                return false;
            }

            MyInt myInt = (MyInt) obj;

            if (v - myInt.v < 2 && v - myInt.v > -2) {
                return true;
            }
            else return false;
        }

        public MyInt(int v) {
            this.v = v;
        }
    }

    public static void main(String[] args) {
        MyInt myInt1 = new MyInt(1);
        MyInt myInt_1 = new MyInt(1);
        MyInt myInt2 = new MyInt(2);
        MyInt myInt_2 = new MyInt(2);
        MyInt myInt3 = new MyInt(3);

//        List<MyInt> myIntList1 = new ArrayList<>();
       // myIntList1.add(myInt1);
     //   myIntList1.add(myInt2);
//        List<MyInt> myIntList2 = new ArrayList<>();
   //     myIntList2.add(myInt1);
   //     myIntList2.add(myInt2);
        List<MyInt> myIntList3 = new ArrayList<>();
        myIntList3.add(myInt_1);
        myIntList3.add(myInt_2);

        List<Set<Integer>> a = new ArrayList<>();
        a.add(new HashSet<Integer>(1) );
        List<Set<Integer>> b = new ArrayList<>();
        b.add(new HashSet<Integer>(2));
        System.out.println(a.equals(b));

        List<MyInt> myIntList1 = new ArrayList<>();
        myIntList1.add(myInt1);
        List<MyInt> myIntList2 = new ArrayList<>();
        myIntList2.add(myInt_1);
        System.out.println(myIntList1.equals(myIntList2));

    }
}
