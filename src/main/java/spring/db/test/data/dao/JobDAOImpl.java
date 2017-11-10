package spring.db.test.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import spring.db.test.data.Job;
import spring.db.test.data.JobStatus;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yamlin on 2017/2/23.
 */
@Repository
public class JobDAOImpl implements JobDAO {
    private static final String FIND_BY_ID_STMT = "SELECT * FROM jobs WHERE id = :id";

    private static final String FIND_BY_ID_FOR_UPDATE_STMT = "SELECT * FROM jobs WHERE id = :id FOR UPDATE";

    private static final String FIND_ALL_JOBS_STMT = "SELECT * FROM jobs";

    private static final String DELETE_JOB_STMT = "DELETE FROM jobs WHERE id = :id";

    private static final String ADD_JOB_STMT =
        "INSERT INTO jobs (id, head_sha, head_timestamp, status) "
            + "VALUES (:id, :headSHA, :headTimestamp, :status)";

    private static final String UPDATE_JOB_STMT =
        "UPDATE jobs SET "
            + "head_sha = :headSHA, head_timestamp = :headTimestamp, "
            + "last_success_sha = :lastSuccessSHA, last_success_timestamp = :lastSuccessTimestamp, "
            + "last_fail_sha = :lastFailSHA, last_fail_timestamp = :lastFailTimestamp, status = :status "
            + "WHERE id = :id";

    private final NamedParameterJdbcTemplate writeNamedParameterJdbcTemplate;
    private final NamedParameterJdbcTemplate readNamedParameterJdbcTemplate;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobRowMapper rowMapper;

    @Autowired
    public JobDAOImpl(@Qualifier("writeDS") DataSource writeDataSource,
                      @Qualifier("readDS") DataSource readDataSource,
                      PlatformTransactionManager platformTransactionManager
    ) {
        writeNamedParameterJdbcTemplate = new NamedParameterJdbcTemplate(writeDataSource);
        readNamedParameterJdbcTemplate = new NamedParameterJdbcTemplate(readDataSource);
        this.platformTransactionManager = platformTransactionManager;
        rowMapper = new JobRowMapper();
    }

    @Override
    public TransactionStatus startTransaction() {
        return platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    @Override
    public void commit(TransactionStatus status) {
        platformTransactionManager.commit(status);
    }

    @Override
    public Job getJob(String id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", Long.parseLong(id));

        //will throw EmptyResultDataAccessException
        return readNamedParameterJdbcTemplate.queryForObject(FIND_BY_ID_STMT, namedParams, rowMapper);
    }

    @Override
    public Job getJobForUpdate(String id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", Long.parseLong(id));

        //will throw EmptyResultDataAccessException
        return writeNamedParameterJdbcTemplate.queryForObject(FIND_BY_ID_FOR_UPDATE_STMT, namedParams, rowMapper);
    }

    @Override
    public List<Job> getJobList() {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        List<Job> projectList = readNamedParameterJdbcTemplate.query(FIND_ALL_JOBS_STMT, namedParams, rowMapper);
        return projectList
            .parallelStream()
            .filter(p -> p != null)
            .collect(Collectors.toList());
    }

    @Override
    public int addJob(Job job) {
        return writeNamedParameterJdbcTemplate.update(ADD_JOB_STMT, getSqlNamedParamsByJob(job));
    }

    @Override
    public int updateJob(Job job) {
        return writeNamedParameterJdbcTemplate.update(UPDATE_JOB_STMT, getSqlNamedParamsByJob(job));
    }

    @Override
    public int removeJob(String id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", Long.parseLong(id));
        return writeNamedParameterJdbcTemplate.update(DELETE_JOB_STMT, namedParams);
    }

    private SqlParameterSource getSqlNamedParamsByJob(Job job) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", Long.parseLong(job.getId()));
        namedParams.addValue("headSHA", job.getHeadSHA());
        namedParams.addValue("headTimestamp", getTimestamp(job.getHeadTimestamp()));
        namedParams.addValue("lastSuccessSHA", job.getLastSuccessSHA());
        namedParams.addValue("lastSuccessTimestamp", getTimestamp(job.getLastSuccessTimestamp()));
        namedParams.addValue("lastFailSHA", job.getLastFailSHA());
        namedParams.addValue("lastFailTimestamp", getTimestamp(job.getLastFailTimestamp()));
        namedParams.addValue("status", job.getStatus().ordinal());
        return namedParams;
    }

    private Timestamp getTimestamp(ZonedDateTime t) {
        if (t == null) {
            return null;
        } else {
            return Timestamp.from(t.toInstant());
        }
    }

    private static class JobRowMapper implements RowMapper<Job> {
        @Override
        public Job mapRow(ResultSet rs, int num) throws SQLException {
            Job job = new Job();
            job.setId(String.valueOf(rs.getLong("id")));
            job.setHeadSHA(rs.getString("head_sha"));
            job.setHeadTimestamp(getZonedDateTime(rs, "head_timestamp"));
            job.setLastSuccessSHA(rs.getString("last_success_sha"));
            job.setLastSuccessTimestamp(getZonedDateTime(rs, "last_success_timestamp"));
            job.setLastFailSHA(rs.getString("last_fail_sha"));
            job.setLastFailTimestamp(getZonedDateTime(rs, "last_fail_timestamp"));
            job.setCreatedDateTime(getZonedDateTime(rs, "created_time"));
            job.setUpdateDateTime(getZonedDateTime(rs, "update_time"));
            job.setStatus(JobStatus.values()[rs.getInt("status")]);

            return job;
        }

        ZonedDateTime getZonedDateTime(ResultSet rs, String fieldName) throws SQLException {
            Timestamp ts = rs.getTimestamp(fieldName);
            if (ts != null) {
                return ZonedDateTime.ofInstant(ts.toInstant(), ZoneOffset.UTC);
            }
            return null;
        }
    }
}
