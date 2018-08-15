package cn.finedo.daemon.gitlab.detail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.finedo.daemon.gitlab.domain.GitlabLog;
import cn.finedo.daemon.gitlab.domain.GitlabLogDetail;
import cn.finedo.fsdp.service.common.exception.TransactionException;
import cn.finedo.fsdp.service.common.jdbc.JdbcTemplate;

@Service
@Transactional
public class GitlabDetailService {
	@Resource(name="jdbcTemplate")
	private  JdbcTemplate jdbcTemplate;
	private static Logger logger = LogManager.getLogger(); 


	public void addLogs(List<GitlabLog> logs) {
		try{
			this.jdbcTemplate.batchUpdate(new StringBuffer()
			.append("delete from tb_rd_gitlablog where projectid=:projectid and fromversion=:fromversion and toversion=:toversion and tool=:tool and username=:username")
			.toString(), logs);
			
			this.jdbcTemplate.batchUpdate(new StringBuffer()
			.append("insert into tb_rd_gitlablog (statid,fromversion,toversion,projectid,author_name,author_email,username,opttime,addcount,removecount,tool,address,realaddress,commitid,shortid,branch,message) ")
			.append("values (:statid,:fromversion,:toversion,:projectid,:author_name,:author_email,:username,STR_TO_DATE(:opttime,'%Y-%m-%d %H:%i:%s'),:addcount,:removecount,:tool,:address,:realaddress,:commitid,:shortid,:branch,:message) ")
			.toString(), logs);
			logger.debug("添加汇总信息成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("添加汇总信息失败",e);
			throw new TransactionException(e);
		}
	}

	public void addLogDetails(List<GitlabLogDetail> logDetails) {
		try{
			
			this.jdbcTemplate.batchUpdate(new StringBuffer()
			.append("insert into tb_rd_gitlablog_detail(statid,detailid,codetype,filepath,addcount,removecount,filetype) ")
			.append("values (:statid,:detailid,:codetype,:filepath,:addcount,:removecount,:filetype) ")
			.toString(), logDetails);
			logger.debug("添加明细成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("添加明细失败",e);
			throw new TransactionException(e);
		}
	}
}
