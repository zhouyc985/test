package cn.finedo.daemon.gitlab.domain;

import java.util.List;

import cn.finedo.daemon.gitlab.Commit;

public class Branch {
	private String name;
	private String merged;
	private String developers_can_merge;
	Commit commit;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMerged() {
		return merged;
	}
	public void setMerged(String merged) {
		this.merged = merged;
	}
	public String getDevelopers_can_merge() {
		return developers_can_merge;
	}
	public void setDevelopers_can_merge(String developers_can_merge) {
		this.developers_can_merge = developers_can_merge;
	}
	public Commit getCommit() {
		return commit;
	}
	public void setCommit(Commit commit) {
		this.commit = commit;
	}
	
		

}
