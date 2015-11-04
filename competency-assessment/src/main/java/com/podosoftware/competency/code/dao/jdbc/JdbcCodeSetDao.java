package com.podosoftware.competency.code.dao.jdbc;

import com.podosoftware.competency.code.dao.CodeSetDao;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcCodeSetDao extends ExtendedJdbcDaoSupport implements CodeSetDao {

	private String sequencerName = "CODE_SET";
	
	private String sequencer2Name = "CODE";
	
	
	
}
