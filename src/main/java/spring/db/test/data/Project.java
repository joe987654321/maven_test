package spring.db.test.data;

import java.time.ZonedDateTime;

/**
 * Project domain object.
 * Created by baiyi on 2/9/17.
 */
public class Project {
    private String id;
    private String name;
    private String description;
    private String custodian;
    private String headSHA;
    private String rdlSrcPath;
    private String docSrcPath;
    private String docUrl;
    private String reviewUrl;
    private String splunkDashboardUrl;
    private String yamasUrl;
    private String testHostUrl;
    private ZonedDateTime createdDateTime;
    private ZonedDateTime updateDateTime;
    private long webHookId;
    private String repoUrl;

    public Project() {
    }

    public Project(String repoUrl)  {
        this.repoUrl = repoUrl;
    }

    public String getCustodian() {
        return custodian;
    }

    public Project setCustodian(String custodian) {
        this.custodian = custodian;
        return this;
    }

    public String getHeadSHA() {
        return headSHA;
    }

    public Project setHeadSHA(String headSHA) {
        this.headSHA = headSHA;
        return this;
    }

    public String getId() {
        return id;
    }

    public Project setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getRdlSrcPath() {
        return rdlSrcPath;
    }

    public Project setRdlSrcPath(String rdlPath) {
        this.rdlSrcPath = rdlPath;
        return this;
    }

    public String getDocSrcPath() {
        return docSrcPath;
    }

    public Project setDocSrcPath(String docPath) {
        this.docSrcPath = docPath;
        return this;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public Project setDocUrl(String url) {
        this.docUrl = url;
        return this;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public Project setReviewUrl(String url) {
        this.reviewUrl = url;
        return this;
    }

    public String getSplunkDashboardUrl() {
        return splunkDashboardUrl;
    }

    public Project setSplunkDashboardUrl(String splunkDashboardUrl) {
        this.splunkDashboardUrl = splunkDashboardUrl;
        return this;
    }

    public String getYamasUrl() {
        return yamasUrl;
    }

    public Project setYamasUrl(String yamasUrl) {
        this.yamasUrl = yamasUrl;
        return this;
    }

    public String getTestHostUrl() {
        return testHostUrl;
    }

    public Project setTestHostUrl(String testHostUrl) {
        this.testHostUrl = testHostUrl;
        return this;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public Project setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    public ZonedDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public Project setUpdateDateTime(ZonedDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
        return this;
    }


    public long getWebHookId() {
        return webHookId;
    }

    public Project setWebHookId(long webHookId) {
        this.webHookId = webHookId;
        return this;
    }

    public static Project getDefaultProject(String id) {
        return new Project("test repourl").setId(id).setName("joe project").setCustodian("test custodian")
            .setDescription("test description").setDocSrcPath("test docsrcpath")
            .setRdlSrcPath("test rdlsrcpath").setHeadSHA("test sha").setWebHookId(11111);
    }


    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }
}
