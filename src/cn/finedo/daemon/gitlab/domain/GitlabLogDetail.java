package cn.finedo.daemon.gitlab.domain;

import cn.finedo.common.domain.BaseDomain;

public class GitlabLogDetail extends BaseDomain {
	private static final long serialVersionUID = 1L;
	private String detailid;
	private String statid;
	private String filepath;
	private String codetype;
	private int addcount;
	private int removecount;
	private String filetype;
	private String branch;
	
	
	
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public String getDetailid() {
		return detailid;
	}

	public void setDetailid(String detailid) {
		this.detailid = detailid;
	}

	public String getStatid() {
		return statid;
	}

	public void setStatid(String statid) {
		this.statid = statid;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getCodetype() {
		return codetype;
	}

	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}

	public int getAddcount() {
		return addcount;
	}

	public void setAddcount(int addcount) {
		this.addcount = addcount;
	}

	public int getRemovecount() {
		return removecount;
	}

	public void setRemovecount(int removecount) {
		this.removecount = removecount;
	}

	
}
