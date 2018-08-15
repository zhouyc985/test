package cn.finedo.daemon.gitlab;

import java.util.Date;

import cn.finedo.common.domain.BaseDomain;

public class Commit extends BaseDomain{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String id;
private String short_id;
private String title;
private String author_name;
private String author_email;
private Date created_at;
private String message;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getShort_id() {
	return short_id;
}
public void setShort_id(String short_id) {
	this.short_id = short_id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getAuthor_name() {
	return author_name;
}
public void setAuthor_name(String author_name) {
	this.author_name = author_name;
}
public String getAuthor_email() {
	return author_email;
}
public void setAuthor_email(String author_email) {
	this.author_email = author_email;
}
public Date getCreated_at() {
	return created_at;
}
public void setCreated_at(Date created_at) {
	this.created_at = created_at;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}


}
