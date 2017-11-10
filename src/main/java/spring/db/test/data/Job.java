package spring.db.test.data;

import java.time.ZonedDateTime;

/**
 * Created by joe321 on 2017/4/26.
 */
public class Job {
    private String id;
    private String headSHA;
    private ZonedDateTime headTimestamp;
    private String lastSuccessSHA;
    private ZonedDateTime lastSuccessTimestamp;
    private String lastFailSHA;
    private ZonedDateTime lastFailTimestamp;
    private JobStatus status;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updateDateTime;

    public String getId() {
        return id;
    }

    public Job setId(String id) {
        this.id = id;
        return this;
    }

    public String getHeadSHA() {
        return headSHA;
    }

    public Job setHeadSHA(String headSHA) {
        this.headSHA = headSHA;
        return this;
    }

    public ZonedDateTime getHeadTimestamp() {
        return headTimestamp;
    }

    public Job setHeadTimestamp(ZonedDateTime headTimestamp) {
        this.headTimestamp = headTimestamp;
        return this;
    }

    public String getLastSuccessSHA() {
        return lastSuccessSHA;
    }

    public Job setLastSuccessSHA(String lastSuccessSHA) {
        this.lastSuccessSHA = lastSuccessSHA;
        return this;
    }

    public ZonedDateTime getLastSuccessTimestamp() {
        return lastSuccessTimestamp;
    }

    public Job setLastSuccessTimestamp(ZonedDateTime lastSuccessTimestamp) {
        this.lastSuccessTimestamp = lastSuccessTimestamp;
        return this;
    }

    public String getLastFailSHA() {
        return lastFailSHA;
    }

    public Job setLastFailSHA(String lastFailSHA) {
        this.lastFailSHA = lastFailSHA;
        return this;
    }

    public ZonedDateTime getLastFailTimestamp() {
        return lastFailTimestamp;
    }

    public Job setLastFailTimestamp(ZonedDateTime lastFailTimestamp) {
        this.lastFailTimestamp = lastFailTimestamp;
        return this;
    }

    public JobStatus getStatus() {
        return status;
    }

    public Job setStatus(JobStatus status) {
        this.status = status;
        return this;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public Job setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    public ZonedDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public Job setUpdateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
        return this;
    }
}
