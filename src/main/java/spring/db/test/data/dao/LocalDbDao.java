package spring.db.test.data.dao;

import com.typesafe.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import spring.db.test.data.Project;
import spring.db.test.data.ProjectTest;

import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Created by joe321 on 2017/4/21.
 */
@Component
public class LocalDbDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDbDao.class);

    private static final String FIND_BY_ID_STMT = "SELECT * FROM projects WHERE id = :id";

    private static final String SELECT_FOR_UPDATE_BY_ID_STMT = "SELECT * FROM projects_test WHERE id = :id FOR UPDATE";
    private static final String SELECT_BY_ID_STMT = "SELECT * FROM projects_test WHERE id = :id";

    private static final String UPDATE_TEXT_STMT =
        "UPDATE projects_test SET "
            + "text = :text "
            + "WHERE id = :id";

    private static final String DELETE_PROJECT_STMT = "DELETE FROM projects WHERE id = :id";

    private static final String ADD_PROJECT_STMT =
            "INSERT INTO projects (id, name, description, custodian, repo_url, "
            + "repo_head_sha, rdl_src_path, doc_src_path, doc_url, "
            + "review_url, splunk_dashboard_url, yamas_url, test_host_url, web_hook_id) "
            + "VALUES (:id, :name, :description, :custodian, :repoUrl, "
            + ":headSHA, :rdlSrcPath, :docSrcPath, :docUrl, "
            + ":reviewUrl, :splunkDashboardUrl, :yamasUrl, :testHostUrl, :webHookId)";

    private static final String UPDATE_PROJECT_STMT =
        "UPDATE projects SET "
            + "name = :name, description = :description, custodian = :custodian, "
            + "repo_url = :repoUrl, repo_head_sha = :headSHA, "
            + "rdl_src_path = :rdlSrcPath, doc_src_path = :docSrcPath, doc_url = :docUrl, "
            + "review_url = :reviewUrl, splunk_dashboard_url = :splunkDashboardUrl, "
            + "test_host_url = :testHostUrl, web_hook_id = :webHookId "
            + "WHERE id = :id";


    private final NamedParameterJdbcTemplate writeNamedParameterJdbcTemplate;
    //private final NamedParameterJdbcTemplate write2NamedParameterJdbcTemplate;
    private final NamedParameterJdbcTemplate readNamedParameterJdbcTemplate;
    private final PlatformTransactionManager platformTransactionManager;
    private final ProjectRowMapper rowMapper;
    private final ProjectRowMapperTest rowMapperTest;

    @Autowired
    public LocalDbDao(@Qualifier("writeDS") DataSource writeDataSource,
                    //  @Qualifier("write2DS") DataSource write2DataSource,
                      @Qualifier("readDS") DataSource readDataSource,
                      PlatformTransactionManager platformTransactionManager) {
        writeNamedParameterJdbcTemplate = new NamedParameterJdbcTemplate(writeDataSource);
       // write2NamedParameterJdbcTemplate = new NamedParameterJdbcTemplate(write2DataSource);
        readNamedParameterJdbcTemplate = new NamedParameterJdbcTemplate(readDataSource);

        this.platformTransactionManager = platformTransactionManager;
        rowMapper = new ProjectRowMapper();
        rowMapperTest = new ProjectRowMapperTest();
    }

    @Transactional
    public int addProject(Project project) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(project);
        return writeNamedParameterJdbcTemplate.update(ADD_PROJECT_STMT, source);
    }

    @Transactional
    public Project getProject(String id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", Long.parseLong(id));

        //will throw EmptyResultDataAccessException
        return readNamedParameterJdbcTemplate.queryForObject(FIND_BY_ID_STMT, namedParams, rowMapper);
    }

    public TransactionStatus startTransaction() {
        return platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    public void commit(TransactionStatus status) {
        platformTransactionManager.commit(status);
    }

    public void printTransactionStatus(TransactionStatus status) {
        LOGGER.info("Transaction is complete: " + status.isCompleted());
    }

    public void startTransactionByStmt() {
        writeNamedParameterJdbcTemplate.update("start transaction", (SqlParameterSource) null);
    }

    public void commitByStmt() {
        writeNamedParameterJdbcTemplate.update("commit", (SqlParameterSource) null);
    }


//    @Transactional
    public int updateProject(Project project) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(project);
        return writeNamedParameterJdbcTemplate.update(UPDATE_PROJECT_STMT, source);
    }

    @Transactional
    public int removeProject(String id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", Long.parseLong(id));
        return writeNamedParameterJdbcTemplate.update(DELETE_PROJECT_STMT, namedParams);
    }

//    public ProjectTest lockProjectByReadDS(String id) {
//        MapSqlParameterSource namedParams = new MapSqlParameterSource();
//        namedParams.addValue("id", Long.parseLong(id));
//
//        //will throw EmptyResultDataAccessException
//
//        JdbcTemplate t = (JdbcTemplate) readNamedParameterJdbcTemplate.getJdbcOperations();
//        LOGGER.info("in dao read datasource is " + t.getDataSource().toString());
//        HikariDataSource hds = (HikariDataSource) t.getDataSource();
//        LOGGER.info("in dao read datasource autocommit is " + hds.isAutoCommit());
//        //LOGGER.info("in dao read datasource isolation is " + hds.getTransactionIsolation());
//
//        return readNamedParameterJdbcTemplate.queryForObject(SELECT_FOR_UPDATE_BY_ID_STMT, namedParams, rowMapperTest);
//    }

    public ProjectTest getProjectTest(String id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", Long.parseLong(id));

        return readNamedParameterJdbcTemplate.queryForObject(SELECT_BY_ID_STMT, namedParams, rowMapperTest);
    }

    public ProjectTest lockProjectByWriteDS(String id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", Long.parseLong(id));

        //will throw EmptyResultDataAccessException
        JdbcTemplate t = (JdbcTemplate) writeNamedParameterJdbcTemplate.getJdbcOperations();
        LOGGER.info("in dao write datasource is " + t.getDataSource().toString());
        HikariDataSource hds = (HikariDataSource) t.getDataSource();
        LOGGER.info("in dao write datasource autocommit is " + hds.isAutoCommit());
        //LOGGER.info("in dao write datasource isolation is " + hds.getTransactionIsolation());

        return writeNamedParameterJdbcTemplate.queryForObject(SELECT_FOR_UPDATE_BY_ID_STMT, namedParams, rowMapperTest);
    }

//    public ProjectTest lockProjectByWrite2DS(String id) {
//        MapSqlParameterSource namedParams = new MapSqlParameterSource();
//        namedParams.addValue("id", Long.parseLong(id));
//
//        //will throw EmptyResultDataAccessException
//        JdbcTemplate t = (JdbcTemplate) write2NamedParameterJdbcTemplate.getJdbcOperations();
//        LOGGER.info("in dao write 2 datasource is " + t.getDataSource().toString());
//        HikariDataSource hds = (HikariDataSource) t.getDataSource();
//        LOGGER.info("in dao write 2 datasource autocommit is " + hds.isAutoCommit());
//        //LOGGER.info("in dao write datasource isolation is " + hds.getTransactionIsolation());
//
//        return write2NamedParameterJdbcTemplate.queryForObject(SELECT_FOR_UPDATE_BY_ID_STMT, namedParams, rowMapperTest);
//    }

    public int updateProjectText(String id, String text) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", Long.parseLong(id));
        namedParams.addValue("text", text);

        //will throw EmptyResultDataAccessException
        return writeNamedParameterJdbcTemplate.update(UPDATE_TEXT_STMT, namedParams);
    }

    private static class ProjectRowMapperTest implements RowMapper<ProjectTest> {
        @Override
        public ProjectTest mapRow(ResultSet rs, int num) throws SQLException {
            ProjectTest projectTest = new ProjectTest();
            projectTest.setId(String.valueOf(rs.getLong("id")));
            projectTest.setText(rs.getString("text"));

            return projectTest;
        }
    }

    private static class ProjectRowMapper implements RowMapper<Project> {
        @Override
        public Project mapRow(ResultSet rs, int num) throws SQLException {
            String repoUrl = rs.getString("repo_url");

                Project project = new Project(repoUrl);
                project.setId(String.valueOf(rs.getLong("id")));
                project.setName(rs.getString("name"));
                project.setDescription(rs.getString("description"));
                project.setCustodian(rs.getString("custodian"));
                project.setHeadSHA(rs.getString("repo_head_sha"));
                project.setRdlSrcPath(rs.getString("rdl_src_path"));
                project.setDocSrcPath(rs.getString("doc_src_path"));
                project.setDocUrl(rs.getString("doc_url"));
                project.setReviewUrl(rs.getString("review_url"));
                project.setSplunkDashboardUrl(rs.getString("splunk_dashboard_url"));
                project.setYamasUrl(rs.getString("yamas_url"));
                project.setTestHostUrl(rs.getString("test_host_url"));
                project.setCreatedDateTime(getZonedDateTime(rs, "created_time"));
                project.setUpdateDateTime(getZonedDateTime(rs, "update_time"));
                project.setWebHookId(rs.getLong("web_hook_id"));

                return project;

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
