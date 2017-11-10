package spring.db.test.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import spring.db.test.data.dao.JobDAO;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by joe321 on 2017/4/26.
 */
@Service
public class JobControllerImpl implements JobController {


    private final JobDAO jobDAO;

    @Autowired
    public JobControllerImpl(JobDAO jobDAO) {
        this.jobDAO = jobDAO;
    }

    @Override
    @Transactional
    public void setJobStatusIfNewest(String id, ZonedDateTime requestCreatedTime, JobStatus status) {
//        TransactionStatus transactionStatus = jobDAO.startTransaction();
        Job job = jobDAO.getJobForUpdate(id);
        //only the newest sha has the privilege to renew status
        if (job.getHeadTimestamp().equals(requestCreatedTime)) {
            job.setStatus(status);
            jobDAO.updateJob(job);
        }
  //      jobDAO.commit(transactionStatus);
    }

    @Override
    @Transactional
    public boolean addJob(Project project, ZonedDateTime requestCreatedTime) {
//        TransactionStatus transactionStatus = jobDAO.startTransaction();
        Job job = null;

        try {
            job = jobDAO.getJobForUpdate(project.getId());
        } catch (DataAccessException e) {
            //need add new job
            job = new Job().setId(project.getId()).setHeadSHA(project.getHeadSHA())
                .setHeadTimestamp(requestCreatedTime).setStatus(JobStatus.BUILDING);
            jobDAO.addJob(job);
        //    jobDAO.commit(transactionStatus);
            return true;
        }
        //check if current job is needed
        if (!isEqualOrOlder(job.getLastSuccessTimestamp(), requestCreatedTime)) {
         //   jobDAO.commit(transactionStatus);
            return false;
        }
        //check if current job is the newest
        if (isEqualOrOlder(job.getHeadTimestamp(), requestCreatedTime)) {
            job.setHeadSHA(project.getHeadSHA()).setHeadTimestamp(requestCreatedTime);
        }
        job.setStatus(JobStatus.BUILDING);
        jobDAO.updateJob(job);
       // jobDAO.commit(transactionStatus);
        return true;
    }

    //http://stackoverflow.com/questions/7890056/exception-propogates-after-already-caught
    @Override
    @Transactional(noRollbackForClassName = "java.lang.Exception")
    public void pushDoc(Project project, ZonedDateTime requestCreatedTime, Callable<Void> pushDocJob) throws Exception {
        Job job = jobDAO.getJobForUpdate(project.getId());
        if (!isEqualOrOlder(job.getLastSuccessTimestamp(), requestCreatedTime)) {
            return;
        }

        try {
            pushDocJob.call();
        } catch (Exception e) {
            if (isEqualOrOlder(job.getLastFailTimestamp(), requestCreatedTime)) {
                job.setLastFailSHA(project.getHeadSHA()).setLastFailTimestamp(requestCreatedTime);
                if (job.getHeadTimestamp().equals(requestCreatedTime)) {
                    job.setStatus(JobStatus.FAIL);
                }
                jobDAO.updateJob(job);
            }
            throw e;
        }

        if (isEqualOrOlder(job.getLastSuccessTimestamp(), requestCreatedTime)) {
            job.setLastSuccessSHA(project.getHeadSHA()).setLastSuccessTimestamp(requestCreatedTime);
            if (job.getHeadTimestamp().equals(requestCreatedTime)) {
                job.setStatus(JobStatus.SUCCESS);
            }
            jobDAO.updateJob(job);
        }
    }

    @Override
    @Transactional
    public Lock getPushDocLock(Project project, ZonedDateTime requestCreatedTime) {
       // TransactionStatus transactionStatus = jobDAO.startTransaction();
        Job job = jobDAO.getJobForUpdate(project.getId());
        if (!isEqualOrOlder(job.getLastSuccessTimestamp(), requestCreatedTime)) {
         //   jobDAO.commit(transactionStatus);
            return null;
        }
        //return new Lock(transactionStatus, job, project.getHeadSHA());
        return new Lock(null, job, project.getHeadSHA());
    }

    @Override
    public void finishPushDoc(Lock lock, ZonedDateTime requestCreatedTime, boolean isSuccess) {
        Job job = lock.job;
        if (isSuccess) {
            if (isEqualOrOlder(job.getLastSuccessTimestamp(), requestCreatedTime)) {
                job.setLastSuccessSHA(lock.buildingSHA).setLastSuccessTimestamp(requestCreatedTime);
                if (job.getHeadTimestamp().equals(requestCreatedTime)) {
                    job.setStatus(JobStatus.SUCCESS);
                }
                jobDAO.updateJob(job);
            }
        } else {
            if (isEqualOrOlder(job.getLastFailTimestamp(), requestCreatedTime)) {
                job.setLastFailSHA(lock.buildingSHA).setLastFailTimestamp(requestCreatedTime);
                if (job.getHeadTimestamp().equals(requestCreatedTime)) {
                    job.setStatus(JobStatus.FAIL);
                }
                jobDAO.updateJob(job);
            }
        }
        //jobDAO.commit(lock.transactionStatus);
    }

    @Override
    public Job getJob(String id) {
        return jobDAO.getJob(id);
    }

    @Override
    public List<Job> getJobList() {
        return jobDAO.getJobList();
    }

    private boolean isEqualOrOlder(ZonedDateTime base, ZonedDateTime now) {
        if (base == null || base.compareTo(now) <= 0) {
            return true;
        }
        return false;
    }
}
