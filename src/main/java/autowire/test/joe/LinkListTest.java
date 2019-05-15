package autowire.test.joe;

public class LinkListTest {

    public static void main(String[] args) {
        LinkListTest linkListTest = new LinkListTest();


    }

    public ListNode reverse(ListNode node) {
        if (node == null || node.next == null) return node;
        ListNode first = node;
        ListNode second = node.next;
        node.next = null;
        while (second != null) {
            ListNode tmp = first;
            first = second;
            second = second.next;
            first.next = tmp;
        }
        return first;
    }


    private static class ListNode {
        public ListNode next;
        public int val;
    }
}
