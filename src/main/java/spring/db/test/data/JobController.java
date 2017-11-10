package spring.db.test.data;

import org.springframework.transaction.TransactionStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by joe321 on 2017/4/26.
 */
public interface JobController {

    void pushDoc(Project project, ZonedDateTime requestCreatedTime, Callable<Void> pushDocJob) throws Exception;

    class Lock {
        TransactionStatus transactionStatus;
        Job job;
        public String buildingSHA;

        Lock(TransactionStatus transactionStatus, Job job, String buildingSHA) {
            this.transactionStatus = transactionStatus;
            this.job = job;
            this.buildingSHA = buildingSHA;
        }
    }

    void setJobStatusIfNewest(String id, ZonedDateTime requestCreatedTime, JobStatus fail);

    boolean addJob(Project project, ZonedDateTime requestCreatedTime);

    Lock getPushDocLock(Project project, ZonedDateTime requestCreatedTime);

    void finishPushDoc(Lock lock, ZonedDateTime requestCreatedTime, boolean isSuccess);

    Job getJob(String id);

    List<Job> getJobList();
}
