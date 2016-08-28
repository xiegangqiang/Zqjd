package com.xysoft.support;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Component;
@Component("solrDao")
@SuppressWarnings({"rawtypes", "unchecked"})
public class SolrJDaoImpl implements SolrJDao {
	
	private SolrServer solrServer;

	public void save(String url, Object param) {
		try {
			solrServer = new HttpSolrServer(url);
			solrServer.addBean(param);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(String url, String param) {
		try {
			solrServer = new HttpSolrServer(url);
			if(param != null && !param.equals("")){
				solrServer.deleteById(param);
				solrServer.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <T> List<T> find(String url, String field, String value, Class<T> clazz) {
		List<T> objs = new ArrayList<T>();
		try {
			solrServer = new HttpSolrServer(url);
			SolrQuery solrParams = new SolrQuery();
			//设置高亮
			solrParams.setHighlight(true);
			solrParams.setHighlightSimplePre("<font color='red'>");
			solrParams.setHighlightSimplePost("</font>");
			solrParams.setParam("hl.fl", field);
				
			solrParams.setQuery(field + ":" + value);
			//solrParams.setQuery("popularity:[0 TO 5]");//范围区间搜索
			//solrParams.setQuery("name:(苹果 AND 苹果 AND 5)");//多个值搜索
			//solrParams.setQuery("name:*苹果");//模糊查询搜索
			QueryResponse  queryResponse = solrServer.query(solrParams);
			SolrDocumentList solrDocumentList=queryResponse.getResults();
			//return toBeanList(solrDocumentList, clazz);
			DocumentObjectBinder binder = new DocumentObjectBinder();
			objs = (List<T>) binder.getBeans(clazz, solrDocumentList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objs;
	}
	
	public <T> Pager<T> getForPager(String url, String field, String value, Integer pageIndex, Integer rows, Class<T> clazz) {
		Pager<T> pager = new Pager<T>();
		try {
			solrServer = new HttpSolrServer(url);
			SolrQuery solrParams = new SolrQuery();
			//设置高亮
			solrParams.setHighlight(true);
			solrParams.setHighlightSimplePre("<font color='red'>");
			solrParams.setHighlightSimplePost("</font>");			
			solrParams.setParam("hl.fl", field);
			
			solrParams.setStart( (pageIndex -1) * rows );
			solrParams.setRows(rows);
			
			solrParams.setQuery(field + ":" + value);
			QueryResponse queryResponse=solrServer.query(solrParams);
			SolrDocumentList solrDocumentList=queryResponse.getResults();
			DocumentObjectBinder binder = new DocumentObjectBinder();
			//pager.setDatas(toBeanList(solrDocumentList, clazz));
			pager.setDatas((List<T>) binder.getBeans(clazz, solrDocumentList));
			pager.setTotal((int) queryResponse.getResults().getNumFound());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pager;
	}
	
	public <T> Pager<T> getForPager(String url, String field, Integer pageIndex, Integer rows, Class<T> clazz, Object... scope) {
		Pager<T> pager = new Pager<T>();
		try {
			solrServer = new HttpSolrServer(url);
			SolrQuery solrParams = new SolrQuery();
			//设置高亮
			solrParams.setHighlight(true);
			solrParams.setHighlightSimplePre("<font color='red'>");
			solrParams.setHighlightSimplePost("</font>");			
			solrParams.setParam("hl.fl", field);
			//分页
			solrParams.setStart( (pageIndex -1) * rows );
			solrParams.setRows(rows);
			
			if (scope != null && scope.length > 0) {
					solrParams.setQuery(field + ":" + "[" + scope[0] + " TO " + scope[1] + "]");//范围区间搜索
			}
			QueryResponse queryResponse=solrServer.query(solrParams);
			SolrDocumentList solrDocumentList=queryResponse.getResults();
			DocumentObjectBinder binder = new DocumentObjectBinder();
			//pager.setDatas(toBeanList(solrDocumentList, clazz));
			pager.setDatas((List<T>) binder.getBeans(clazz, solrDocumentList));
			pager.setTotal((int) queryResponse.getResults().getNumFound());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pager;
	}
	
	public <T> Pager<T> getForPager(String url, String field, String value, String sortField, Boolean sort, Integer pageIndex, Integer rows, Class<T> clazz, String scopeField, Object... scope) {
		Pager<T> pager = new Pager<T>();
		try {
			solrServer = new HttpSolrServer(url);
			SolrQuery solrParams = new SolrQuery();
			//设置高亮
			solrParams.setHighlight(true);
			solrParams.setHighlightSimplePre("<font color='red'>");
			solrParams.setHighlightSimplePost("</font>");			
			solrParams.setParam("hl.fl", field);
			//分页
			solrParams.setStart( (pageIndex -1) * rows );
			solrParams.setRows(rows);
			
			if(field !=null && !"".equals(field)){
				solrParams.setQuery(field + ":" + value);
			}
			if (scope != null && scope.length > 0) {
				solrParams.setQuery(scopeField + ":" + "[" + scope[0] + " TO " + scope[1] + "]");//范围区间字段搜索
			}
			if(sortField !=null && !"".equals(sortField)){
				if(sort){
					solrParams.addSort(sortField, ORDER.asc);
				} else{
					solrParams.addSort(sortField, ORDER.desc);
				}
			}
			QueryResponse queryResponse=solrServer.query(solrParams);
			SolrDocumentList solrDocumentList=queryResponse.getResults();
			DocumentObjectBinder binder = new DocumentObjectBinder();
			//pager.setDatas(toBeanList(solrDocumentList, clazz));
			pager.setDatas((List<T>) binder.getBeans(clazz, solrDocumentList));
			pager.setTotal((int) queryResponse.getResults().getNumFound());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pager;
	}

	protected static Object toBean(SolrDocument record, Class clazz) {
		Object obj = null;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Object value = record.get(field.getName());
			try {
				Method method = clazz.getMethod("set" + getMethodName(field.getName()), field.getType());
				method.invoke(obj, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

	protected static List<Object> toBeanList(SolrDocumentList records, Class  clazz){
        List  list = new ArrayList();
        for(SolrDocument record : records){
            list.add(toBean(record,clazz));
        }
        return list;
    }
	
	protected static String getMethodName(String fildeName) throws Exception{
		byte[] items = fildeName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}
	
	
}

















