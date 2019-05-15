package bastet

class SleepTest {
    public static void main(String[] args) {
        def url = "https://s.yimg.com/aw/api/res/1.2/iuXP6cl96O8pUsA.C4pNgA--/YXBwaWQ9eXR3YXVjdGlvbnNlcnZpY2U7aD0xNTA7cT04NTtyb3RhdGU9YXV0bztzcj0xLjI7c3M9MS4yO3c9MTA4/https://s.yimg.com/yn/image/b2421dcb-f29f-43c2-a31b-0d31ca65a2c7.jpg.cf.jpg"
        def proc = "sleep 5".execute()
        def proc2 = "sleep 5".execute()
        println "[Before validate ONE MRS URL] time: " + System.currentTimeMillis()
        proc.waitFor()
        println "[AFTER validate FIRST MRS URL] time: " + System.currentTimeMillis()
        proc2.waitFor()
        println "[AFTER validate SECOND MRS URL] time: " + System.currentTimeMillis()
        println proc.exitValue()
    }
}
