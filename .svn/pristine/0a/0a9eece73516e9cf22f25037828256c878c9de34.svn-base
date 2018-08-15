package cn.finedo.daemon.gitlab;

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

import cn.finedo.fsdp.service.common.exception.TransactionException;
import cn.finedo.fsdp.service.common.jdbc.JdbcTemplate;

@Service
@Transactional
public class ProjectCodeStatService {
	
	@Resource(name="jdbcTemplate")
	private  JdbcTemplate jdbcTemplate;
	
	private static Logger logger = LogManager.getLogger(); 
	
	/**
	 * 报存代码统计结果
	 * @param stats
	 */
	public void add(List<ProjectCodeStat> stats) {
		try{
			this.jdbcTemplate.batchUpdate(new StringBuffer()
			.append("delete from  tb_rd_projectcodestat where projectid=:projectid and tool='gitlab' and date_format(opttime,'%Y-%m')=date_format(str_to_date(:opttime,'%Y-%m-%d %H:%i:%s'),'%Y-%m') and address=:address ")
			.toString(), stats);
			
			this.jdbcTemplate.batchUpdate(new StringBuffer()
			.append("insert into tb_rd_projectcodestat(statid,revision,projectid,codetype,opttime,userid,addcount,removecount,modifycount,tool,address) ")
			.append("values (:statid,:revision,:projectid,:codetype,STR_TO_DATE(:opttime,'%Y-%m-%d %H:%i:%s'),:userid,:addcount,:removecount,:modifycount,:tool,:address) ")
			.toString(), stats);
			logger.debug("添加代码量统计结果成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("添加代码量统计结果失败",e);
			throw new TransactionException(e);
		}
	}
	
	//需要查询的项目
	@Deprecated
	public List<Basecase> search(){
		try{
			List<Basecase> base=this.jdbcTemplate.query(new StringBuffer("  select projectid,path from rb_rd_baselineexpand where tool='gitlab' ").toString(), Basecase.class);
			return base;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("gitlab地址结果失败",e);
			throw new TransactionException(e);
		}
	}
	
	
	
	
	@Deprecated
	public void addAll(List<Basecase> base){
		try {
			StringBuffer sql=new StringBuffer("   select projectid from tb_rd_casebase    ");
			List<Basecase> 	retpage = jdbcTemplate.query(sql.toString(),Basecase.class);
			for (Basecase basecase : retpage) {
				for (Basecase bc : base) {
					if(basecase.getProjectid().equals(bc.getProjectid())){
						this.jdbcTemplate.batchUpdate(new StringBuffer()
						.append(" update tb_rd_basecase set codenums ")
						.append("values (:projectid,:projectname,:codenums) ")
						.toString(), base);
					}else{
						
					}
				}
			}
			
			logger.debug("添加代码量统计结果成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加代码总量统计结果失败",e);
			throw new TransactionException(e);
		}
	}
	
	/**
	 * 获取需要统计的项目日期
	 * @return
	 */
	public List<BaselineExtends> getStatList(){
		SimpleDateFormat sdf_month = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		String lastMonth = sdf_month.format(c.getTime());
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select a.projectid,a.tool,a.path,b.statmonth from rb_rd_baselineexpand a,                             ");
		stringBuffer.append("(select '"+lastMonth+"' statmonth from dual) b  where a.tool='gitlab'                                 ");
		
		List<BaselineExtends> ret = this.jdbcTemplate.query(stringBuffer.toString(), BaselineExtends.class);
		return ret;
	}
	
	/**
	 * 统计统计时间
	 * @return
	 */
	public void counttime(Basecase base){
		String sql="insert into tb_rd_tool  (id,month,tool,time,stat,opttime,address) values(:id,:month,:tool,:time,:stat,date_format(now(),'%Y-%m-%d %H:%i:%s'),:address) ";
		try{
			this.jdbcTemplate.update(sql.toString(), base);
			logger.debug("统计成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("统计失败",e);
			throw new TransactionException(e);
		}
		
		
	}
	
	/**
	 * 版本明细
	 * 
	 */
	public void addinfo(List<ProjectCodeStat> stats){
		try{
			this.jdbcTemplate.batchUpdate(new StringBuffer()
			.append("insert into tb_rd_gitlablog(statid,projectid,revision,codetype,userid,opttime,addcount,modifycount,removecount,tool,address) ")
			.append("values (:statid,:projectid,:revision,:codetype,:userid,STR_TO_DATE(:opttime,'%Y-%m-%d %H:%i:%s'),:addcount,:modifycount,:removecount,:tool,:address ) ")
			.toString(), stats);
			logger.debug("版本明细添加成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("版本明细添加失败",e);
			throw new TransactionException(e);
		}
	}
	
	/**
	 * 对比文件明细
	 */
	
	public void adddiff(List<Change> list,String projectid,String month){
		try{
			this.jdbcTemplate.batchUpdate(new StringBuffer()
			.append("insert into tb_rd_gitlabdiff(diffname,addcount,modifycount,removecount,path,codetype,projectid,month) ")
			.append("values (:diffname,:addcount,:modifycount,:removecount,:path,:codetype,'"+projectid+"',date_format('"+month+"','%Y-%m') )")
			.toString(), list);
			logger.debug("对比文件明细添加成功");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("对比文件明细添加失败",e);
			throw new TransactionException(e);
		}
	}
	
	
	
	
}
