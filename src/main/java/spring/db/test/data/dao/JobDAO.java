package spring.db.test.data.dao;


import org.springframework.transaction.TransactionStatus;
import spring.db.test.data.Job;

import java.util.List;

/**
 * Created by joe321 on 2017/4/26.
 */
public interface JobDAO {
    Job getJob(String id);
    Job getJobForUpdate(String id);
    int updateJob(Job job);
    int addJob(Job job);
    int removeJob(String id);
    List<Job> getJobList();

    TransactionStatus startTransaction();
    void commit(TransactionStatus status);
}
