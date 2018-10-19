public class test {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                if (i == 0 && j == 0 || i == 0 && j == 19 || i == 9 && j == 0 || i == 9 && j == 19) {
                    System.out.print('*');
                } else if (i > 0 && i < 9 && j>0 && j<19) {
                    System.out.print(' ');
                } else if (i == 0 || i == 9 ) {
                    System.out.print('-');
                } else if (j == 0 || j == 19 ) {
                    System.out.print('|');
                }
            }
            System.out.print('\n');
        }
    }

//    public int test() {
//        String s = "h";
//        char c = '[';
//    }
}
