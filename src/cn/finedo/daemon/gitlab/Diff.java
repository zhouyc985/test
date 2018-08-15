package cn.finedo.daemon.gitlab;

import java.io.File;

import cn.finedo.common.domain.BaseDomain;

public class Diff extends BaseDomain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String old_path;
	private String new_path;
	private int a_mode;
	private int b_mode;
	private String diff;
	private boolean new_file;
	private boolean renamed_file;
	private boolean deleted_file;
	public String getOld_path() {
		return old_path;
	}
	public void setOld_path(String old_path) {
		this.old_path = old_path;
	}
	public String getNew_path() {
		return new_path;
	}
	public void setNew_path(String new_path) {
		this.new_path = new_path;
	}

	public int getA_mode() {
		return a_mode;
	}
	public void setA_mode(int a_mode) {
		this.a_mode = a_mode;
	}
	public int getB_mode() {
		return b_mode;
	}
	public void setB_mode(int b_mode) {
		this.b_mode = b_mode;
	}

	
	
	public String getDiff() {
		return diff;
	}
	public void setDiff(String diff) {
		this.diff = diff;
	}
	public boolean isNew_file() {
		return new_file;
	}
	public void setNew_file(boolean new_file) {
		this.new_file = new_file;
	}
	public boolean isRenamed_file() {
		return renamed_file;
	}
	public void setRenamed_file(boolean renamed_file) {
		this.renamed_file = renamed_file;
	}
	public boolean isDeleted_file() {
		return deleted_file;
	}
	public void setDeleted_file(boolean deleted_file) {
		this.deleted_file = deleted_file;
	}
	
	
	
	
	
	
	
	
	
}
