package spring.db.test;

/**
 * Created by joe321 on 2017/4/21.
 */
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import com.typesafe.config.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.db.test.data.Greeting;
import spring.db.test.data.Host;
import spring.db.test.data.Job;
import spring.db.test.data.JobController;
import spring.db.test.data.Project;
import spring.db.test.data.ProjectTest;
import spring.db.test.data.TESTTTT;
import spring.db.test.data.dao.JobDAO;
import spring.db.test.data.dao.LocalDbDao;

@Controller
@RequestMapping("/hello-world")
public class HelloWorldController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    Logger LOGGER = LoggerFactory.getLogger(HelloWorldController.class);

    private static final AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);
    private static final LocalDbDao dao = ac.getBean(LocalDbDao.class);
    private static final JobDAO jobDAO = ac.getBean(JobDAO.class);
    private static final JobController jobController = ac.getBean(JobController.class);
    private static final TESTTTT testttt = ac.getBean(TESTTTT.class);

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody
    Greeting sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {

        Host host = ac.getBean(Host.class);
        LOGGER.error("host name: " + host.getName());

        Greeting greeting = ac.getBean(Greeting.class);

        LOGGER.error("host name in greeting: " + greeting.getHost().getName());

        Config config = ac.getBean(Config.class);
        LOGGER.error(config.getString("db.writeUrl"));

      //  LocalDbDao dao = ac.getBean(LocalDbDao.class);

        return greeting;
    }

    @RequestMapping(value="/project/{id}", method=RequestMethod.GET)
    public @ResponseBody
    Project getProject( @PathVariable("id") String id) {

   //     LocalDbDao dao = ac.getBean(LocalDbDao.class);
        return dao.getProject(id);
    }

    @RequestMapping(value="/project", method=RequestMethod.POST)
    public @ResponseBody
    Project getProject() {

     //   LocalDbDao dao = ac.getBean(LocalDbDao.class);
        Project req = Project.getDefaultProject(Long.toString(System.currentTimeMillis()));
        dao.addProject(req);
        return req;
    }

    @RequestMapping(value="/project/{id}", method=RequestMethod.PUT)
    public @ResponseBody
    Project updateProject( @PathVariable("id") String id) {
       // LocalDbDao dao = ac.getBean(LocalDbDao.class);
        Project req = dao.getProject(id);
        req.setDescription(req.getDescription() + "_updated");
        TransactionStatus transactionStatus = dao.startTransaction();
        dao.printTransactionStatus(transactionStatus);
        dao.updateProject(req);
        dao.printTransactionStatus(transactionStatus);
        dao.commit(transactionStatus);
        dao.printTransactionStatus(transactionStatus);
        return req;
    }

    @RequestMapping(value="/project/{id}", method=RequestMethod.DELETE)
    public @ResponseBody
    int deleteProject( @PathVariable("id") String id) {

       // LocalDbDao dao = ac.getBean(LocalDbDao.class);
       // Project req = Project.getDefaultProject(Long.toString(System.currentTimeMillis()));
        dao.removeProject(id);
        return dao.removeProject(id);
    }

    @RequestMapping(value="/projectTest/lockw/{id}", method=RequestMethod.GET)
    public @ResponseBody
    void lockTestProjectByReadDSSlow( @PathVariable("id") String id) {

//        DataSourceTransactionManager manager = (DataSourceTransactionManager)ac.getBean(PlatformTransactionManager.class);
//        LOGGER.info("manager data source is " + manager.getDataSource().toString());
//        TransactionStatus status = manager.getTransaction(new DefaultTransactionDefinition());
        TransactionStatus transactionStatus = dao.startTransaction();
        dao.lockProjectByWriteDS(id);
        dao.updateProjectText(id, Long.toString(System.currentTimeMillis()));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        manager.commit(status);
        dao.commit(transactionStatus);
        return;
    }

//    @RequestMapping(value="/projectTest/lockr/{id}", method=RequestMethod.GET)
//    public @ResponseBody
//    void lockTestProjectByWriteDSSlow( @PathVariable("id") String id) {
//        //TransactionStatus transactionStatus = dao.startTransaction();
//        dao.startTransactionByStmt();
//        dao.lockProjectByWriteDS(id);
//
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        //dao.commit(transactionStatus);
//        dao.commitByStmt();
//        return;
//    }

    @RequestMapping(value="/projectTest/{id}", method=RequestMethod.GET)
    public @ResponseBody
    ProjectTest lockTestProject( @PathVariable("id") String id) {
        return dao.lockProjectByWriteDS(id);
    }

    @RequestMapping(value="/projectTest/get/{id}", method=RequestMethod.GET)
    public @ResponseBody
    ProjectTest getTestProject( @PathVariable("id") String id) {
        return dao.getProjectTest(id);
    }

    @RequestMapping(value="/projectTest/slow/{id}", method=RequestMethod.GET)
    public @ResponseBody
    ProjectTest getTestProjectSlow( @PathVariable("id") String id) {
        ProjectTest projectTest = dao.lockProjectByWriteDS(id);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return projectTest;
    }

    @Transactional
    @RequestMapping(value="/projectTest/{id}", method=RequestMethod.PUT)
    public @ResponseBody
    ProjectTest updateTestProject(@PathVariable("id") String id) {
        dao.lockProjectByWriteDS(id);
        dao.updateProjectText(id, "time: " + System.currentTimeMillis());
        return dao.lockProjectByWriteDS(id);
    }


    @RequestMapping(value="/job/slow/{id}", method=RequestMethod.GET)
    public @ResponseBody
    Job lockJob(@PathVariable("id") String id) {
        Job job = testttt._lockJob(id);
        //Job job = _lockJob(id);
        return job;
    }

//    @Transactional
//    public Job _lockJob(String id) {
//        Job job = jobDAO.getJobForUpdate(id);
//        ZonedDateTime requestCreatedTime = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
//        try {
//            int second = 0;
//            while (second < 10) {
//                Thread.sleep(1000);
//                second++;
//                LOGGER.error("In thread " + requestCreatedTime + ", sleep " + second + " second.");
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return job;
//    }

    @RequestMapping(value="/job/{id}/{sha}", method=RequestMethod.PUT)
    public @ResponseBody
    String addNewJob(@PathVariable("id") String id, @PathVariable("sha") String sha) {
        ZonedDateTime requestCreatedTime = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        Project project = new Project().setId(id).setHeadSHA(sha);
        String ret =  Boolean.toString(jobController.addJob(project, requestCreatedTime));
        return ret;
    }

    @RequestMapping(value="/job/{id}", method=RequestMethod.GET)
    public @ResponseBody
    Job getJob(@PathVariable("id") String id) {
        Job job = jobDAO.getJob(id);
        LOGGER.error("in db: " + dateToString(job.getHeadTimestamp()));
        ZonedDateTime requestCreatedTime = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        LOGGER.error("now: " + dateToString(requestCreatedTime));
        return job;
    }

    String dateToString(ZonedDateTime date) {
        if (date != null) {
            return date.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
        return null;
    }

    @RequestMapping(value="/job/{id}/{sha}/{isSuccess}", method=RequestMethod.PUT)
    public @ResponseBody
    int finishJob(@PathVariable("id") String id, @PathVariable("sha") String sha, @PathVariable("isSuccess") int isSuccess) {
        Project project = new Project().setId(id).setHeadSHA(sha);
        ZonedDateTime requestCreatedTime = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        String sign = dateToString(requestCreatedTime);
        JobController.Lock lock = jobController.getPushDocLock(project, requestCreatedTime);
        if (lock == null) {
            return -1;
        }
        try {
            int second = 0;
            while (second < 10) {
                Thread.sleep(1000);
                second++;
                LOGGER.error("In thread " + requestCreatedTime + ", sleep " + second + " second.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        jobController.finishPushDoc(lock, requestCreatedTime, isSuccess == 1);
        return isSuccess;
    }

    @RequestMapping(value="/jobtx/{id}/{sha}/{isSuccess}", method=RequestMethod.PUT)
    public @ResponseBody
    int finishJobWithTransaction(@PathVariable("id") String id, @PathVariable("sha") String sha, @PathVariable("isSuccess") int isSuccess) throws Exception {
        ZonedDateTime requestCreatedTime = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        Project project = new Project().setId(id).setHeadSHA(sha);
        try {
            jobController.pushDoc(
                project, requestCreatedTime, new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        try {
                            int second = 0;
                            while (second < 10) {
                                Thread.sleep(1000);
                                second++;
                                LOGGER.error("In thread " + requestCreatedTime + ", sleep " + second + " second.");
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if ("fail".equals(sha)) {
                            throw new Exception("job fail");
                        }
                        return null;
                    }
                }
            );
        } catch (Exception e) {
            LOGGER.error("get exception: " + e.getMessage());
        }
        return isSuccess;
        //int a = testttt._finishJobWithTransaction(id, sha, isSuccess);
        //return a;
    }

//    @Transactional
//    public int _finishJobWithTransaction(String id , String sha, int isSuccess) {
//        Project project = new Project().setId(id).setHeadSHA(sha);
//        ZonedDateTime requestCreatedTime = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
//        JobController.Lock lock = jobController.getPushDocLock(project, requestCreatedTime);
//        if (lock == null) {
//            return -1;
//        }
//        try {
//            int second = 0;
//            while (second < 10) {
//                Thread.sleep(1000);
//                second++;
//                LOGGER.error("In thread " + requestCreatedTime + ", sleep " + second + " second.");
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        jobController.finishPushDoc(lock, requestCreatedTime, isSuccess == 1);
//        return isSuccess;
//    }
}
