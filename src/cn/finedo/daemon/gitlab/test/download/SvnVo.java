package cn.finedo.daemon.gitlab.test.download;

/**
 * 
 * @author yangqingxin
 *
 */
public class SvnVo {

	//主键
 	private Integer svnId;
 	
	//svn项目名称
 	private String projectName;
 	
	//用户名
 	private String userName;
 	
	//密码
 	private String passWd;
 	
	//项目检出地址
 	private String checkPath;
 	
	//项目检出版本
 	private String svnVersion;
 	
 	//项目检出时间
 	private String svnTime;
 	
 	
 	
	public String getSvnTime() {
		return svnTime;
	}

	public void setSvnTime(String svnTime) {
		this.svnTime = svnTime;
	}
	public void setSvnId(Integer svnId) {
        this.svnId = svnId;
    }
    
    public Integer getSvnId() {
        return svnId;
    }
    
	public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
	public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }
    
	public void setPassWd(String passWd) {
        this.passWd = passWd;
    }
    
    public String getPassWd() {
        return passWd;
    }
    
	public void setCheckPath(String checkPath) {
        this.checkPath = checkPath;
    }
    
    public String getCheckPath() {
        return checkPath;
    }
    
	public void setSvnVersion(String svnVersion) {
        this.svnVersion = svnVersion;
    }
    
    public String getSvnVersion() {
        return svnVersion;
    }
    
	
    
}