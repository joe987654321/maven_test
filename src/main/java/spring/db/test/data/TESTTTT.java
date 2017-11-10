package spring.db.test.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import spring.db.test.data.dao.JobDAO;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Created by joe321 on 2017/5/4.
 */
@Transactional
@Component
public class TESTTTT {

    private final JobDAO jobDAO;
    private final JobController jobController;
    Logger LOGGER = LoggerFactory.getLogger(TESTTTT.class);

    @Autowired
    TESTTTT(JobController jobController, JobDAO jobDAO) {
        this.jobController = jobController;
        this.jobDAO = jobDAO;
    }

    @Transactional
    public Job _lockJob(String id) {
        Job job = jobDAO.getJobForUpdate(id);
        ZonedDateTime requestCreatedTime = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
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
        return job;
    }

    public int _finishJobWithTransaction(String id , String sha, int isSuccess) {
        Project project = new Project().setId(id).setHeadSHA(sha);
        ZonedDateTime requestCreatedTime = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
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
}
