package com.xysoft.support;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component("jdbcDao")
@SuppressWarnings({"unchecked"})
public class JdbcDaoImpl<T> implements JdbcDao<T>{
	
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public int execute(String sql) {
		int count = jdbcTemplate.execute(sql, new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) {
				int count = 0;
				try {
					ps.execute();
					ResultSet rs = ps.getResultSet();
					if(rs != null){
						while (rs.next()) {
							count ++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return count;
			}
		});
		return count;
	}
	
	public int execute(final String sql, final Object... param) {
		int count = jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) {
				PreparedStatement ps = null;
				try {
					ps = con.prepareStatement(sql);
					if (param != null && param.length > 0) {
						for (int i = 0; i < param.length; i++) {
							ps.setObject(i+1, param[i]);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ps;
			}
		}, new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) {
				int count = 0;
				try {
					ps.execute();
					ResultSet rs = ps.getResultSet();
					if(rs != null){
						while (rs.next()) {
							count ++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return count;
			}
		});
		return count;
	}
	
	public int executeMoreResult(final String sql, final int index, final Object... param) {
		int count = jdbcTemplate.execute(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) {
				PreparedStatement ps = null;
				try {
					ps = con.prepareStatement(sql);
					if (param != null && param.length > 0) {
						for (int i = 0; i < param.length; i++) {
							ps.setObject(i+1, param[i]);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return ps;
			}
		}, new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) {
				int count = 0;
				try {
					boolean hasresult =  ps.execute();
					int resultIndex = 0;
					while (hasresult) {
						if(resultIndex == index) {
							ResultSet rs = ps.getResultSet();
							if(rs != null){
								while (rs.next()) {
									count ++;
								}
							}
						}
						resultIndex++;
						hasresult = ps.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return count;
			}
		});
		return count;
	}
	
	public List<T> executeBatch(final List<String> sqls, final RowMapper<T> rowmapper) {
		final List<T> list = new ArrayList<T>();
		jdbcTemplate.execute(new ConnectionCallback<T>() {
			@Override
			public T doInConnection(Connection con){
				try {
					CallableStatement cs = null;
					for (String sql : sqls) {
						cs = con.prepareCall(sql);
						cs.execute();
					}
					ResultSet	rs = cs.getResultSet();
					int currentRow = 0;
					if(rs != null) {
						while (rs.next()) {
							 list.add(rowmapper.mapRow(rs, currentRow));
							 currentRow++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
	}
	
	public String save(final String sql, final Object... param) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update( new PreparedStatementCreator(){
            @Override
           public PreparedStatement createPreparedStatement(Connection conn){
            	PreparedStatement ps = null;
            	try {
            		ps = conn.prepareStatement(sql, new String[] {}); 
            		ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					if (param != null && param.length > 0) {
						for (int i = 0; i < param.length; i++) {
							ps.setObject(i+1, param[i]);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
                return ps;
            }
        }, keyHolder);
		return keyHolder.getKey().toString();
	}
	
	public List<T> call(final String sql, final RowMapper<T> rowmapper, final Object... param) {
		final List<T> list = new ArrayList<T>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					cs.execute();
					ResultSet	rs = cs.getResultSet();
					int currentRow = 0;
					if(rs != null){
						while (rs.next()) {
							 list.add(rowmapper.mapRow(rs, currentRow));
							 currentRow++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
					cs.execute();
					ResultSet	rs = cs.getResultSet();
					int currentRow = 0;
					if(rs != null){
						while (rs.next()) {
							 list.add(rowmapper.mapRow(rs, currentRow));
							 currentRow++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
		return list;*/
	}
	
	public List<T> call(final String sql, final int page, final int rows, final RowMapper<T> rowmapper, final Object... param) {
		final int startRow = (page - 1) * rows;
		final List<T> list = new ArrayList<T>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					cs.execute();
					ResultSet	rs = cs.getResultSet();
					int currentRow = 0;
					if(rs != null) {
						while (rs.next()) {
							 if(startRow<rs.getRow() && rs.getRow() <= startRow + rows) {
								 list.add(rowmapper.mapRow(rs, currentRow));
								 currentRow++;
							 }
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
					cs.execute();
					ResultSet	rs = cs.getResultSet();
					int currentRow = 0;
					if(rs != null) {
						while (rs.next()) {
							 if(startRow<rs.getRow() && rs.getRow() <= startRow + rows) {
								 list.add(rowmapper.mapRow(rs, currentRow));
								 currentRow++;
							 }
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
		return list;*/
	}
	
	public Pager<T>  callForPager(String sql, final PageParam page, final RowMapper<T> rowmapper, Object... param) {
		int rowCount = this.execute(sql, param);
		int pageCount = rowCount / page.getLimit();
		if (rowCount > page.getLimit() * pageCount) {
			pageCount++;
		}
		final Pager<T> pager = new Pager<T>();
		pager.setTotal(rowCount);
		pager.setPageIndex(page.getPage());
		pager.setPageCount(pageCount);
		pager.setDatas(this.call(sql, page.getPage(), page.getLimit(), rowmapper, param));
		return pager;
	}
	
	public List<T> callMoreResult(final String sql, final int index, final RowMapper<T> rowmapper, final Object... param) {
		final List<T> list = new ArrayList<T>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						if(resultIndex == index) {
							ResultSet rs = cs.getResultSet();
							if(rs != null){
								int currentRow = 0;
								while (rs.next()) {
									list.add(rowmapper.mapRow(rs, currentRow));
									currentRow++;
								}
							}
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						if(resultIndex == index) {
							ResultSet rs = cs.getResultSet();
							if(rs != null){
								int currentRow = 0;
								while (rs.next()) {
									list.add(rowmapper.mapRow(rs, currentRow));
									currentRow++;
								}
							}
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
		return list;*/
	}
	
	public List<Object> callMoreResult(final String sql, final List<Object> param, final Object... objects) {
		final List<Object> lists = new ArrayList<Object>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.size(); i++) {
						String clazz = param.get(i).getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String) param.get(i));
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer) param.get(i));
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean) param.get(i));
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param.get(i));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						ResultSet rs = cs.getResultSet();
						if(rs != null){
							int currentRow = 0;
							List<Object> list = new ArrayList<Object>();
							while (rs.next()) {
								list.add(objects[resultIndex].getClass().getMethod("mapRow", ResultSet.class, int.class).invoke(objects[resultIndex], rs, currentRow));
								currentRow++;
							}
							lists.add(list);
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return lists;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.size(); i++) {
						String clazz = param.get(i).getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String) param.get(i));
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer) param.get(i));
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean) param.get(i));
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param.get(i));
					}
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						ResultSet rs = cs.getResultSet();
						if(rs != null){
							int currentRow = 0;
							List<Object> list = new ArrayList<Object>();
							while (rs.next()) {
								list.add(objects[resultIndex].getClass().getMethod("mapRow", ResultSet.class, int.class).invoke(objects[resultIndex], rs, currentRow));
								currentRow++;
							}
							lists.add(list);
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
		return lists;*/
	}
	
	public List<T> callMoreResult(final String sql, final int index, final int page, final int rows, final RowMapper<T> rowmapper, final Object... param) {
		final int startRow = (page - 1) * rows;
		final List<T> list = new ArrayList<T>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						if(resultIndex == index) {
							ResultSet rs = cs.getResultSet();
							if(rs != null){
								int currentRow = 0;
								while (rs.next()) {
									 if(startRow<rs.getRow() && rs.getRow() <= startRow + rows) {
										 list.add(rowmapper.mapRow(rs, currentRow));
										 currentRow++;
									 }
								}
							}
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						if(resultIndex == index) {
							ResultSet rs = cs.getResultSet();
							if(rs != null){
								int currentRow = 0;
								while (rs.next()) {
									 if(startRow<rs.getRow() && rs.getRow() <= startRow + rows) {
										 list.add(rowmapper.mapRow(rs, currentRow));
										 currentRow++;
									 }
								}
							}
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
	    return list;*/
	}
	
	public List<Object> callMoreResult(final String sql, final int page, final int rows, final List<Object> param, final Object... objects) {
		final int startRow = (page - 1) * rows;
		final List<Object> lists = new ArrayList<Object>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.size(); i++) {
						String clazz = param.get(i).getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String) param.get(i));
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer) param.get(i));
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean) param.get(i));
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param.get(i));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						ResultSet rs = cs.getResultSet();
						if(rs != null){
							int currentRow = 0;
							List<Object> list = new ArrayList<Object>();
							while (rs.next()) {
								 if(startRow<rs.getRow() && rs.getRow() <= startRow + rows) {
									 list.add(objects[resultIndex].getClass().getMethod("mapRow", ResultSet.class, int.class).invoke(objects[resultIndex], rs, currentRow));
									 currentRow++;
								 }
							}
							lists.add(list);
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return lists;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.size(); i++) {
						String clazz = param.get(i).getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String) param.get(i));
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer) param.get(i));
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean) param.get(i));
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param.get(i));
					}
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						ResultSet rs = cs.getResultSet();
						if(rs != null){
							int currentRow = 0;
							List<Object> list = new ArrayList<Object>();
							while (rs.next()) {
								 if(startRow<rs.getRow() && rs.getRow() <= startRow + rows) {
									 list.add(objects[resultIndex].getClass().getMethod("mapRow", ResultSet.class, int.class).invoke(objects[resultIndex], rs, currentRow));
									 currentRow++;
								 }
							}
							lists.add(list);
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
		return lists;*/
	}
	
	public Pager<T>  callMoreResultForPager(String sql, final int index, final PageParam page, final RowMapper<T> rowmapper, Object... param) {
		int rowCount = this.executeMoreResult(sql, index, param);
		int pageCount = rowCount / page.getLimit();
		if (rowCount > page.getLimit() * pageCount) {
			pageCount++;
		}
		final Pager<T> pager = new Pager<T>();
		pager.setTotal(rowCount);
		pager.setPageIndex(page.getPage());
		pager.setPageCount(pageCount);
		pager.setDatas(this.callMoreResult(sql, index, page.getPage(), page.getLimit(), rowmapper, param));
		return pager;
	}
	
	public List<T> queryForList(String sql) {
		return (List<T>) jdbcTemplate.queryForList(sql);
	}
	
	public List<T> queryForList(String sql, Object... param) {
		return (List<T>) jdbcTemplate.queryForList(sql, param);
	}
	
	public List<T> queryForList(String sql, Class<T> elementType, Object... param) {
		return jdbcTemplate.queryForList(sql, elementType, param);
	}
	
	public Map<String, T> queryForMap(String sql, Object... param) {
		return (Map<String, T>) jdbcTemplate.queryForMap(sql, param);
	}
	
	public List<T> query(String sql, RowMapper<T> rowmapper) {
		List<T> list = jdbcTemplate.query(sql, rowmapper);
		return list;
	}
	
	public List<T> query(String sql, Object[] param, RowMapper<T> rowmapper) {
		List<T> list = jdbcTemplate.query(sql, param, rowmapper);
		return list;
	}
	
	public List<T> query(String sql, RowMapper<T> rowmapper, Object... param) {
		List<T> list = jdbcTemplate.query(sql, rowmapper, param);
		return list;
	}
	
	public List<T> find(String sql, final RowMapper<T> rowmapper, Object... param) {
		List<T> list = jdbcTemplate.query(sql, new ResultSetExtractor<List<T>>() {
			@Override
			public List<T> extractData(ResultSet rs) {
				List<T> list = new ArrayList<T>();
				try {
					int currentRow = 0;
					if(rs != null) {
						while (rs.next()) {
							 list.add(rowmapper.mapRow(rs, currentRow));
							 currentRow++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}
		}, param);
		return list;
	}
	
	public List<T> find(String sql, final RowMapper<T> rowmapper, final List<T> param) {
		final List<T> list = new ArrayList<T>();
		jdbcTemplate.query(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps)  {
				try {
					if(param!=null && param.size()>0) {
						for (int i = 0; i < param.size(); i++) {
							ps.setObject(i+1, param.get(i));
						}
					}
					ps.execute();
					ResultSet rs = ps.getResultSet();
					int currentRow = 0;
					if(rs != null) {
						while (rs.next()) {
							list.add(rowmapper.mapRow(rs, currentRow));
							currentRow ++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, rowmapper);
		return list;
	}
	
	public List<T> find(String sql, final int page, final int rows, final RowMapper<T> rowmapper, Object... param) {
		final int startRow = (page - 1) * rows;
		List<T> list = jdbcTemplate.query(sql, new ResultSetExtractor<List<T>>(){
			@Override
			public List<T> extractData(ResultSet rs){
				List<T> list = new ArrayList<T>();
				try {
					int currentRow = 0;
					if(rs != null) {
						while (rs.next()) {
							 if(startRow<rs.getRow() && rs.getRow() <= startRow + rows) {
								 list.add(rowmapper.mapRow(rs, currentRow));
								 currentRow++;
							 }
						}
					}
					return list;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}
		}, param);
		return list;
	}
	
	public List<T> find(String sql, final int page, final int rows, final RowMapper<T> rowmapper, final List<T> param) {
		final int startRow = (page - 1) * rows;
		final List<T> list = new ArrayList<T>();
		jdbcTemplate.query(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps)  {
				try {
					if(param!=null && param.size()>0) {
						for (int i = 0; i < param.size(); i++) {
							ps.setObject(i+1, param.get(i));
						}
					}
					ps.execute();
					ResultSet rs = ps.getResultSet();
					int currentRow = 0;
					if(rs != null) {
						while (rs.next()) {
							 if(startRow<rs.getRow() && rs.getRow() <= startRow + rows) {
								 list.add(rowmapper.mapRow(rs, currentRow));
								 currentRow++;
							 }
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, rowmapper);
		return list;
	}
	
	public T get(final String sql, final RowMapper<T> rowmapper, final Object... param) {
		List<T> list = this.find(sql, rowmapper, param);
		if(list.size() == 0) return null;
		return list.get(0);
	}
	
	public T get(final String sql, final RowMapper<T> rowmapper, final List<T> param) {
		List<T> list = this.find(sql, rowmapper, param);
		if(list.size() == 0) return null;
		return list.get(0);
	}
	
	public T queryForObject(String sql, Class<T> elementType, Object... param) {
		return jdbcTemplate.queryForObject(sql, elementType, param);
	}
	
	public T queryForObject(String sql, Object[] param, RowMapper<T> rowmapper) {
		return  jdbcTemplate.queryForObject(sql, param, rowmapper);
	}
	
	public T queryForObject(String sql, RowMapper<T> rowmapper, Object... param) {
		return  jdbcTemplate.queryForObject(sql, rowmapper, param);
	}
	
	public Pager<T> queryForPager(String sql, final int page, final int rows, final RowMapper<T> rowmapper, Object... param) {
		final Pager<T> pager = new Pager<T>();
		int rowCount = this.execute(sql, param);
		int pageCount = rowCount / rows;
		if (rowCount > page * pageCount) {
			pageCount++;
		}
		pager.setPageIndex(page);
		pager.setTotal(rowCount);
		pager.setPageCount(pageCount);
		pager.setDatas(this.find(sql, page, rows, rowmapper, param));
		return pager;
	}
	
	public Pager<T> queryForPager(String sql, final int page, final int rows, final RowMapper<T> rowmapper, List<T> param) {
		final Pager<T> pager = new Pager<T>();
		int rowCount = this.execute(sql, param);
		int pageCount = rowCount / rows;
		if (rowCount > page * pageCount) {
			pageCount++;
		}
		pager.setPageIndex(page);
		pager.setTotal(rowCount);
		pager.setPageCount(pageCount);
		pager.setDatas(this.find(sql, page, rows, rowmapper, param));
		return pager;
	}
	
	public Pager<T> queryForPager(String sql, final PageParam page, final RowMapper<T> rowmapper, Object... param) {
		int rowCount = this.execute(sql, param);
		int pageCount = rowCount / page.getLimit();
		if (rowCount > page.getLimit() * pageCount) {
			pageCount++;
		}
		final Pager<T> pager = new Pager<T>();
		pager.setPageIndex(page.getPage());
		pager.setTotal(rowCount);
		pager.setPageCount(pageCount);
		pager.setDatas(this.find(sql, page.getPage(), page.getLimit(), rowmapper, param));
		return pager;
	}
	
	public Pager<T> queryForPager(String sql, final PageParam page, final RowMapper<T> rowmapper, List<T> param) {
		int rowCount = this.execute(sql, param);
		int pageCount = rowCount / page.getLimit();
		if (rowCount > page.getLimit() * pageCount) {
			pageCount++;
		}
		final Pager<T> pager = new Pager<T>();
		pager.setPageIndex(page.getPage());
		pager.setTotal(rowCount);
		pager.setPageCount(pageCount);
		pager.setDatas(this.find(sql, page.getPage(), page.getLimit(), rowmapper, param));
		return pager;
	}
	
	
	/**************************************************动态Bean****************************************************************/
	private static List<DynamicBean> setBeanValue(ResultSet rs) {
		List<DynamicBean> list = new ArrayList<DynamicBean>();
		try {
			if(rs != null) {
				HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					if("java.sql.Timestamp".equals(rsmd.getColumnClassName(i+1))) map.put(rsmd.getColumnLabel(i+1), Class.forName("java.util.Date"));
					else map.put(rsmd.getColumnLabel(i+1), Class.forName( rsmd.getColumnClassName(i+1) ));
				}
				while (rs.next()) {
					DynamicBean bean = new DynamicBean(map);
					for (int j = 0; j < rsmd.getColumnCount(); j++) {
						String field = rsmd.getColumnLabel(j+1);
						if( "class java.lang.Object".equals(map.get(field).toString()) ) bean.setValue(field, rs.getString(field));
						if( "class java.lang.String".equals(map.get(field).toString()) ) bean.setValue(field, rs.getString(field));
						if( "class java.lang.Long".equals(map.get(field).toString()) ) bean.setValue(field, rs.getLong(field));
						if( "class java.lang.Boolean".equals(map.get(field).toString()) ) bean.setValue(field, rs.getBoolean(field));
						if( "class java.lang.Integer".equals(map.get(field).toString()) ) bean.setValue(field, rs.getInt(field));
						if( "class java.lang.Float".equals(map.get(field).toString()) ) bean.setValue(field, rs.getFloat(field));
						if( "class java.lang.Double".equals(map.get(field).toString()) ) bean.setValue(field, rs.getDouble(field));
						if( "class java.math.BigDecimal".equals(map.get(field).toString()) ) bean.setValue(field, rs.getBigDecimal(field));
						if( "class java.util.Date".equals(map.get(field).toString()) ) bean.setValue(field, new Date(rs.getTimestamp(field).getTime()) );
					}
					list.add(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private static List<DynamicBean> setBeanValue(ResultSet rs, int startRow, int rows) {
		List<DynamicBean> list = new ArrayList<DynamicBean>();
		try {
			if(rs != null) {
				HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					if("java.sql.Timestamp".equals(rsmd.getColumnClassName(i+1))) map.put(rsmd.getColumnLabel(i+1), Class.forName("java.util.Date"));
					else map.put(rsmd.getColumnLabel(i+1), Class.forName( rsmd.getColumnClassName(i+1) ));
				}
				while (rs.next()) {
					if(startRow<rs.getRow() && rs.getRow() <= startRow + rows) {
						DynamicBean bean = new DynamicBean(map);
						for (int j = 0; j < rsmd.getColumnCount(); j++) {
							String field = rsmd.getColumnLabel(j+1);
							if( "class java.lang.Object".equals(map.get(field).toString()) ) bean.setValue(field, rs.getString(field));
							if( "class java.lang.String".equals(map.get(field).toString()) ) bean.setValue(field, rs.getString(field));
							if( "class java.lang.Long".equals(map.get(field).toString()) ) bean.setValue(field, rs.getLong(field));
							if( "class java.lang.Boolean".equals(map.get(field).toString()) ) bean.setValue(field, rs.getBoolean(field));
							if( "class java.lang.Integer".equals(map.get(field).toString()) ) bean.setValue(field, rs.getInt(field));
							if( "class java.lang.Float".equals(map.get(field).toString()) ) bean.setValue(field, rs.getFloat(field));
							if( "class java.lang.Double".equals(map.get(field).toString()) ) bean.setValue(field, rs.getDouble(field));
							if( "class java.math.BigDecimal".equals(map.get(field).toString()) ) bean.setValue(field, rs.getBigDecimal(field));
							if( "class java.util.Date".equals(map.get(field).toString()) ) bean.setValue(field, new Date(rs.getTimestamp(field).getTime()) );
						}
						list.add(bean);
					 }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<DynamicBean> executeBatch(final List<String> sqls) {
		List<DynamicBean> list = jdbcTemplate.execute(new ConnectionCallback<List<DynamicBean>>() {
			@Override
			public List<DynamicBean> doInConnection(Connection con){
				ResultSet rs = null;
				CallableStatement cs = null;
				try {
					for (String sql : sqls) {
						cs = con.prepareCall(sql);
						cs.execute();
					}
					rs = cs.getResultSet();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return setBeanValue(rs);
			}
		});
		return list;
	}
	
	public List<DynamicBean> call(final String sql, final Object... param) {
		final List<DynamicBean> list = new ArrayList<DynamicBean>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					cs.execute();
					ResultSet rs = cs.getResultSet();
					List<DynamicBean> beans = setBeanValue(rs);
					for (DynamicBean bean : beans) {
						list.add(bean);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				ResultSet rs = null;
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
					cs.execute();
					rs = cs.getResultSet();
					List<DynamicBean> beans = setBeanValue(rs);
					for (DynamicBean bean : beans) {
						list.add(bean);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
		return list;*/
	}
	
	public List<DynamicBean> call(final String sql, final int page, final int rows, final Object... param) {
		final int startRow = (page - 1) * rows;
		final List<DynamicBean> list = new ArrayList<DynamicBean>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					cs.execute();
					ResultSet rs = cs.getResultSet();
					List<DynamicBean> beans = setBeanValue(rs, startRow, rows);
					for(DynamicBean bean : beans) {
						list.add(bean);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
					cs.execute();
					ResultSet rs = cs.getResultSet();
					List<DynamicBean> beans = setBeanValue(rs, startRow, rows);
					for(DynamicBean bean : beans) {
						list.add(bean);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
		return list;*/
	}
	
	public Pager<DynamicBean> callForPager(String sql, PageParam page, Object... param) {
		int rowCount = this.execute(sql, param);
		int pageCount = rowCount / page.getLimit();
		if (rowCount > page.getLimit() * pageCount) {
			pageCount++;
		}
		Pager<DynamicBean> pager = new Pager<DynamicBean>();
		pager.setTotal(rowCount);
		pager.setPageIndex(page.getPage());
		pager.setPageCount(pageCount);
		pager.setDatas(this.call(sql, page.getPage(), page.getLimit(), param));
		return pager;
	}
	
	public List<List<DynamicBean>> callMoreResult(final String sql, final Object... param) {
		final List<List<DynamicBean>> list = new ArrayList<List<DynamicBean>>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					boolean hasresult =  cs.execute();
					while (hasresult) {
						ResultSet rs = cs.getResultSet();
						List<DynamicBean> beans = setBeanValue(rs);
						list.add(beans);
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
	}
	
	public List<DynamicBean> callMoreResult(final String sql, final int index, final Object... param) {
		final List<DynamicBean> list = new ArrayList<DynamicBean>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						if(resultIndex == index) {
							ResultSet rs = cs.getResultSet();
							List<DynamicBean> beans = setBeanValue(rs);
							for (DynamicBean bean : beans) {
								list.add(bean);
							}
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						if(resultIndex == index) {
							ResultSet rs = cs.getResultSet();
							List<DynamicBean> beans = setBeanValue(rs);
							for (DynamicBean bean : beans) {
								list.add(bean);
							}
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
		return list;*/
	}
	
	public List<DynamicBean> callMoreResult(final String sql, final int index, int page, final int rows, final Object... param) {
		final int startRow = (page - 1) * rows;
		final List<DynamicBean> list = new ArrayList<DynamicBean>();
		jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) {
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, new CallableStatementCallback<T>() {
			@Override
			public T doInCallableStatement(CallableStatement cs) {
				try {
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						if(resultIndex == index) {
							ResultSet rs = cs.getResultSet();
							List<DynamicBean> beans = setBeanValue(rs, startRow, rows);
							for(DynamicBean bean : beans) {
								list.add(bean);
							}
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		return list;
/*		List<SqlParameter> sqlparam = new ArrayList<SqlParameter>();
	    jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con){
				CallableStatement cs = null;
				try {
					cs = con.prepareCall(sql);
					for (int i = 0; i < param.length; i++) {
						String clazz = param[i].getClass().toString();
						if(clazz.equals("class java.lang.String")) cs.setString(i+1, (String)param[i]);
						if(clazz.equals("class java.lang.Integer")) cs.setInt(i+1, (Integer)param[i]);
						if(clazz.equals("class java.lang.Boolean")) cs.setBoolean(i+1, (Boolean)param[i]);
						if(clazz.equals("class java.lang.Float")) cs.setFloat(i+1, (Float) param[i]);
					}
					boolean hasresult =  cs.execute();
					int resultIndex = 0;
					while (hasresult) {
						if(resultIndex == index) {
							ResultSet rs = cs.getResultSet();
							List<DynamicBean> beans = setBeanValue(rs, startRow, rows);
							for(DynamicBean bean : beans) {
								list.add(bean);
							}
						}
						resultIndex++;
						hasresult = cs.getMoreResults();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return cs;
			}
		}, sqlparam);
	    return list;*/
	}
	
	public Pager<DynamicBean> callMoreResultForPager(String sql, int index, PageParam page, Object... param) {
		int rowCount = this.executeMoreResult(sql, index, param);
		int pageCount = rowCount / page.getLimit();
		if (rowCount > page.getLimit() * pageCount) {
			pageCount++;
		}
		final Pager<DynamicBean> pager = new Pager<DynamicBean>();
		pager.setTotal(rowCount);
		pager.setPageIndex(page.getPage());
		pager.setPageCount(pageCount);
		pager.setDatas(this.callMoreResult(sql, index, page.getPage(), page.getLimit(), param));
		return pager;
	}

	public List<DynamicBean> find(String sql, Object... param) {
		List<DynamicBean> list = jdbcTemplate.query(sql, new ResultSetExtractor<List<DynamicBean>>() {
			@Override
			public List<DynamicBean> extractData(ResultSet rs) {
				return setBeanValue(rs);
			}
		}, param);
		return list;
	}
	
	public List<DynamicBean> find(String sql, final List<Object> param) {
		final List<DynamicBean> list = new ArrayList<DynamicBean>();
		jdbcTemplate.query(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps)  {
				try {
					if(param!=null && param.size()>0) {
						for (int i = 0; i < param.size(); i++) {
							ps.setObject(i+1, param.get(i));
						}
					}
					ps.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				List<DynamicBean> beans = setBeanValue(rs);
				for (DynamicBean bean : beans) {
					list.add(bean);
				}
				return null;
			}
		});
		return list;
	}
	
	public List<DynamicBean> find(String sql, int page, final int rows, Object... param) {
		final int startRow = (page - 1) * rows;
		List<DynamicBean> list = jdbcTemplate.query(sql, new ResultSetExtractor<List<DynamicBean>>(){
			@Override
			public List<DynamicBean> extractData(ResultSet rs){
				return setBeanValue(rs, startRow, rows);
			}
		}, param);
		return list;
	}

	public List<DynamicBean> find(String sql, int page, final int rows, final List<Object> param) {
		final int startRow = (page - 1) * rows;
		final List<DynamicBean> list = new ArrayList<DynamicBean>();
		jdbcTemplate.query(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps)  {
				try {
					if(param!=null && param.size()>0) {
						for (int i = 0; i < param.size(); i++) {
							ps.setObject(i+1, param.get(i));
						}
					}
					ps.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new RowMapper<T>() {
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {
				List<DynamicBean> beans = setBeanValue(rs, startRow, rows);
				for (DynamicBean bean : beans) {
					list.add(bean);
				}
				return null;
			}
		});
		return list;
	}
	
	public DynamicBean get(String sql, Object... param) {
		List<DynamicBean> list = this.find(sql, param);
		if(list.size() == 0) return null;
		return list.get(0);
	}
	
	public DynamicBean get(String sql, List<Object> param) {
		List<DynamicBean> list = this.find(sql, param);
		if(list.size() == 0) return null;
		return list.get(0);
	}
	
	public Pager<DynamicBean> queryForPager(String sql, int page, int rows, Object... param) {
		final Pager<DynamicBean> pager = new Pager<DynamicBean>();
		int rowCount = this.execute(sql, param);
		int pageCount = rowCount / rows;
		if (rowCount > page * pageCount) {
			pageCount++;
		}
		pager.setPageIndex(page);
		pager.setTotal(rowCount);
		pager.setPageCount(pageCount);
		pager.setDatas(this.find(sql, page, rows, param));
		return pager;
	}
	
	public Pager<DynamicBean> queryForPager(String sql, PageParam page, List<Object> param) {
		int rowCount = this.execute(sql, param);
		int pageCount = rowCount / page.getLimit();
		if (rowCount > page.getLimit() * pageCount) {
			pageCount++;
		}
		final Pager<DynamicBean> pager = new Pager<DynamicBean>();
		pager.setPageIndex(page.getPage());
		pager.setTotal(rowCount);
		pager.setPageCount(pageCount);
		pager.setDatas(this.find(sql, page.getPage(), page.getLimit(), param));
		return pager;
	}
	
	public Pager<DynamicBean> queryForPager(String sql, PageParam page, Object... param) {
		int rowCount = this.execute(sql, param);
		int pageCount = rowCount / page.getLimit();
		if (rowCount > page.getLimit() * pageCount) {
			pageCount++;
		}
		final Pager<DynamicBean> pager = new Pager<DynamicBean>();
		pager.setPageIndex(page.getPage());
		pager.setTotal(rowCount);
		pager.setPageCount(pageCount);
		pager.setDatas(this.find(sql, page.getPage(), page.getLimit(), param));
		return pager;
	}
	
}













